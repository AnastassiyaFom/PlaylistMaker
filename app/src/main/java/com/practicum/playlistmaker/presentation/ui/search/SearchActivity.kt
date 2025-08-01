package com.practicum.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator.getTrackHistoryRepository

import com.practicum.playlistmaker.Creator.provideTracksInteractor
import com.practicum.playlistmaker.R


// !!!!!!!!!!!!!!!!!!!!!! Не нужно ли здесь сделать не напрямую к интерфейсу взаимодействие, а через интерактор????
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository

import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.library.LibraryActivity
import com.practicum.playlistmaker.presentation.ui.main.MainActivity



@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {
    private var searchRequest:String?=null

    //Вспомагательные переменные для debouncer
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequestToNet() }

    // Интерактивные элементы экрана
    private lateinit var backButton : ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton : ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var noInternetView: ViewGroup
    private lateinit var refrashButton: Button
    private lateinit var trackNotFound: TextView
    private lateinit var deleteTracksHistoryButton: Button
    private lateinit var searchHistoryView: ViewGroup
    private lateinit var progressBarView: ProgressBar


    private var tracks : MutableList<Track> = mutableListOf()
    private lateinit var tracksHistory: TracksHistoryRepository


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
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }

        tracksHistory = getTrackHistoryRepository(this)

        backButton = findViewById<ImageView>(R.id.backToMainFromSearch)
        inputEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById<ImageView>(R.id.clearSearch)
        recycler = findViewById<RecyclerView>(R.id.tracksList)
        historyRecycler = findViewById<RecyclerView>(R.id.tracksHistoryList)
        noInternetView = findViewById<ViewGroup>(R.id.error_no_internet)
        refrashButton = findViewById<Button>(R.id.refrash_button)
        trackNotFound = findViewById<TextView>(R.id.error_track_not_found)
        deleteTracksHistoryButton = findViewById<Button>(R.id.clear_history)
        searchHistoryView= findViewById<ViewGroup>(R.id.search_history)
        progressBarView=findViewById<ProgressBar>(R.id.progressBar)

        deleteTracksHistoryButton.setOnClickListener {
            tracksHistory.clearHistory()
            historyRecycler.adapter?.notifyDataSetChanged()
            searchHistoryView.visibility=View.GONE
        }

        refrashButton.setOnClickListener {
            searchRequestToNet()
        }

       backButton.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

       clearButton.setOnClickListener {
           inputEditText.setText("")
           trackNotFound.visibility = View.GONE
           noInternetView.visibility = View.GONE
           tracks.clear()
           recycler.adapter?.notifyDataSetChanged()
           this.currentFocus?.let { view ->
               val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
               imm?.hideSoftInputFromWindow(view.windowToken, 0)
           }
       }

        if (searchRequest!=null) {
            inputEditText.setText(searchRequest)
        }

        val simpleTextWatcher = object : TextWatcher {

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // empty
           }
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s.isNullOrEmpty()) {
                   clearButton.visibility = clearButtonVisibility(s)
                   if (inputEditText.hasFocus() && tracksHistory.getTracksFromHistory().isNotEmpty())
                       searchHistoryView.visibility=View.VISIBLE
                   else searchHistoryView.visibility=View.GONE

               } else {
                   searchRequest = s.toString()
                   searchDebounce()
               }
               clearButton.visibility = clearButtonVisibility(s)
           }
           override fun afterTextChanged(s: Editable?) {
               // empty

           }
       }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && tracksHistory.getTracksFromHistory().isNotEmpty())
                searchHistoryView.visibility=View.VISIBLE
            else searchHistoryView.visibility=View.GONE
        }

        recycler.adapter = TracksAdapter(tracks, object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Для предотвращения двойных нажатий на элемент
                if (clickDebounce()){
                    tracksHistory.addTrackToHistory(tracks[position])
                    historyRecycler.adapter?.notifyItemRemoved(tracksHistory.getTracksInHistoryMaxLength()-1)
                    historyRecycler.adapter?.notifyItemInserted(0)
                    toLibrary(tracks[position])
                }
            }
        })
        recycler.layoutManager = LinearLayoutManager(this)

        historyRecycler.adapter = TracksAdapter(tracksHistory.getTracksFromHistory(), object:
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickDebounce()) {
                    toLibrary(tracksHistory.getTracksFromHistory()[position])
                }
            }
        })
        historyRecycler.layoutManager = LinearLayoutManager(this)
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
            handler.post { noInternetView.visibility = View.GONE}
            handler.post { trackNotFound.visibility = View.GONE}
            handler.post { progressBarView.visibility = View.GONE}
            // Получили ответ от сервера
            if (resultCode == 200 && foundTracks.isNotEmpty() == true && foundTracks != null) {
                // Наш запрос был удачным, получаем ответ в JSON-тексте
                tracks.clear()
                tracks.addAll(foundTracks)
                handler.post { recycler.adapter?.notifyDataSetChanged() }
            }
            else {
                tracks.clear()
                handler.post { recycler.adapter?.notifyDataSetChanged()}
                if (resultCode == 400) {
                    // показываем, что нет инета
                    handler.post { noInternetView.visibility = View.VISIBLE}
                } else {
                    // показываем, что ничего не найдено
                    handler.post { trackNotFound.visibility = View.VISIBLE}
                }
            }
        }
    }

    private fun searchRequestToNet(){
        if (inputEditText.text.isNotEmpty()) {
           searchHistoryView.visibility=View.GONE
            noInternetView.visibility = View.GONE
            trackNotFound.visibility = View.GONE
            progressBarView.visibility = View.VISIBLE
            val tracksInteractor:TracksInteractor = provideTracksInteractor()
            tracksInteractor.searchTracks(inputEditText.text.toString(), consumer)
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