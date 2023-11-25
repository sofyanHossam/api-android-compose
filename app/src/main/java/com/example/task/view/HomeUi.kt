package com.example.task.view


import android.annotation.SuppressLint
import android.app.Activity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.task.R
import com.example.task.model.AlbumsModel
import com.example.task.model.PhotoModel
import com.example.task.model.myUserData

//album item
@Composable
fun AlbumItem(album: AlbumsModel,onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .clickable { onItemClick.invoke() }
            .height(110.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Surface() {

            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                ) {
                    Text(
                        text = album.title,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }

}

//first screen shows user info and his album
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(navController: NavHostController, user: myUserData?, albums: List<AlbumsModel>, userId: Int?) {
    Scaffold(
        topBar = {
            // You can customize the top bar as needed
            // For example, you can add a back button here
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display user information
            Text(text = " ${user?.name}", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = " ${user?.address}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            // Display the list of albums using LazyColumn
            // Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                itemsIndexed(items = albums){index, item ->
                    if (item.userId==userId){
                        AlbumItem(album = item){
                            //changed
                            navController.navigate("screenFun2/${item.id}/${item.title}")
                        }
                    }

                }
            }
        }
    }
}

//on click at any item it navigate with album data to all photos in album with a search bar
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SecondScreen(
    navController: NavHostController,
    photo: List<PhotoModel>,
    albumId: Int,
    title: String
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Bold text view for album name
                Text(
                    text = "${title}",
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Edit text view for search
                // Edit text view for search
                OutlinedTextField(
                    value = searchQuery, // Add your own state for the search text
                    onValueChange = {searchQuery=it /* Handle search text change */ },
                    label = { Text("Search") },
                    leadingIcon = {
                        // You can use any icon you want for the search
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            // You can use any icon you want for clearing the search
                            IconButton(onClick = {
                                // Clear the search text
                                /* Handle clear action */
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Handle search action
                            println("done")
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )


                val filterPhotos =photo.filter { it.albumId==albumId&&
                        it.title.contains(searchQuery, true)
                }
                // Image grid
                ImageGrid(
                    imageList = filterPhotos.map { it.thumbnailUrl },
                    onItemClick = { imageUrl ->
                        // Navigate to ImageViewer when an image is clicked
                    }
                )
            }
        }
    )
}
//instagram image grid photos hold and control images
@ExperimentalFoundationApi
@Composable
fun ImageGrid(imageList: List<String>, onItemClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {
        items(imageList) { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { }
                    .padding(4.dp)
                    .background(MaterialTheme.colors.primary)
            )
        }
    }
}

//navigation control
@Composable
fun AppNavigation(
    randomUser: myUserData?,
    albumsViewModel: List<AlbumsModel>,
    userId: Int?,
    photosViewModel: List<PhotoModel>,
) {
    // Create a NavController
    val navController = rememberNavController()

    // Set up navigation with NavHost
    NavHost(
        navController = navController,
        startDestination = "screenFun1",
    ) {
        composable("screenFun1") {
            UserScreen(navController, user = randomUser, albums = albumsViewModel, userId = userId)
        }
        composable(
            "screenFun2/{id}/{title}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            SecondScreen(navController, photo = photosViewModel, id.toInt(), title)
        }
    }
}


@Composable
fun ProgressDialog(isVisible: Boolean) {
    if (isVisible) {
        Dialog(
            onDismissRequest = { /* Dismiss the dialog if needed */ },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Center)
                )
            }
        }
    }
}
