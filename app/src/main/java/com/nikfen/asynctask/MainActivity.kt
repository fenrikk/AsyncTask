package com.nikfen.asynctask

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.nikfen.asynctask.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val liveData = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.textView.rootView)

        val listLiveData = mutableListOf<Int>();
        Thread {
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(1000)
                liveData.postValue(Random.nextInt(0, 100))
            }
        }.start()
        liveData.observe(this) {
            listLiveData.add(it)
            binding.textView.text = "Livedata ${listLiveData.last()}"
        }

        val listRxJava = mutableListOf<Long>()
        Observable
            .interval(0, 500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listRxJava.add(it)
                binding.textView2.text = "RxJava ${listRxJava.last()}"
            }, {
                it.printStackTrace()
            })

        val flow = MutableStateFlow(0)
        lifecycleScope.launch {
            coroutineScope {
                launch {
                    while (true) {
                        flow.value = (Random.nextInt(0, 100))
                        Log.d("ADA", "onCreate: dd")
                        delay(500)
                    }
                }
            }
            coroutineScope {
                launch {
                    flow.collect {
                        binding.textView3.text = "Coroutines $it"
                    }
                }
            }
        }
    }
}