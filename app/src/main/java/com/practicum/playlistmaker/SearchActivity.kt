package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private var searchRequest:String?=null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST, SEARCH_REQUEST_DEF)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }

        val backButton = findViewById<ImageView>(R.id.backToMainFromSearch)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        if (searchRequest!=null) {
            inputEditText.setText(searchRequest)
        }

        val clearButton = findViewById<ImageView>(R.id.clearSearch)

       backButton.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

       clearButton.setOnClickListener {
           inputEditText.setText("")
           this.currentFocus?.let { view ->
               val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
               imm?.hideSoftInputFromWindow(view.windowToken, 0)
           }
       }

       val simpleTextWatcher = object : TextWatcher {

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // empty
           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s.isNullOrEmpty()) {
                   clearButton.visibility = clearButtonVisibility(s)
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

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrEmpty()) {
            return  View.GONE
        } else {
            return View.VISIBLE
        }
    }


    companion object {
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
        const val SEARCH_REQUEST_DEF = ""
    }

}


