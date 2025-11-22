package com.practicum.playlistmaker.library.ui.activity


import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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
    private var imageUri: Uri? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreatePlaylist.setEnabled(false)
        // Возврат в предыдущую активити или фрагмент
        binding.backFromAddingPlaylist.setOnClickListener {
            returnWithDialog()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                returnWithDialog()
            }
        })
        // Для ввода имени альбома
        binding.inputPlaylistName.setText("")
        var nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                albumName = s?.toString() ?: ""
                if (albumName.isNotEmpty()) {
                    binding.buttonCreatePlaylist.setEnabled(true)
                } else {
                    binding.buttonCreatePlaylist.setEnabled(false)
                }
            }
        }
        binding.inputPlaylistName.addTextChangedListener(nameTextWatcher)

        // Для ввода описания альбома
        binding.inputPlaylistDescription.setText("")
        var descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                albumDescription = s?.toString() ?: ""
            }
        }
        binding.inputPlaylistDescription.addTextChangedListener(descriptionTextWatcher)

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.addedImage.setImageURI(uri)
                    imageUri= uri
                    binding.addedImage.setScaleType( CENTER_CROP)

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
            if (binding.buttonCreatePlaylist.isEnabled() && albumName.isNotEmpty()) {
                savePlaylist()

                findNavController().navigateUp()

            } else {
                TODO("???")
            }
        }
    }

    private fun savePlaylist() {
        if (albumName.isNotEmpty()) {
            viewModel.addTrackToBD(
                Playlist(
                    playlstName = albumName,
                    playlistDescription = albumDescription,
                    playlistImageDir = imageUri
                )
            )
            if (imageUri!=null){
                viewModel.saveImageToPrivateStorage(imageUri!!, albumName)
            }

            binding.buttonCreatePlaylist.let {
                Snackbar.make(it, "Плейлист $albumName создан", Snackbar.LENGTH_LONG).show()
            }
        }
    }



    private fun   returnWithDialog()
    {
        if (albumName.isNotEmpty()|| albumDescription.isNotEmpty()||imageUri.toString().isNotEmpty()){
            showDialog()
        }

    }

    private fun   showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.creating_playlist_dialog_hadder))
            .setMessage(requireContext().getString(R.string.creating_playlist_dialog_message))
            .setNeutralButton(requireContext().getString(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(requireContext().getString(R.string.finish)) { dialog, which ->
                savePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

}