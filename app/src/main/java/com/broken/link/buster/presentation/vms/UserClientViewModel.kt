package com.broken.link.buster.presentation.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.broken.link.buster.presentation.UI_element.FolderInfoModel

class UserClientViewModel(

): ViewModel() {
    var currentFolder by mutableStateOf<FolderInfoModel?>(null)
        private set

    var currentUserUrl by mutableStateOf("")

    fun setCurrentFolderValue(value: FolderInfoModel) { currentFolder = value }


}