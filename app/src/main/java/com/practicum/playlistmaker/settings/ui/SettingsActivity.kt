package com.practicum.playlistmaker.settings.ui
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, SettingsViewModel.getFactory())
            .get(SettingsViewModel::class.java)

        // Назад в главное меню
        binding.backToMainFromSettings.setOnClickListener {
            val butBackClickListener = Intent(this, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

        // Темная тема
        binding.themeSwitcher.setChecked((applicationContext as App).getDarkThemeFlag())
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        // Поделиться приложением в мессенджерах и т.п.
        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }
        // Написать в поддержку
        binding.textToSupport.setOnClickListener {
            viewModel.textToSupport()
        }
        // Пользовательское соглашение
        binding.userAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
}
