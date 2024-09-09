package com.broken.link.buster.presentation.fragment

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.broken.link.buster.SplashActivity
import com.broken.link.buster.data._const.SHARED_USER_STATUS_NAME
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.data._const.USER_STATUS_SHARED_NAME
import com.broken.link.buster.data._const.getUserStatusSignInToInt
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@Composable
fun SettingsFragment(
    activity: ComponentActivity,
    navController: NavController,
) {
    val pref = activity.getSharedPreferences(SHARED_USER_STATUS_NAME, Context.MODE_PRIVATE)
    FragmentContainer(navController = navController) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Здесь пока ничего нет! SettingsFragment")

            Button(onClick = {
                pref.edit().apply {
                    putInt(USER_STATUS_SHARED_NAME, getUserStatusSignInToInt(null))
                }.apply()

                Log.e(TAG, "SettingsFragment: user status = ${pref.getInt(USER_STATUS_SHARED_NAME, -2)}", )

                val i = Intent(activity, SplashActivity::class.java)
                activity.startActivity(i)
            }) {
                Text(text = "Выйти")
            }
        }
    }
}