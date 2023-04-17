package com.example.minedemo.activity

import android.media.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.minedemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.ByteBuffer


class TestMediaMuxerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_media_muxer)
        initMediaMuxer()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initMediaMuxer() {
//        lifecycleScope.launch(Dispatchers.IO) {
            val resultPath = Environment.getExternalStorageDirectory().path + "/000/testMuxer.m4a"
            val audioPath = Environment.getExternalStorageDirectory().path + "/000/lemon.m4a"

            val file = File(resultPath)
            if (!file.exists()) {
                file.createNewFile()
            }

            val mediaMuxer = MediaMuxer(resultPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            val mediaExtractor = MediaExtractor()
            mediaExtractor.setDataSource(audioPath)

            //添加音频轨
            val audioIndex = selectTrack(mediaExtractor)
            mediaExtractor.selectTrack(audioIndex)
            val audioFormat = mediaExtractor.getTrackFormat(audioIndex)
            val muxerAudioIndex = mediaMuxer.addTrack(audioFormat)
            var capacity = audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
            Log.e(TAG, "-----> capacity: $capacity")
            if (capacity == 0) {
                capacity = 1024 * 1024
            }
            val byteBuffer = ByteBuffer.allocate(capacity)
            val bufferInfo = MediaCodec.BufferInfo()

            //添加字幕轨
            val textVitFormat = MediaFormat.createSubtitleFormat(MediaFormat.MIMETYPE_TEXT_VTT, "und")
            val vitIndex = mediaMuxer.addTrack(textVitFormat)

            mediaMuxer.start()
            try {
                while (true) {
                    val readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0)
                    if (readSampleSize < 0) {
                        Log.e(TAG, "-----> readSampleSize  break")
                        mediaExtractor.unselectTrack(audioIndex)
                        break
                    }
                    bufferInfo.size = readSampleSize
                    bufferInfo.flags = mediaExtractor.sampleFlags
                    bufferInfo.offset = 0
                    bufferInfo.presentationTimeUs = mediaExtractor.sampleTime
                    Log.e(TAG, "-----> bufferInfo: $bufferInfo")


                    mediaMuxer.writeSampleData(muxerAudioIndex, byteBuffer, bufferInfo)

                    mediaExtractor.advance()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            try {
                val text = "hello world"
                val textByteArray = text.toByteArray()
                val vitByteBuffer = ByteBuffer.wrap(textByteArray)
                while (true) {
                    val vitBufferInfo = MediaCodec.BufferInfo()
                    vitBufferInfo.offset = 0
                    vitBufferInfo.size = textByteArray.size
                    mediaMuxer.writeSampleData(vitIndex, vitByteBuffer, vitBufferInfo)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                mediaMuxer.stop()
                mediaMuxer.release()
                mediaExtractor.release()
            }

            Log.e(TAG, "-----> encode end")
//        }

    }

    private fun selectTrack(extractor: MediaExtractor): Int {
        val numTracks = extractor.trackCount
        for (i in 0 until numTracks) {
            Log.e(TAG, "-----> selectTrack: $i")
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith("audio/")) {
                return i
            }
        }
        return -5
    }

    companion object {
        const val TAG = "Muxer"
    }
}