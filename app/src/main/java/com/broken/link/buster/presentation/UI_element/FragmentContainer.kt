package com.broken.link.buster.presentation.UI_element

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.presentation.navigation.Screen

@Composable
fun FragmentContainer(
    navController: NavController,
    content: @Composable () -> Unit,
) {
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Container(weight = 10f) {
                content()
            }
            
            BottomBar(navController = navController)
        }
    }
    
}



@Composable
private fun ColumnScope.BottomBar(
    navController: NavController,
) {
    Container(weight = 1f) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            BottomBarButton(
                ic = Icons.Default.Search,
                status = navController.currentDestination?.route == Screen.SearchScreen.route
            ) {
                navController.navigate(Screen.SearchScreen.route)
            }
            
            
            BottomBarButton(
                ic = Icons.Default.DateRange,
                status = navController.currentDestination?.route == Screen.FolderScreen.route
            ) {
                navController.navigate(Screen.FolderScreen.route)
            }
            
            BottomBarButton(
                ic = Icons.Default.AccountCircle,
                status = navController.currentDestination?.route == Screen.SettingsScreen.route
            ) {
                navController.navigate(Screen.SettingsScreen.route)
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarButton(
    ic: ImageVector,
    status: Boolean,
    onNavigate: () -> Unit,
) {
    val contentColor by animateColorAsState(targetValue = if (status) MaterialTheme.colorScheme.primary else Color.Gray)
    
    Container(weight = 1f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { if (!status) onNavigate() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = ic, contentDescription = null, tint = contentColor, modifier = Modifier.size(30.dp))
        }
    }
    
}

