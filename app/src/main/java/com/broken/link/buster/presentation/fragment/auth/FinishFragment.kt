package com.broken.link.buster.presentation.fragment.auth

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.presentation.navigation.AuthentificationGuestScreen

@Composable
fun FinishFragment(
    activity: ComponentActivity,
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            val i = Intent(activity.applicationContext, MainActivity::class.java)
            i.apply {
                putExtra("guest", true)
                putExtra("name", activity.intent.getStringExtra("name"))
                putExtra("about", activity.intent.getStringExtra("about"))
            }
        }) {
            Text(text = "Завершить")
        }
    }

}