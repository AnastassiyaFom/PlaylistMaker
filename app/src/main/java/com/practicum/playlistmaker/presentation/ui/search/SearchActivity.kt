package com.practicum.playlistmaker.presentation.ui.search

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.creator.Creator.provideTrackHistoryInteractor
import com.practicum.playlistmaker.creator.Creator.provideTracksInteractor
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.interfaces.interactors.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.interfaces.interactors.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.library.LibraryActivity
import com.practicum.playlistmaker.presentation.ui.main.MainActivity



@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {
    private var searchRequest:String?=null

    private lateinit var binding: ActivitySearchBinding
    //Вспомагательные переменные для debouncer
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequestToNet() }

    private var tracks : MutableList<Track> = mutableListOf()
    private lateinit var tracksHistoryInteractor: TracksHistoryInteractor


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
        outState.putParcelableArrayList(TRACKS_LIST, tracks as ArrayList<Track>)
   }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST, SEARCH_REQUEST_DEF)
        tracks = savedInstanceState.getParcelableArrayList<Track>(TRACKS_LIST)!!
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }

        tracksHistoryInteractor = provideTrackHistoryInteractor(this)
        binding.clearHistory.setOnClickListener {
            tracksHistoryInteractor.clearHistory()
            binding.tracksHistoryList.adapter?.notifyDataSetChanged()
            binding.searchHistory.visibility=View.GONE
        }

        binding.refrashButton.setOnClickListener {
            searchRequestToNet()
        }

        binding.backToMainFromSearch.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

        binding.clearSearch.setOnClickListener {
            binding.inputEditText.setText("")
            binding.errorTrackNotFound.visibility = View.GONE
            binding.errorNoInternet.visibility = View.GONE
            tracks.clear()
            binding.tracksList.adapter?.notifyDataSetChanged()
            this.currentFocus?.let { view ->
               val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
               imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
       }

        if (searchRequest!=null) {
            binding.inputEditText.setText(searchRequest)
        }

        val simpleTextWatcher = object : TextWatcher {

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // empty
           }
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s.isNullOrEmpty()) {
                   binding.clearSearch.visibility = clearButtonVisibility(s)
                   if ( binding.inputEditText.hasFocus() && tracksHistoryInteractor.getTracksFromHistory().isNotEmpty())
                       binding.searchHistory.visibility=View.VISIBLE
                   else  binding.searchHistory.visibility=View.GONE

               } else {
                   searchRequest = s.toString()
                   searchDebounce()
               }
               binding.clearSearch.visibility = clearButtonVisibility(s)
           }
           override fun afterTextChanged(s: Editable?) {
               // empty

           }
       }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus &&  binding.inputEditText.text.isEmpty() && tracksHistoryInteractor.getTracksFromHistory().isNotEmpty())
                binding.searchHistory.visibility=View.VISIBLE
            else  binding.searchHistory.visibility=View.GONE
        }

        binding.tracksList.adapter = TracksAdapter(tracks, object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Для предотвращения двойных нажатий на элемент
                if (clickDebounce()){
                    tracksHistoryInteractor.addTrackToHistory(tracks[position])
                    binding.tracksHistoryList.adapter?.notifyItemRemoved(tracksHistoryInteractor.getTracksInHistoryMaxLength()-1)
                    binding.tracksHistoryList.adapter?.notifyItemInserted(0)
                    toLibrary(tracks[position])
                }
            }
        })
        binding.tracksList.layoutManager = LinearLayoutManager(this)

        binding.tracksHistoryList.adapter = TracksAdapter(tracksHistoryInteractor.getTracksFromHistory(), object:
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickDebounce()) {
                    toLibrary(tracksHistoryInteractor.getTracksFromHistory()[position])
                }
            }
        })
        binding.tracksHistoryList.layoutManager = LinearLayoutManager(this)
    }


    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            return  View.GONE

        } else {
            return View.VISIBLE
        }
    }

    //!!!!!!!!!!!!!!!!!!!!! Как то смущает здесь могократный вызов handler.post
    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>, resultCode: Int) {
            handler.post {  binding.errorNoInternet.visibility = View.GONE}
            handler.post {  binding.errorTrackNotFound.visibility = View.GONE}
            handler.post {  binding.progressBar.visibility = View.GONE}
            // Получили ответ от сервера
            if (resultCode >= 200 && resultCode<300 && foundTracks.isNotEmpty() == true && foundTracks != null) {
                // Наш запрос был удачным, получаем ответ в JSON-тексте
                tracks.clear()
                tracks.addAll(foundTracks)
                handler.post {  binding.tracksList.adapter?.notifyDataSetChanged() }
            }
            else {
                tracks.clear()
                handler.post {  binding.tracksList.adapter?.notifyDataSetChanged()}
                if (resultCode == 400) {
                    // показываем, что нет инета
                    handler.post {  binding.errorNoInternet.visibility = View.VISIBLE}
                } else {
                    // показываем, что ничего не найдено
                    handler.post {  binding.errorTrackNotFound.visibility = View.VISIBLE}
                }
            }
        }
    }

    private fun searchRequestToNet(){
        if ( binding.inputEditText.text.isNotEmpty()) {
            binding.searchHistory.visibility=View.GONE
            binding.errorNoInternet.visibility = View.GONE
            binding.errorTrackNotFound.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            val tracksInteractor: TracksInteractor = provideTracksInteractor()
            tracksInteractor.searchTracks( binding.inputEditText.text.toString(), consumer)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun toLibrary(item: Track) {
        val intent = Intent(this, LibraryActivity::class.java)
        intent.putExtra(CHECKED_TRACK, item)
        this.onPause()
        startActivity(intent)
    }

    companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
        const val SEARCH_REQUEST_DEF = ""
        const val TRACKS_LIST = "TRACKS_LIST"
        const val CHECKED_TRACK = "CHECKED_TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}