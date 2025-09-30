package com.sazon.digital.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.platform.LocalContext

@Composable
fun VideoPlayer(url: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = ExoPlayer.Builder(it).build().also { p ->
                    p.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                    p.prepare()
                    p.playWhenReady = false
                }
            }
        },
        onRelease = { view -> view.player?.release() }
    )
}
