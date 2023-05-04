package com.example.rxvsflow.data

import kotlinx.coroutines.flow.Flow

interface FlowHelper {
    fun getAnimeAvailable(): Flow<List<String>>
}