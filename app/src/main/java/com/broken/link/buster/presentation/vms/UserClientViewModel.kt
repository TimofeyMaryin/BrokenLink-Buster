package com.broken.link.buster.presentation.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserClientViewModel(

): ViewModel() {
    var user: String? by mutableStateOf(null)
    var isLoadAccount by mutableStateOf(false)


    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            user = auth.currentUser?.email
            isLoadAccount = true
        }
    }
}