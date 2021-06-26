package com.example.countsheep

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var sheepCount: Int = 0
    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar = supportActionBar
        actionBar?.hide()
        frameLayout = findViewById(R.id.frameLayout)
        resetView()
    }

    private fun resetView() {
        sheepCount = 0
        frameLayout.removeAllViews()
        val randomLayout = RandomLayout(this)
        val total = Random.nextInt(50, 150)
        randomLayout.initView(total)
        randomLayout.setCountListener {
            sheepCount += 1
            if (sheepCount == total) {
                Toast.makeText(this, "congratulation", Toast.LENGTH_LONG).show()
                frameLayout.postDelayed({ resetView() }, 5000)
            }
        }
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout.addView(randomLayout, layoutParams)
    }

}