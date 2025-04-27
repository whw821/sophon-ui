package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(title: String, isHome: Boolean = false, onIconClick: () -> Unit) {
    val colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
    )
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxHeight(), Alignment.Center) {
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
        },
        colors = colors,
        modifier = Modifier.height(64.dp),
        navigationIcon = {
            if (isHome) {
                IconButton({}, modifier = Modifier.fillMaxHeight()) {
                    Icon(Icons.Rounded.Home, "主页", modifier = Modifier.size(24.dp))
                }
            } else {
                IconButton(onIconClick, modifier = Modifier.fillMaxHeight()) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        "返回",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}