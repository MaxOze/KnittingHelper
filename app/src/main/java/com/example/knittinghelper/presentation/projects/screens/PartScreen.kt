package com.example.knittinghelper.presentation.projects

import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.PartViewModel
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectsViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: PartViewModel = hiltViewModel()
    viewModel.getPartInfo()
    when(val response = viewModel.getPartData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            if(response.data != null) {
                val part = response.data
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            scrollBehavior = scrollBehavior,
                            title = { Text("Часть " + part.name) },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                            )
                        )
                    },
                    bottomBar = {
                        BottomNavigationMenu(navController = navController)
                    }
                ) {

                }
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, response.message, Toast.LENGTH_SHORT).show()
        }
    }

}