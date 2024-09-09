package com.broken.link.buster

import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.broken.link.buster.data._const.SHARED_USER_STATUS_NAME
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.data._const.USER_STATUS_SHARED_NAME
import com.broken.link.buster.data._const.UserStatusSignIn
import com.broken.link.buster.data._const.getUserStatusSignInToInt
import com.broken.link.buster.data._const.webClientId
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
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var isErrorPopUp by mutableStateOf(false)

    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        pref = getSharedPreferences(SHARED_USER_STATUS_NAME, MODE_PRIVATE)
        prefEdit = pref.edit()


        enableEdgeToEdge()
        setContent {

            LaunchedEffect(key1 = Unit) {
                Log.e(TAG, "onCreate: user is register ${pref.getInt(USER_STATUS_SHARED_NAME, -1)}", )
                if (
                    pref.getInt(USER_STATUS_SHARED_NAME, -1) == getUserStatusSignInToInt(UserStatusSignIn.GUEST) ||
                    pref.getInt(USER_STATUS_SHARED_NAME, -1) == getUserStatusSignInToInt(UserStatusSignIn.GOOGLE) ||
                    pref.getInt(USER_STATUS_SHARED_NAME, -1) == getUserStatusSignInToInt(UserStatusSignIn.DEVELOPER)
                ) {
                    Log.e(TAG, "onCreate: user is register ${pref.getInt(USER_STATUS_SHARED_NAME, -1)}", )
                    val i = Intent(applicationContext, MainActivity::class.java)
                    startActivity(i)
                }

                if (pref.getInt(USER_STATUS_SHARED_NAME, -1) == getUserStatusSignInToInt(UserStatusSignIn.GUEST_NO_FILLING)) {
                    Log.e(TAG, "onCreate: user register as guest but does not compleated fill")
                    val i = Intent(applicationContext, AuthentificationGuestActivity::class.java)
                    startActivity(i)
                }

            }


            BrokenLinkBusterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

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
                            TextButton(onClick = {
                                prefEdit.putInt(USER_STATUS_SHARED_NAME, getUserStatusSignInToInt(UserStatusSignIn.GUEST_NO_FILLING))
                                prefEdit.apply()

                                Log.e(TAG, "onCreate: pref.getInt(USER_STATUS_SHARED_NAME, -1) = ${pref.getInt(USER_STATUS_SHARED_NAME, -1)}", )


                                val i = Intent(applicationContext, AuthentificationGuestActivity::class.java)
                                startActivity(i)

                            }) {
                                Text(text = "Войти как гость", color = Color.Blue)
                            }

                            TextButton(onClick = {
                                prefEdit.putInt(USER_STATUS_SHARED_NAME, getUserStatusSignInToInt(UserStatusSignIn.DEVELOPER)).apply()
                                val i = Intent(applicationContext, MainActivity::class.java)
                                startActivity(i)
                            }) {
                                Text(text = "Войти разработчик", color = Color.Blue)
                            }
                        }
                    }

                    if (isErrorPopUp) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(.4f))
                                .clickable { isErrorPopUp = false },
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth(.9f)
                                    .fillMaxHeight(.7f)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "Ошибка регистрации Google аккаунта",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.titleLarge,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = "Проверьте интернет соединение и попробуйте позже",
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                }
                            }
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
            isErrorPopUp = true
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    prefEdit.putInt(USER_STATUS_SHARED_NAME, getUserStatusSignInToInt(UserStatusSignIn.GOOGLE)).apply()
                    val user = auth.currentUser
                    Log.e(TAG, "firebaseAuthWithGoogle: success ${user?.email}", )
                    Log.e(TAG, "firebaseAuthWithGoogle: ${pref.getInt(USER_STATUS_SHARED_NAME, -1)}", )
                    val navIntent = Intent(this, MainActivity::class.java)
                    startActivity(navIntent)
                } else {
                    Log.e("TAG", "firebaseAuthWithGoogle: error", )
                    isErrorPopUp = true
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

