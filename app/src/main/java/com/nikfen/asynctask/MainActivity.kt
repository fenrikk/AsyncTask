package com.nikfen.asynctask

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikfen.asynctask.databinding.ActivityMainBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var thread: Thread? = null
    private val compositeDisposable = CompositeDisposable()
    private val listInt = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        val adapter = StringAdapter()
        binding.recycleView.layoutManager = linearLayoutManager
        binding.recycleView.adapter = adapter

        val liveData = MutableLiveData<List<Int>>()
        thread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(1000)
                listInt.add(Random.nextInt(0, 100))
                liveData.postValue(listInt)
            }
        }
        thread?.start()
        liveData.observe(this) {
            Log.d("app", "onCreate: $it")
            adapter.submitList(it)
        }

//        compositeDisposable.add(
//            Observable
//                .interval(1000L, TimeUnit.MILLISECONDS)
//                .map {
//                    Random.nextInt(0, 100)
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    listInt.add(it)
//                    adapter.submitList(listInt)
//                    binding.recycleView.adapter = adapter
//                }, {
//                    it.printStackTrace()
//                })
//        )
//
//        val flow = MutableStateFlow(0)
//        lifecycleScope.launch {
//            while (true) {
//                delay(1000)
//                launch {
//                    flow.value = (Random.nextInt(0, 100))
//                    flow.collect {
//                        listInt.add(it)
//                        Log.d("app", "onCreate: $listInt")
//                        adapter.submitList(listInt)
//                        binding.recycleView.adapter = adapter
//                    }
//                }
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