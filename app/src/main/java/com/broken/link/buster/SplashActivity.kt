package com.broken.link.buster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.broken.link.buster.presentation.UI_element.GoogleButton
import com.broken.link.buster.presentation.vms.UserClientViewModel
import com.broken.link.buster.ui.theme.BrokenLinkBusterTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SplashActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("886930288472-cenrat278g32fpgm3bjp9kee3dd1egh1.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        enableEdgeToEdge()
        setContent {
            val userViewModel by viewModels<UserClientViewModel>()

            BrokenLinkBusterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    if (userViewModel.isLoadAccount) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Добро пожаловать в ${getString(R.string.app_name)}")

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(text = "Выберите один из возможных способов входа")

                                GoogleButton {
                                    signInWithGoogle()
                                }
                            }

                            Column(
                                modifier = Modifier.fillMaxSize(.9f),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton(onClick = { /*TODO*/ }) {
                                    Text(text = "Войти как гость", color = Color.Blue)
                                }
                            }
                        }

                    } else {
                        val infiniteTransition = rememberInfiniteTransition()
                        val progress by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 1f,
                            animationSpec = InfiniteRepeatableSpec(
                                animation = tween(400),
                                repeatMode = RepeatMode.Restart
                            ), label = ""
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Подождите, идет загрузка аккаунта")
                            CircularProgressIndicator(progress = { progress })
                        }
                    }

                }
            }
        }
    }


    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {

            Log.e("TAG", "signInLauncher: $e",)
            Log.e("TAG", "signInLauncher: ${e.message}",)
            Log.e("TAG", "signInLauncher: ${e.cause?.message}",)
            Log.e("TAG", "signInLauncher: ${e.cause?.cause}",)
            Log.e("TAG", "signInLauncher: ${e.cause?.stackTrace}",)
            Log.e("TAG", "signInLauncher: ${e.suppressed}",)
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.e("TAG", "firebaseAuthWithGoogle: success", )
                    // Обновите UI
                } else {
                    Log.e("TAG", "firebaseAuthWithGoogle: error", )
                    // Обработка ошибки
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }


    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}

