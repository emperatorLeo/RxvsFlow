package com.example.rxvsflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxvsflow.ui.theme.RxVsFlowTheme
import com.example.rxvsflow.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RxVsFlowTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ButtonsToGetData("Android", viewModel = viewModel)
                }
            }
        }
      viewModel.getFlowAnimeAvailable()
    }
}

@Composable
fun ButtonsToGetData(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Button(onClick={
     viewModel.getRxAnimeAvailable()
    }){
        Text(text = "Get anime with RX")
    }

    Button(onClick={
        viewModel.getFlowAnimeAvailable()
    },modifier.padding(top = 50.dp)){
        Text(text = "Get anime with Flow")
    }
}


