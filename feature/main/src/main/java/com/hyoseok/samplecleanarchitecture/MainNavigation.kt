package com.hyoseok.samplecleanarchitecture

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyoseok.home.HomeScreen
import com.hyoseok.samplecleanarchitecture.camera.CameraScreen
import com.hyoseok.samplecleanarchitecture.camera.PictureScreen

sealed class  MainPath(val path : String){
    object Home : MainPath("HOME")
    object Camera : MainPath("CAMERA")
    object Gallery : MainPath("GALLERY")
    object PictureScreen : MainPath("PICTURE")
}

@Composable
fun MainNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    HandleBackButtonAction{
        Log.e("test" , "back")
    }

    NavHost(
        navController = navController,
        startDestination = MainPath.Home.path
    ) {
        composable(MainPath.Home.path) {
            HomeScreen{
                if(it == "CAMERA") {
                    navController.navigateSingleTopTo(MainPath.Camera.path)
                }
                else {
                    navController.navigateSingleTopTo(MainPath.Gallery.path)
                }
            }
        }
        composable(
            route = MainPath.Camera.path
        ) {
            CameraScreen(
                backPress = {
                    navController.popBackStack()
                },
                showSnackBar = {

                },
                completePictureUri = { savedUri->
                    val uriString = Uri.encode(savedUri)
                    navController.navigateSingleTopTo(
                        "${MainPath.PictureScreen.path}/$uriString"
                    )
                }
            )
        }
        composable(MainPath.Gallery.path) {

        }
        composable(
            route = "${MainPath.PictureScreen.path}/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) {
            backstackEntry ->
            val result = backstackEntry.arguments?.getString("data")
            result?.let {
                PictureScreen(
                    savedUri = it,
                    retry = {
                        navController.popBackStack()
                    },
                    complete = {
                        navController.popBackStack(MainPath.Home.path , false)
                    }
                )
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }