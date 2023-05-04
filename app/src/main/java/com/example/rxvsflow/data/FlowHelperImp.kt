package com.example.rxvsflow.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlowHelperImp(private val coroutineServices: CoroutineServices) : FlowHelper {
    override fun getAnimeAvailable(): Flow<List<String>> = flow {
        emit(coroutineServices.getAnimeAvailable())
    }
}