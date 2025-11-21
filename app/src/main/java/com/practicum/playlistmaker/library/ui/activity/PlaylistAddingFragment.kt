package com.practicum.playlistmaker.library.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddingPlaylistBinding
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.ui.viewModel.NewPlaylistViewModel
import org.koin.android.ext.android.inject


class PlaylistAddingFragment: Fragment() {
    private var _binding: FragmentAddingPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by inject()
    private var albumName=""
    private var albumDescription=""
    private var imageUri=""
    private var isButtonActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonInactive()
        // Возврат в предыдущую активити или фрагмент
        binding.backFromAddingPlaylist.setOnClickListener {
            returnWithDialog()
        }
        // Для ввода имени альбома
        binding.inputPlaylistName.setText("")
        var nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                albumName = s?.toString() ?: ""
                if (albumName.isNotEmpty()) {
                    setButtonActive()
                } else {
                    setButtonInactive()
                }
            }
        }
        binding.inputPlaylistName.addTextChangedListener(nameTextWatcher)

        // Для ввода описания альбома
        binding.inputPlaylistName.setText("")
        var descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                albumDescription = s?.toString() ?: ""
            }
        }
        binding.inputPlaylistName.addTextChangedListener(descriptionTextWatcher)

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.addedImage.setImageURI(uri)
                    imageUri= uri.toString()
                    binding.addedImage.setScaleType( CENTER_CROP)
                    viewModel.saveImageToPrivateStorage(uri, albumName)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на пространство addedImage запускаем photo picker
        binding.addedImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //  Создать альбом
        binding.buttonCreatePlaylist.setOnClickListener {
            if (isButtonActive) {
                viewModel.addTrackToBD(
                    Playlist(
                    playlstName = albumName,
                    playlistDescription = albumDescription ,
                    playlistImageDir =viewModel.getImageDir())
                )
                findNavController().navigateUp()
            } else {
                TODO("???")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
    @SuppressLint("ResourceAsColor")
    private fun setButtonInactive(){
        binding.buttonCreatePlaylist.setBackgroundColor(R.color.add_image_frame)
        isButtonActive = false
    }
    @SuppressLint("ResourceAsColor")
    private fun setButtonActive(){
        binding.buttonCreatePlaylist.setBackgroundColor(R.color.item_selected)
        isButtonActive = true
    }

    private fun returnWithDialog(){
        //TODO("Диалог на сохранение изменений")

        findNavController().navigateUp()

        //bundleOf(PlaylistAddingFragment.ARGS_PLAYLIST to item)
    }


}