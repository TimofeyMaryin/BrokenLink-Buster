package com.broken.link.buster

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
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
import com.broken.link.buster.data._const.COLLECTION_FIELD_LINKS
import com.broken.link.buster.data._const.COLLECTION_FILED_GROUP
import com.broken.link.buster.data._const.COLLECTION_PATH
import com.broken.link.buster.data._const.SHARED_USER_STATUS_NAME
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.realm.kotlin.Realm

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

    val getUserId = { auth.currentUser?.uid }

    fun getUserLink(
        onSuccess: (List<String>) -> Unit,
        onError: () -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection(COLLECTION_PATH).document(userId)
            .get()
            .addOnSuccessListener {  documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val links = documentSnapshot.get(COLLECTION_FIELD_LINKS) as? List<String>

                    if (links != null) {
                        onSuccess(links)
                    } else {
                        onError()
                    }

                } else {
                    onError()
                }
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun removeLinkFromFirestore(
        link: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val userDocRef = firestore.collection(COLLECTION_PATH).document(userId)

        userDocRef
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val links = documentSnapshot.get(COLLECTION_FIELD_LINKS) as? MutableList<String>

                links?.let {
                    if (it.contains(link)) {
                        it.remove(link)

                        userDocRef
                            .update(COLLECTION_FIELD_LINKS, it)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onError("Cannot Update (1)") }
                    } else {
                        onError("link == null")
                    }
                }
            }
            .addOnFailureListener {
                onError("addOnFailureListener")
            }


    }


    fun removeAllUserLink(
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val userUI = auth.currentUser?.uid ?: return

        firestore.collection(COLLECTION_PATH).document(userUI)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }


    fun createNewDataBase(
        data: String,
        onSuccess: () -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection(COLLECTION_PATH)
            .document(userId)
            .set(hashMapOf(COLLECTION_FIELD_LINKS to listOf(data)))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {  }
    }


    fun saveLink(
        url: String,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        val data = hashMapOf(COLLECTION_FIELD_LINKS to listOf(url))


        firestore.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { querySnap ->
                if (querySnap.isEmpty) {
                    firestore.collection(COLLECTION_PATH)
                        .document(userId)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Success Save data", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed Save data", Toast.LENGTH_SHORT).show()
                            onError()
                        }
                } else {

                    firestore.collection(COLLECTION_PATH).document(userId)
                        .update(COLLECTION_FIELD_LINKS, FieldValue.arrayUnion(url))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Links success add", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Connit save link. Please try again.", Toast.LENGTH_SHORT).show()
                            onError()
                        }
                }
            }
            .addOnFailureListener {
                firestore.collection(COLLECTION_PATH)
                    .document(userId)
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Success Save data", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed Save data", Toast.LENGTH_SHORT).show()
                        onError()
                    }

            }

    }


    fun getUserFolder(
        onSuccess: (List<HashMap<String, List<String>>>) -> Unit,
        onError: (String) -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        val firestoreDoc = firestore.collection(COLLECTION_PATH).document(userId)

        firestoreDoc
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    onError("documentSnapshot does not exist")
                }

                val folders = documentSnapshot.get(COLLECTION_FILED_GROUP) as? List<HashMap<String, List<String>>> // 1 - создает пространство всех папок; 2 - создает саму папку, где 1 - это название папки, 2 - данные папки

                if (folders == null) {
                    onError("Folder does not exist")
                }

                onSuccess(folders!!)
            }
            .addOnFailureListener {
                onError("getUserFolder addOnFailureListener")
            }
    }

    fun createUserFolder(
        name: String,
        links: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        val firestoreDoc = firestore.collection(COLLECTION_PATH).document(userId)

        val data = hashMapOf(COLLECTION_FILED_GROUP to listOf(hashMapOf(name to links)))
        firestoreDoc
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError("Cannot create DB") }
    }



}

