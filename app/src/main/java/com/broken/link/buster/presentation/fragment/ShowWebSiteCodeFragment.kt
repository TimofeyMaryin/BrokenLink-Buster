package com.broken.link.buster.presentation.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Composable
fun ShowWebSiteCodeFragment(
    navController: NavController,
    link: String,
) {


    var doc: Document? by remember { mutableStateOf(null) }
    var html: String? by remember { mutableStateOf(null) }

    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            try {
                doc = Jsoup.connect(link).get()
                html = doc!!.html()
            } catch (e: HttpStatusException) {
                isError = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize(.9f),
        ) {
            Text(
                text = link,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(100.dp))

            if (!isError) {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        html?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.surfaceBright,
                                lineHeight = 48.sp,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }
            }
        }


        if (isError) {
            Text(text = "Сайт не рабочий!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
        }
    }


}