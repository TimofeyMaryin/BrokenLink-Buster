package com.broken.link.buster

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.broken.link.buster.data.BrokenLinkModel
import com.broken.link.buster.data._const.SHARED_USER_STATUS_NAME
import com.broken.link.buster.data._const.USER_GOOGLE_LINK_TO_DATA
import com.broken.link.buster.data._const.USER_STATUS_SHARED_NAME
import com.broken.link.buster.data._const.UserStatusSignIn
import com.broken.link.buster.data._const.getUserStatusSignInToInt
import com.broken.link.buster.data._const.webClientId
import com.broken.link.buster.presentation.navigation.ApplicationNavigation
import com.broken.link.buster.ui.theme.BrokenLinkBusterTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.realm.kotlin.Realm
import okhttp3.internal.userAgent

class MainActivity : ComponentActivity() {

    private lateinit var realm: Realm
    private lateinit var pref: SharedPreferences

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(SHARED_USER_STATUS_NAME, Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firestore = FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        setContent {
            BrokenLinkBusterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->



                    ApplicationNavigation(this)


                    if (pref.getInt(USER_STATUS_SHARED_NAME, -1) == getUserStatusSignInToInt(UserStatusSignIn.DEVELOPER)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.error.copy(.01f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.fillMaxSize(.9f), contentAlignment = Alignment.BottomEnd) {
                                Text(text = "Вы зашли как разработчик")
                            }
                        }
                    }
                }
            }
        }
    }


    fun saveOfferForUser(
        model: BrokenLinkModel,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("user").document(userId)
            .update(USER_GOOGLE_LINK_TO_DATA, model)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }

    fun loadOfferForUser(
        onSuccess: (MutableList<BrokenLinkModel>) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        firestore.collection("user")
            .get()
            .addOnSuccessListener { result ->
                val offerList = mutableListOf<BrokenLinkModel>()

                if (result != null) {
                    for (document in result.documents) {
                        val link = document.toObject(BrokenLinkModel::class.java)
                        if (link != null) {
                            offerList.add(link)
                        }
                    }

                    // Используйте usersList для отображения данных или других операций
                    for (user in offerList) {
                        Log.d("Firestore", "link: ${user.link}, statusLink: ${user.statusLink}")
                    }
                }

                onSuccess(offerList)
            }
            .addOnFailureListener { e ->
                onError(e)
            }

    }


}

