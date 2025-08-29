package com.practicum.playlistmaker.settings.ui
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.main.ui.MainActivity


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Назад в главное меню
        binding.backToMainFromSettings.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }
        var settingsInteractor: SettingsInteractor = provideSettingsInteractor(this)
        // Темная тема
        binding.themeSwitcher.setChecked((applicationContext as App).getDarkThemeFlag())
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        // Поделиться приложением в мессенджерах и т.п.
        binding.shareApp.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            val message: String = getResources().getString(R.string.share_message)
            shareAppIntent.setType("text/plain")
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareAppIntent, "Share with"))
        }
        // Написать в поддержку
        binding.textToSupport.setOnClickListener {
            val textToSupportIntent = Intent(Intent.ACTION_SENDTO)
            textToSupportIntent.data = Uri.parse("mailto:")
            textToSupportIntent.putExtra(Intent.EXTRA_EMAIL, settingsInteractor.getSupportMailAdress())
            textToSupportIntent.putExtra(Intent.EXTRA_TEXT, settingsInteractor.getSupportMessage() )
            textToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, settingsInteractor.getSupportMailSubject())
            startActivity(textToSupportIntent)
        }
        // Пользовательское соглашение
        binding.userAgreement.setOnClickListener {
            val openlink = Intent(Intent.ACTION_VIEW, Uri.parse(settingsInteractor.getUserAgreementLink()))
            startActivity(openlink)
        }
    }



}
