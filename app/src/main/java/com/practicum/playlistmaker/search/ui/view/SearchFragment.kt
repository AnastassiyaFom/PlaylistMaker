package com.practicum.playlistmaker.search.ui.view



import android.annotation.SuppressLint

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.search.ui.viewModel.TracksState
import com.practicum.playlistmaker.utils.debounce
import org.koin.android.ext.android.inject


@Suppress("DEPRECATION")
class SearchFragment : Fragment() {


    private val viewModel:SearchViewModel by inject()
    private lateinit var textWatcher  : TextWatcher
    private var tracks : MutableList<Track> = mutableListOf()
    private var history : MutableList<Track> = mutableListOf()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onHistoryClickDebounce: (Track) -> Unit


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var searchRequest:String=""

        var historyMaxLength = viewModel.getTracksInHistoryMaxLength()?:1


        onHistoryClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            toPlayer(track)
        }
        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            viewModel.chooseTrack(track)
            binding.tracksHistoryList.adapter?.notifyItemRemoved(historyMaxLength-1)
            binding.tracksHistoryList.adapter?.notifyItemInserted(0)
            toPlayer(track)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeHistory().observe(viewLifecycleOwner) {
            history.clear()
            history.addAll(it)
        }

        if (tracks.isNotEmpty()){

            showContent(tracks)
        }


        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.tracksHistoryList.adapter?.notifyDataSetChanged()
            binding.searchHistory.visibility=View.GONE
        }

        binding.refrashButton.setOnClickListener {

            viewModel.searchDebounce(searchRequest)
        }

        binding.clearSearch.setOnClickListener {
            binding.inputEditText.setText("")
            searchRequest=""
            tracks.clear()
            binding.tracksList.adapter?.notifyDataSetChanged()
            viewModel.destroy()
            binding.errorTrackNotFound.visibility = View.GONE
            binding.errorNoInternet.visibility = View.GONE

            requireActivity().currentFocus?.let { view:View ->
               val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
               imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
       }

        binding.inputEditText.setText(searchRequest)

        textWatcher =  object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               searchRequest = s?.toString() ?: ""
               if (searchRequest.isEmpty()) {
                   binding.clearSearch.visibility = View.GONE
                   showHistory()
                   if (binding.inputEditText.hasFocus() && !history.isEmpty())
                       binding.searchHistory.visibility=View.VISIBLE
                   else  binding.searchHistory.visibility=View.GONE
                   return
               } else {
                   binding.clearSearch.visibility = View.VISIBLE
                   viewModel.searchDebounce(searchRequest)
               }
           }
        }
        binding.inputEditText.addTextChangedListener(textWatcher)

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus &&  binding.inputEditText.text.isEmpty() && !history.isEmpty())
                showHistory()
            else  binding.searchHistory.visibility=View.GONE
        }

        binding.tracksList.adapter = TracksAdapter(tracks, object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Для предотвращения двойных нажатий на элемент
                onTrackClickDebounce(tracks[position])
            }
        })
        binding.tracksList.layoutManager = LinearLayoutManager(requireContext())

        binding.tracksHistoryList.adapter = TracksAdapter(history, object:
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                onHistoryClickDebounce(history[position])
            }
        })
        binding.tracksHistoryList.layoutManager = LinearLayoutManager(requireContext())
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
        tracks.clear()
        binding.tracksList.adapter?.notifyDataSetChanged()
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
        if (history.isNotEmpty()) {
            tracks.clear()
            binding.tracksList.adapter?.notifyDataSetChanged()
            binding.searchHistory.visibility = View.VISIBLE
        }
    }

    private fun toPlayer(item: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            bundleOf(PlayerFragment.ARGS_TRACK to item)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tracksList.adapter = null
        binding.tracksHistoryList.adapter = null
        textWatcher.let {  binding.inputEditText.removeTextChangedListener(it) }
        _binding=null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}