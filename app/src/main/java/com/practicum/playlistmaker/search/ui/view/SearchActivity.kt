package com.practicum.playlistmaker.search.ui.view

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.player.ui.activity.LibraryActivity
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.viewModel.TracksState
import org.koin.android.ext.android.inject


@Suppress("DEPRECATION")
class SearchActivity() : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel:SearchViewModel by inject()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var textWatcher  : TextWatcher
    private var tracks : MutableList<Track> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var searchRequest:String=""
        var history : MutableList<Track> = mutableListOf()
        var historyMaxLength=viewModel?.getTracksInHistoryMaxLength()?:1

        viewModel?.observeState()?.observe(this) {
            render(it)
        }
        viewModel?.observeHistory()?.observe(this) {
            history.clear()
            history.addAll(it)
        }

        binding.clearHistory.setOnClickListener {
            viewModel?.clearHistory()
            binding.tracksHistoryList.adapter?.notifyDataSetChanged()
            binding.searchHistory.visibility=View.GONE
        }

        binding.refrashButton.setOnClickListener {
            viewModel?.searchDebounce(searchRequest)
        }

        binding.backToMainFromSearch.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

        binding.clearSearch.setOnClickListener {
            binding.inputEditText.setText("")
            searchRequest=""
            tracks.clear()
            binding.tracksList.adapter?.notifyDataSetChanged()
            viewModel?.destroy()
            binding.errorTrackNotFound.visibility = View.GONE
            binding.errorNoInternet.visibility = View.GONE
            this.currentFocus?.let { view ->
               val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
               imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
       }

        if (searchRequest!=null) {
            binding.inputEditText.setText(searchRequest)
        }

        textWatcher =  object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               searchRequest = s?.toString() ?: ""
               if (searchRequest.isEmpty()) {
                   binding.clearSearch.visibility = View.GONE
                   if (binding.inputEditText.hasFocus() && !history.isEmpty())
                       binding.searchHistory.visibility=View.VISIBLE
                   else  binding.searchHistory.visibility=View.GONE

               } else {
                   binding.clearSearch.visibility = View.VISIBLE
                   viewModel?.searchDebounce(searchRequest)
               }
           }

        }
        binding.inputEditText.addTextChangedListener(textWatcher)

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus &&  binding.inputEditText.text.isEmpty() && !history.isEmpty())
                binding.searchHistory.visibility=View.VISIBLE
            else  binding.searchHistory.visibility=View.GONE
        }

        binding.tracksList.adapter = TracksAdapter(tracks, object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Для предотвращения двойных нажатий на элемент
                if (clickDebounce()){
                    viewModel?.chooseTrack(tracks[position])
                    binding.tracksHistoryList.adapter?.notifyItemRemoved(historyMaxLength-1)
                    binding.tracksHistoryList.adapter?.notifyItemInserted(0)
                    toLibrary(tracks[position])
                }
            }
        })
        binding.tracksList.layoutManager = LinearLayoutManager(this)

        binding.tracksHistoryList.adapter = TracksAdapter(history, object:
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickDebounce()) {
                    toLibrary(history[position])
                }
            }
        })
        binding.tracksHistoryList.layoutManager = LinearLayoutManager(this)
    }

    private fun render(state: TracksState) {
         binding.errorNoInternet.visibility = View.GONE
         binding.errorTrackNotFound.visibility = View.GONE
         binding.progressBar.visibility = View.GONE
         binding.searchHistory.visibility = View.GONE
        when (state){
            is TracksState.Error->showError()
            is TracksState.Empty->showEmpty()
            is TracksState.Loading->showLoading()
            is TracksState.WaitingForRequest->showHistory()
            is TracksState.Content->showContent(state.tracks)
        }
    }
    fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }
    fun showContent(tracksList: List<Track>) {
        tracks.clear()
        tracks.addAll(tracksList)
        binding.tracksList.adapter?.notifyDataSetChanged()
    }
    fun showError() {
        tracks.clear()
        binding.tracksList.adapter?.notifyDataSetChanged()
        binding.errorNoInternet.visibility = View.VISIBLE
    }
    fun showEmpty() {
        tracks.clear()
        binding.tracksList.adapter?.notifyDataSetChanged()
        binding.errorTrackNotFound.visibility = View.VISIBLE
    }
    fun showHistory(){
        binding.searchHistory.visibility=View.VISIBLE
    }

    private fun toLibrary(item: Track) {
        val intent = Intent(this, LibraryActivity::class.java)
        intent.putExtra(CHECKED_TRACK, item)
        this.onPause()
        startActivity(intent)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.destroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    companion object {
        const val CHECKED_TRACK = "CHECKED_TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}