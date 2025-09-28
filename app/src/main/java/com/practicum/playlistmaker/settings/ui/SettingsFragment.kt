package com.practicum.playlistmaker.settings.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    val viewModel:SettingsViewModel by inject()
    private var _binding: FragmentSettingsBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Темная тема
        binding.themeSwitcher.setChecked(viewModel.getDarkThemeFlag())

        binding.themeSwitcher.setOnCheckedChangeListener {
                switcher, checked -> viewModel.saveTheme(checked)
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
