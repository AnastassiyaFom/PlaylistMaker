package com.practicum.playlistmaker.player.ui.viewModel

import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val isButtonPlay : Boolean, val progress: String) {

    class Default : PlayerState(false, true, SimpleDateFormat("mm:ss").format(0))

    class Prepared : PlayerState(true, true, SimpleDateFormat("mm:ss").format(0))

    class Playing(progress: String) : PlayerState(true, false, progress)

    class Paused(progress: String) : PlayerState(true, true, progress)
}