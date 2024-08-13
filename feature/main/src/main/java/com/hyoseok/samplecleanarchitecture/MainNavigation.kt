package com.hyoseok.samplecleanarchitecture

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyoseok.home.HomeScreen
import com.hyoseok.samplecleanarchitecture.camera.CameraScreen

sealed class  MainPath(val path : String){
    object Home : MainPath("HOME")
    object Camera : MainPath("CAMERA")
    object Gallery : MainPath("GALLERY")
}

@Composable
fun MainNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    val screenState by mainViewModel.screenState.collectAsStateWithLifecycle()

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
        composable(MainPath.Camera.path) {
            CameraScreen(
                backPress = {
                    navController.popBackStack()
                },
                showSnackBar = {

                },
                completePicture = {
                    //TODO PictureScreen 로 이동하면서 데이터 전달
                }
            )
        }
        composable(MainPath.Gallery.path) {

        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }