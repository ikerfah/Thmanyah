package com.ikerfah.thmanyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ikerfah.thmanyah.ui.navigation.AppNavigation
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThmanyahTheme {
                AppNavigation()
            }
        }
    }
}
