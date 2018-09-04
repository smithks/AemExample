package com.keegansmith.aemexampleapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_aem_example.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AemExampleActivity : AppCompatActivity() {
    lateinit var aemService: AemService
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aem_example)
        recycler = aem_image_recycler
        recycler.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-90-131-68.compute-1.amazonaws.com:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        aemService = retrofit.create(AemService::class.java)
    }

    override fun onResume() {
        super.onResume()
        fetchAemInfo()
    }

    private fun fetchAemInfo() {
        Single.fromCallable { aemService.getResponse().execute() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dataFetched, this::onError);
    }

    private fun dataFetched(response: Response<List<AemEntry>>) {
        response.body()?.let {
            val adapter = AemImageAdapter(it)
            recycler.adapter = adapter
        }
    }

    private fun onError(throwable: Throwable) {
        Log.e(this.localClassName, throwable.message)
    }
}
