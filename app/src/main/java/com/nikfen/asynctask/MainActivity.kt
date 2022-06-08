package com.nikfen.asynctask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikfen.asynctask.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var thread: Thread? = null
    private val compositeDisposable = CompositeDisposable()
    private var listInt = listOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        val adapter = StringAdapter()
        binding.recycleView.layoutManager = linearLayoutManager
        binding.recycleView.adapter = adapter

//        val liveData = MutableLiveData<List<Int>>()
//        thread = Thread {
//            while (!Thread.currentThread().isInterrupted) {
//                Thread.sleep(1000)
//                listInt = listInt + Random.nextInt(0, 100)
//                liveData.postValue(listInt)
//            }
//        }
//        thread?.start()
//        liveData.observe(this) {
//            adapter.submitList(it)
//        }
//
        compositeDisposable.add(
            Observable
                .interval(1000L, TimeUnit.MILLISECONDS)
                .map {
                    Random.nextInt(0, 100)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listInt = listInt + it
                    adapter.submitList(listInt)
                }, {
                    it.printStackTrace()
                })
        )

//        val flow = MutableStateFlow(0)
//        lifecycleScope.launch {
//            while (true) {
//                flow.value = (Random.nextInt(0, 100))
//                delay(1000)
//            }
//        }
//        lifecycleScope.launch {
//            flow.collect {
//                listInt = listInt + it
//                Log.d("app", "onCreate: $listInt")
//                adapter.submitList(listInt)
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        thread?.interrupt()
        thread = null
        compositeDisposable.dispose()
    }
}