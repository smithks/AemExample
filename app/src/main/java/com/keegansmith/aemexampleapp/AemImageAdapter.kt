package com.keegansmith.aemexampleapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


class AemImageAdapter(private var aemEntries: List<AemEntry>) : RecyclerView.Adapter<AemImageAdapter.ImageViewHolder>() {
    
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.aem_image_cell, p0, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return aemEntries.size
    }

    override fun onBindViewHolder(p0: ImageViewHolder, p1: Int) {
        p0.setContent(aemEntries[p1])
    }

    class ImageViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var client: OkHttpClient? = null

        init {
            client = OkHttpClient.Builder()
                    .addInterceptor(object : Interceptor {
                        @Throws(IOException::class)
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "Base YWRtaW46YWRtaW4=")
                                    .build()
                            return chain.proceed(newRequest)
                        }
                    })
                    .build()

        }

        fun setContent(aemEntry: AemEntry) {
            val textView = view.findViewById<TextView>(R.id.entry_title)
            textView.text = aemEntry.Title

            val imageView = view.findViewById<ImageView>(R.id.aem_image)
            val picasso = Picasso.Builder(view.context)
                    .downloader(OkHttp3Downloader(client))
                    .build()

            picasso.load(aemEntry.Thumbnail)
                    .error(R.color.colorPrimary)
                    .into(imageView)
        }
    }
}