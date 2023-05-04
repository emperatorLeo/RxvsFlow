package com.example.rxvsflow.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rxvsflow.data.ApiClient
import com.example.rxvsflow.data.FlowHelperImp
import com.example.rxvsflow.data.RxServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
class MainViewModel : ViewModel() {
    private val rxClient: RxServices = ApiClient().provideRxClient()
    private val flowHelper = FlowHelperImp(ApiClient().provideFlowClient())
    private val disposable = CompositeDisposable()

    fun getRxAnimeAvailable() {
        disposable.add(rxClient.getAnimeAvailable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { list ->
                list.takeLast(20).forEach {
                    Log.d("Leo", it)
                }
            }, onComplete = { Log.d("RxvsFlow", "those were the available anime...") },
                onError = { t -> Log.d("RxvsFlow", "there was an error: ${t.message}") }))
    }

    fun behaviorSubject() {
        val behaviorSubject = BehaviorSubject.create<String>()
        behaviorSubject.onNext("zero")
        val subscription1 = behaviorSubject.subscribe { Log.d("subs1",it) }
        behaviorSubject.onNext("one")
        val subscription2 = behaviorSubject.subscribe { Log.d("subs2",it) }
        behaviorSubject.onNext("two")

        disposable.addAll(subscription1,subscription2)
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    fun broadcastChannel(){
         val requestChannel = ConflatedBroadcastChannel<String>()
        val mutableStateFlow = MutableStateFlow("empty")
        val consumer1 = mutableStateFlow.asStateFlow()

        val broadcastChannel = BroadcastChannel<String>(Channel.BUFFERED)
        val receiver1 : ReceiveChannel<String> = requestChannel.openSubscription()
        val receiver2 = requestChannel.openSubscription()


        viewModelScope.launch {
            requestChannel.send("Zero")
            requestChannel.asFlow().collect{
                Log.d("Flow subs1", it)
            }
            requestChannel.send("One")
            requestChannel.send("Two")

            /*
            val consumer2 = mutableStateFlow.asStateFlow()
            mutableStateFlow.value = "Zero"
            mutableStateFlow.value = "One"
            consumer1.collect {  }*/

           /* requestChannel.send("Zero")
            receiver1.consumeAsFlow().collect { Log.d("Flow subs1", it) }
            requestChannel.send("One")
            receiver2.consumeAsFlow().collect { Log.d("Flow subs2", it)}
            requestChannel.send("Two")*/
        }

    }

    fun getFlowAnimeAvailable() {
        viewModelScope.launch {
            flowHelper.getAnimeAvailable()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.d("RxvsFlow", "ERROR: ${e.message}")
                }
                .collect {
                    it.takeLast(20).forEach {
                        Log.d("RxvsFlow", it)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}