package com.hyoseok.samplecleanarchitecture.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hyoseok.compose_camera.CameraXFactory
import com.hyoseok.compose_camera.RecordingInfo
import com.hyoseok.compose_camera.RecordingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(
    showSnackBar: (String) -> Unit,
    backPress : ()->Unit
) {
    val flashOn = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraX = remember { CameraXFactory.create() }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()
    val recordingState = cameraX.getRecordingState().collectAsState()
    val recordingInfo = cameraX.getRecordingInfo().collectAsState(RecordingInfo(0,0,0.0))
    val cameraInitialized = remember { mutableStateOf(false) }

    val cameraPermission = Manifest.permission.CAMERA
    val permissionStatus = remember {
        ContextCompat.checkSelfPermission(context, cameraPermission)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraX.initialize(context = context)
            previewView.value = cameraX.getPreviewView()
            cameraInitialized.value = true
        } else {
            showSnackBar("Camera permission is required to use this feature.")
        }
    }

    BackHandler{
        backPress.invoke()
    }

    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        LaunchedEffect(Unit) {
            if (!cameraInitialized.value) {
                cameraX.initialize(context = context)
                previewView.value = cameraX.getPreviewView()
                cameraInitialized.value = true
            }
        }
    } else {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(cameraPermission)
        }
    }

    if (cameraInitialized.value) {
        DisposableEffect(facing.value) {
            cameraScope.launch(Dispatchers.Main) {
                cameraX.startCamera(lifecycleOwner = lifecycleOwner)
            }
            onDispose {
                cameraX.unBindCamera()
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        previewView.value?.let { preview -> AndroidView(modifier = Modifier.fillMaxSize(), factory = { preview }) {} }
        Row(
            Modifier
                .height(100.dp)
                .align(Alignment.TopStart)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                Text("${(recordingInfo.value.duration / 1000000000.0)}", fontSize = 18.sp, color = Color.White)
            }
            Row(
                Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.Bottom) {
                repeat(25){
                    Box(modifier = Modifier
                        .size(4.dp, (recordingInfo.value.audioAmplitude * it * 4).dp)
                        .background(Color.Black))
                }
                repeat(25){
                    Box(modifier = Modifier
                        .size(4.dp, ((recordingInfo.value.audioAmplitude * (100 - it * 4))).dp)
                        .background(Color.Black))
                }
            }
        }
        Column(Modifier.align(Alignment.BottomCenter)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                when(recordingState.value){
                    is RecordingState.OnRecord -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.closeRecordVideo()
                            }
                        ) {
                            Text("close")
                        }
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.stopRecordVideo()
                            }
                        ) {
                            Text("stop")
                        }
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.pauseRecordVideo()
                            }
                        ) {
                            Text("pause")
                        }
                    }
                    is RecordingState.Idle -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.startRecordVideo()
                            }
                        ) {
                            Text("record")
                        }
                    }
                    is RecordingState.Paused -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.resumeRecordVideo()
                            }
                        ) {
                            Text("resume")
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        cameraX.turnOnOffFlash()
                    }
                ) {
                    Text(if (flashOn.value) "Turn OFF" else "Turn ON")
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        cameraX.flipCameraFacing()
                    }
                ) {
                    Text(if (facing.value == CameraSelector.LENS_FACING_FRONT) "back" else "front")
                }

                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        cameraX.takePicture{ result->
                            //TODO 촬영을 확정할지 확인하는 화면으로 이동
                            
                        }
                    }
                ) {
                    Text("takePicture")
                }
            }
        }

    }
}