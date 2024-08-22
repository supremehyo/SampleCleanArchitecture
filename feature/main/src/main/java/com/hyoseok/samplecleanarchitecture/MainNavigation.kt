import com.hyoseok.samplecleanarchitecture.HandleBackButtonAction
import com.hyoseok.samplecleanarchitecture.MainViewModel
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyoseok.gallery.GalleryImageScreen
import com.hyoseok.home.HomeScreen
import com.hyoseok.samplecleanarchitecture.camera.CameraScreen
import com.hyoseok.samplecleanarchitecture.camera.PictureScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

sealed class  MainPath(val path : String){
    object Home : MainPath("HOME")
    object Camera : MainPath("CAMERA")
    object Gallery : MainPath("GALLERY")
    object PictureScreen : MainPath("PICTURE")
}

@Composable
fun MainNavigation(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
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
            GalleryImageScreen(
                ioDispatcher = ioDispatcher,
                coroutineScope = coroutineScope,
                onCancel = {
                    navController.popBackStack()
                }
            )
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