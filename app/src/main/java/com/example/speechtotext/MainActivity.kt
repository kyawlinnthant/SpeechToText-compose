package com.example.speechtotext

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.speechtotext.ui.theme.SpeechToTextTheme
import com.google.accompanist.permissions.*
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeechToTextTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Greeting(name: String) {

    val permission = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (permission.status == PermissionStatus.Granted) {
            SpeechToTextView()
        } else {
            PermissionRequestView(permission = permission)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestView(
    permission: PermissionState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (permission.status.shouldShowRationale) {
            "မအေလိုးလေး အရင်တစ်ခါက လီးလို့ Allow One ပဲ ရွေးခဲ့တာလား"
        } else {
            "သုံးချင်ရင် permission လေးတော့ ပေးလိုက်"
        }

        Text(textToShow)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            permission.launchPermissionRequest()
        }) {
            Text("တောင်းလိုက်")
        }
    }
}

@Composable

fun SpeechToTextView() {

    val result = remember {
        mutableStateOf("Start Record!")
    }

    val context = LocalContext.current
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        this.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        this.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }
    speechRecognizer.setRecognitionListener(object : RecognitionListener {

        override fun onReadyForSpeech(bundle: Bundle) {

        }

        override fun onBeginningOfSpeech() {
            result.value = "စနားထောင်နေပြီဟေး..."
        }

        override fun onRmsChanged(v: Float) {

        }

        override fun onBufferReceived(bytes: ByteArray) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(i: Int) {

        }

        override fun onResults(bundle: Bundle) {

            val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val speechText = data?.first() ?: "ပြောင်းလို့မရဘူးဟေး"
            result.value = speechText
        }

        override fun onPartialResults(bundle: Bundle) {

        }

        override fun onEvent(i: Int, bundle: Bundle) {

        }
    })


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = result.value)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                speechRecognizer.startListening(speechIntent)
            }) {
                Text("ပြောလိုက်")
            }
            Button(onClick = {
                speechRecognizer.stopListening()
            }) {
                Text("ရပ်လိုက်")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeechToTextTheme {
        Greeting("Android")
    }
}
