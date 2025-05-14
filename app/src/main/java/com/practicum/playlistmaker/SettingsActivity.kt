package com.practicum.playlistmaker
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        // Назад в главное меню
        val butBack = findViewById<ImageView>(R.id.backToMainFromSettings)
        butBack.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

        // Поделиться приложением в мессенджерах и т.п.
        val shareApp = findViewById<TextView>(R.id.shareApp)
        shareApp.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            val message: String = getResources().getString(R.string.share_message)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareAppIntent, "Share with"))
        }

        // Написать в поддержку
        val textToSupport = findViewById<TextView>(R.id.textToSupport)
        textToSupport.setOnClickListener {
            val textToSupportIntent = Intent(Intent.ACTION_SENDTO)
            val message: String = getResources().getString(R.string.support_message)
            val subject: String = getResources().getString(R.string.support_mail_subject)
            textToSupportIntent.data = Uri.parse("mailto:")
            textToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getResources().getString(R.string.support_mail_adress)))
            textToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            textToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(textToSupportIntent)
        }

        // Пользовательское соглашение
        val userAgreement = findViewById<TextView>(R.id.userAgreement)
        userAgreement.setOnClickListener {
            val address: Uri = Uri.parse(getResources().getString(R.string.user_agreement_link))
            val openlink = Intent(Intent.ACTION_VIEW, address)
            startActivity(openlink)
        }
    //////////////////////////////////////////////////////////////////
    /*Реализовать кнопку «Пользовательское соглашение»:
Если пользователь находится на экране «Настройки», то при нажатии на кнопку
«Пользовательское соглашение» должен открыться браузер, использующийся
на устройстве пользователя по умолчанию. В качестве ссылки
 на веб-страницу используйте ссылку на оферту на оказание образовательных услуг Яндекс Практикума.*/
    }

}