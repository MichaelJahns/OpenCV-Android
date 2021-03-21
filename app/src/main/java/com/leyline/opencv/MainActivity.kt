package com.leyline.opencv

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.leyline.opencv.databinding.ActivityMainBinding
import java.lang.Float.max


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    var srcBitmap: Bitmap? = null
    var dstBitMap: Bitmap? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.sldSigma.setOnSeekBarChangeListener(this)

        srcBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.tree)
        dstBitMap = srcBitmap!!.copy(srcBitmap!!.config, true)

        updateImageView()
        val view = binding.root
        setContentView(view)
    }

    private fun updateImageView() {
        binding.mutableImage.setImageBitmap(this.dstBitMap)
    }

    fun btnGrayscale(view: View) {
        Log.i("GRAYSCALE", "Attempted grayscale")
        this.myErode(srcBitmap!!, dstBitMap!!)
        updateImageView()
    }

    fun btnAutumn(view: View) {
        Log.i("AUTUMN", "Attempted ColorMap")
        updateImageView()
    }

    //  FLIP
    fun btnFlipHorizontal(view: View) {
        Log.i("Flip", "Attempted Flip")
        this.myFlip(srcBitmap!!, dstBitMap!!)
        updateImageView()
    }

    //    NDK
    external fun myFlip(bitmapIn: Bitmap, bitmapOut: Bitmap)
    external fun myBlur(bitmapIn: Bitmap, bitmapOut: Bitmap, sigma: Float)
    external fun myErode(bitmapIn: Bitmap, bitmapOut: Bitmap)
    external fun myDilation(bitmapIn: Bitmap, bitmapOut: Bitmap)


    //   BLUR
    fun doBlur() {
        val progress = binding.sldSigma.progress
        val sigma = max(0.1F, progress / 5F)
        this.myBlur(srcBitmap!!, dstBitMap!!, sigma)
        updateImageView()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        doBlur()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}