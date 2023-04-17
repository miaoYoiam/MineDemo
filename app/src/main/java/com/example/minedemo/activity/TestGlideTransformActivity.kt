package com.example.minedemo.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.minedemo.GlideApp
import com.example.minedemo.R
import com.example.minedemo.utils.DisplayUtil
import kotlinx.android.synthetic.main.activity_text_glide_transform.*


class TestGlideTransformActivity : AppCompatActivity() {

    val imgs = listOf(
        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/%E4%B9%9D%E5%AF%A8%E6%BA%9D-%E4%BA%94%E8%8A%B1%E6%B5%B7.jpg/2560px-%E4%B9%9D%E5%AF%A8%E6%BA%9D-%E4%BA%94%E8%8A%B1%E6%B5%B7.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Mirror_Lake_Jiuzhaigou.jpg/2560px-Mirror_Lake_Jiuzhaigou.jpg",
        "http://img.netbian.com/file/2021/1027/e6496d85ea96e0ea6b1183d51535ac0c.jpg",
        "http://img.netbian.com/file/2021/1025/9e8c4ee0cdcafa7b1a6af4adf288b4a6.jpg",
        "http://img.netbian.com/file/2021/1023/b42e7a634918332853f2762ba60081f2.jpg",
        "http://img.netbian.com/file/2021/1023/f2b6069ff0433b17945429e33bb8ec5d.jpg",
        "http://img.netbian.com/file/2021/1022/659d5fed9a374f13b15ed8abff61cc20.jpg",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.vjshi.com%2F2018-10-18%2Fe86efff18b0a8788be4d60fa9ccaf153%2F00003.jpg%3Fx-oss-process%3Dstyle%2Fwatermark&refer=http%3A%2F%2Fpic.vjshi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638600352&t=dbba687b993e7bf959219bb6be119248",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.vjshi.com%2F2017-08-26%2Fcdd746428baf0ed5e71206783e58b815%2F00002.jpg%3Fx-oss-process%3Dstyle%2Fwatermark&refer=http%3A%2F%2Fpic.vjshi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638600352&t=f323dc876394c26e5cfc7ffcb5692bd1",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.vjshi.com%2F2017-06-14%2Fb72107b4012fdc18ffead2f1951807a5%2F00003.jpg%3Fx-oss-process%3Dstyle%2Fwatermark&refer=http%3A%2F%2Fpic.vjshi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638600352&t=035f15596f800119aa12965959812359"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_glide_transform)

        testLoadGif()
    }

    private fun testLoadGif(){
    }

    private fun testLoadBitmapPool(){
        GlideApp.with(this)
            .asBitmap()
            .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Ftupian.qqjay.com%2Fu%2F2018%2F0222%2F2_163119_13.jpg&refer=http%3A%2F%2Ftupian.qqjay.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1639217719&t=c4e45f94a394c7db2193ed4229129c1c")
            .downsample(DownsampleStrategy.CENTER_OUTSIDE)
            .apply(RequestOptions().apply {
                transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
            })
            .into(test_target)


        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(100f).toInt(), DisplayUtil.dp2px(100f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(200f).toInt(), DisplayUtil.dp2px(200f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(300f).toInt(), DisplayUtil.dp2px(300f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(400f).toInt(), DisplayUtil.dp2px(400f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(500f).toInt(), DisplayUtil.dp2px(500f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(600f).toInt(), DisplayUtil.dp2px(600f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(700f).toInt(), DisplayUtil.dp2px(700f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(800f).toInt(), DisplayUtil.dp2px(800f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        for (i in imgs.indices) {
            val url = imgs[i]

            Glide.with(this)
                .asBitmap()
                .load(url)
                .override(DisplayUtil.dp2px(900f).toInt(), DisplayUtil.dp2px(900f).toInt())
                .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                .apply(RequestOptions().apply {
                    transform(MultiTransformation(CenterCrop(), RoundedCorners(DisplayUtil.dp2px(7f).toInt())))
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

    }}