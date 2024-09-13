package com.broken.link.buster.presentation.UI_element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FolderItem(
    name: String,
    links: MutableList<String>,
    onToClick: (FolderInfoModel) -> Unit
) {

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Container(weight = 10f) {
                Row(
                    modifier = Modifier.fillMaxSize(.9f)
                ) {
                    Container(weight = 1f) {
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outline)
                            .size(35.dp))
                    }

                    Container(weight = 5f) {
                        Column(
                            modifier = Modifier.fillMaxSize(.9f),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = name, style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = links.joinToString(" , "),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Container(weight = 1f) {
                        IconButton(onClick = { onToClick(FolderInfoModel(name, links)) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }

            Container(weight = .5f) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }

}


data class FolderInfoModel(
    val name: String,
    val links: MutableList<String>
)