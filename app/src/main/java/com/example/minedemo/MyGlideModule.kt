package com.example.minedemo

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client: OkHttpClient = OkHttpClient.Builder()
                .connectionPool(ConnectionPool(8, 5, TimeUnit.MINUTES))
                .build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        val memCalculatorBuilder = MemorySizeCalculator.Builder(context)
        val calculator = memCalculatorBuilder.setMemoryCacheScreens(2f).build()
        val bitmapPoolCalculator = memCalculatorBuilder.setBitmapPoolScreens(3f).build()
        val memSize = calculator.memoryCacheSize.toLong()
        val bitmapPoolSize = bitmapPoolCalculator.bitmapPoolSize.toLong()

        builder.setMemoryCache(LruResourceCache(memSize))
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context))
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize))

        Log.d("MyGlideModule", "-----> memSize:${memSize}  bitmapPoolSize:${bitmapPoolSize}")
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}