package com.practicum.playlistmaker.library.ui.activity.playlists


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistAddingViewModel
import org.koin.android.ext.android.inject


class PlaylistAddingFragment: Fragment() {
    private var _binding: FragmentAddingPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistAddingViewModel by inject()
    private var previousFragment = ADDING_FRAGMENT
    private var playlistId = -1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddingPlaylistBinding.inflate(inflater, container, false)
        previousFragment = requireArguments().getString(PREVIOUS_FRAGMENT)?:ADDING_FRAGMENT
        playlistId = requireArguments().getInt(PLAYLIST_ID)?:-1
        viewModel.setPlaylistData(previousFragment,playlistId)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (previousFragment) {
            PLAYLIST_SCREEN_FRAGMENT->{
                val uri = viewModel.getImageUri()
                if (uri!=null && uri.toString().isNotEmpty()) setImage(uri)
                else binding.addedImage.setImageDrawable(viewModel.getImageDrawable())
            }
        }
        binding.tittle.setText(viewModel.getFragmentHadder())
        binding.buttonCreatePlaylist.setText(viewModel.getSaveButtonText())
        binding.buttonCreatePlaylist.setEnabled(false)
        // Возврат в предыдущую активити или фрагмент
        binding.backFromAddingPlaylist.setOnClickListener {
            back()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               back()
            }
        })
        // Для ввода имени альбома
        binding.inputPlaylistName.setText(viewModel.getAlbumName())
        if (viewModel.getAlbumName().isNotEmpty()) {
            binding.buttonCreatePlaylist.setEnabled(true)
        } else {
            binding.buttonCreatePlaylist.setEnabled(false)
        }
        var nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val albumName = s?.toString() ?: ""
                viewModel.setAlbumName(albumName)
               if (albumName.isNotEmpty()) {
                    binding.buttonCreatePlaylist.setEnabled(true)
                } else {
                    binding.buttonCreatePlaylist.setEnabled(false)
                }

            }
        }
        binding.inputPlaylistName.addTextChangedListener(nameTextWatcher)

        // Для ввода описания альбома
        binding.inputPlaylistDescription.setText(viewModel.getAlbumDescription())
        var descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val albumDescription = s?.toString() ?: ""
                viewModel.setAlbumDescription(albumDescription)
            }
        }
        binding.inputPlaylistDescription.addTextChangedListener(descriptionTextWatcher)

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                setImage(uri)
            }


        binding.addedImage.setBackgroundDrawable(requireContext().getDrawable(R.drawable.frame))
        //по нажатию на пространство addedImage запускаем photo picker
        binding.addedImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //  Создать альбом
        binding.buttonCreatePlaylist.setOnClickListener {
            if (binding.buttonCreatePlaylist.isEnabled() && viewModel.getAlbumName().isNotEmpty()) {
                savePlaylist()
                findNavController().navigateUp()
            }
        }
    }

    private fun setImage(uri:Uri?){
        if (uri != null) {
            binding.addedImage.setImageURI(uri)
            viewModel.setImageUri(uri)
            binding.addedImage.setScaleType( CENTER_CROP)
        }
    }

    private fun savePlaylist() {
        val albumName = viewModel.getAlbumName()
        if (albumName.isNotEmpty()) {
            viewModel.savePlaylist()
            binding.buttonCreatePlaylist.let {
                binding.buttonCreatePlaylist.let {
                    Snackbar.make(it, viewModel.getSavePlaylistMessage(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

private fun back() {
    when (previousFragment) {
        PLAYLIST_SCREEN_FRAGMENT -> findNavController().navigateUp()
        else -> returnWithDialog()
    }
}
    private fun   returnWithDialog() {
        if (viewModel.hasData()){
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
                findNavController().navigateUp()
            }
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object{
        const val PLAYLIST_ID = "PlaylistId"
        const val PREVIOUS_FRAGMENT = "previousFragment"
        const val ADDING_FRAGMENT="playlistAddingFragment"
        const val PLAYLIST_SCREEN_FRAGMENT="playlistScreenFragment"
        const val PLAYLISTS_FRAGMENT="paylistsFragment"
        const val PLAYER_FRAGMENT="payerFragment"
    }
}