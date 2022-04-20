package dev.thielker.playground.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import dev.thielker.playground.android.ui.PlaygroundApp
import dev.thielker.playground.android.ui.theme.PlaygroundTheme
import timber.log.Timber

class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}

@ExperimentalMaterial3Api
class PlaygroundActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set to false so that the modal navigation drawer overlaps status and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            PlaygroundTheme {

                PlaygroundApp()
            }
        }
    }
}
