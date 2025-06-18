package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {
    private var searchRequest:String?=null
    
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



    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val trackSearchApiService = retrofit.create(TrackSearchApi::class.java)


    private var tracks : MutableList<Track> = mutableListOf()
    private lateinit var  sharedPrefs: SharedPreferences



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
        outState.putSerializable(TRACKS_LIST, tracks as java.util.ArrayList<*>)
   }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST, SEARCH_REQUEST_DEF)
        tracks = savedInstanceState.getSerializable(TRACKS_LIST) as ArrayList<Track>
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }

        sharedPrefs = getSharedPreferences(TRACKS_HISTORY_PREFERENCES, MODE_PRIVATE)
        var tracksHistory  = SearchHistory(sharedPrefs)


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

        deleteTracksHistoryButton.setOnClickListener {
            tracksHistory.clearHistory()
            historyRecycler.adapter?.notifyDataSetChanged()
            searchHistoryView.visibility=View.GONE
        }

        refrashButton.setOnClickListener {
            trackSearchApiService.searchTrack(inputEditText.text.toString())
                .enqueue(callback)
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
                   if (inputEditText.hasFocus() && tracksHistory.tracksInHistory.isNotEmpty())
                       searchHistoryView.visibility=View.VISIBLE
                   else searchHistoryView.visibility=View.GONE

               } else {
                   searchRequest = s.toString()
               }
               clearButton.visibility = clearButtonVisibility(s)

           }
           override fun afterTextChanged(s: Editable?) {
               // empty

           }
       }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                true
                if (inputEditText.text.isNotEmpty()) {
                    searchHistoryView.visibility=View.GONE
                    trackSearchApiService.searchTrack(inputEditText.text.toString())
                        .enqueue(callback)
                }
            }
            false
        }

        inputEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && tracksHistory.tracksInHistory.isNotEmpty())
                searchHistoryView.visibility=View.VISIBLE
            else searchHistoryView.visibility=View.GONE
        }

        recycler.adapter = TracksAdapter(tracks, object:OnItemClickListener{
            override fun onItemClick(position: Int) {
                tracksHistory.addTrackToHistory(tracks[position])
                historyRecycler.adapter?.notifyItemRemoved(tracksHistory.tracksInHistoryMaxLength-1)
                historyRecycler.adapter?.notifyItemInserted(0)

            }
        })
        recycler.layoutManager = LinearLayoutManager(this)

        historyRecycler.adapter = TracksAdapter(tracksHistory.getTracksFromHistory(), object:OnItemClickListener{
            override fun onItemClick(position: Int) {}
        })
        historyRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            return  View.GONE

        } else {
            return View.VISIBLE
        }
    }


    private val callback: Callback<TracksSearchResponse> = object : Callback<TracksSearchResponse>  {
        override fun onResponse(
            call: Call<TracksSearchResponse>,
            response: Response<TracksSearchResponse>
        ) {
            noInternetView.visibility = View.GONE
            trackNotFound.visibility=View.GONE
            // Получили ответ от сервера
            if (response.isSuccessful) {
                // Наш запрос был удачным, получаем ответ в JSON-тексте
                tracks.clear()
                var responseResults = response.body()?.results
                if (responseResults?.isNotEmpty() == true && responseResults!=null ) {
                    response.body()?.setFormatTarckTime()
                    tracks.addAll(responseResults)
                    recycler.adapter?.notifyDataSetChanged()

                }
                else {

                    // показываем, что ничего не найдено
                    trackNotFound.visibility=View.VISIBLE
                }

            } else {
                tracks.clear()
                recycler.adapter?.notifyDataSetChanged()
                noInternetView.visibility = View.VISIBLE
            }
        }

        override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
            // Не смогли соединиться с сервером

            tracks.clear()
            recycler.adapter?.notifyDataSetChanged()
            t.printStackTrace()
            noInternetView.visibility=View.VISIBLE
        }
    }



    companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
        const val SEARCH_REQUEST_DEF = ""
        const val TRACKS_HISTORY_PREFERENCES = "Tracks History Preferences"
        const val TRACKS_LIST = "TRACKS_LIST"
        const val TRACKS_LIST_DEF = ""
    }
}

//class TracksSearchRequest(val text: String) {}






