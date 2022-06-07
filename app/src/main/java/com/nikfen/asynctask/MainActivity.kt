package com.nikfen.asynctask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.nikfen.asynctask.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val liveData = MutableLiveData<Int>()
    private var thread: Thread? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.textView.rootView)

        val listLiveData = mutableListOf<Int>();
        thread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(1000)
                liveData.postValue(Random.nextInt(0, 100))
            }
        }
        thread!!.start()
        liveData.observe(this) {
            listLiveData.add(it)
            binding.textView.text = "Livedata ${listLiveData.last()}"
        }

        compositeDisposable.add(Observable
            .interval(1000L, TimeUnit.MILLISECONDS)
            .map {
                Random.nextInt(0, 100)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.textView2.text = "RxJava $it"
            }, {
                it.printStackTrace()
            })
        )

        val flow = MutableStateFlow(0)
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                launch {
                    flow.value = (Random.nextInt(0, 100))
                    flow.collect {
                        binding.textView3.text = "Coroutines $it"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        thread!!.interrupt()
        thread = null
        compositeDisposable.dispose()
    }
}