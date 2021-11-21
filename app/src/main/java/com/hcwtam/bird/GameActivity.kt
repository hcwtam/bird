package com.hcwtam.tappykirb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.math.log

class GameActivity : AppCompatActivity() {

    var displayWidth = 0
    var displayHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Find screen width and height
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels

        // move sprite on click
        findViewById<View>(android.R.id.content).setOnClickListener {
            val sprite = findViewById<ImageView>(R.id.sprite)
            moveSpriteOnClick(sprite)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {

            // Find views
            val sprite = findViewById<ImageView>(R.id.sprite)
            val upperObstacles = findViewById<ConstraintLayout>(R.id.topBlocksLayout)
            val lowerObstacles = findViewById<ConstraintLayout>(R.id.bottomBlocksLayout)


            // Make sprite fall
            val handler = Handler(Looper.getMainLooper())
            handler.post(object : Runnable {
                override fun run() {
                    moveSprite(sprite)

                    handler.postDelayed(this, 10)
                }
            })


            val genObstaclesHandler = Handler(Looper.getMainLooper())
            genObstaclesHandler.post(object : Runnable {

                override fun run() {
                    val(generatedUpperObstacles, generatedBottomObstacles) = cloneObstacles(upperObstacles, lowerObstacles)
                    // Make obstacles move left
                    val obstacleHandler = Handler(Looper.getMainLooper())
                    obstacleHandler.post(object : Runnable {
                        override fun run() {
                            moveObstacles(generatedUpperObstacles, generatedBottomObstacles)

                            obstacleHandler.postDelayed(this, 10)
                        }
                    })

                    genObstaclesHandler.postDelayed(this, 1000)
                }
            })
        }
    }

    private fun generateTopBlocks(obstacles: ConstraintLayout) {
        // Find block image height
        val blockHeight = findViewById<ImageView>(R.id.mainTopBlock).measuredHeight

        var current = (obstacles.height - blockHeight).toFloat()

        while (current >= 0) {
            val generatedBlock = ImageView(this)
            generatedBlock.setImageResource(R.drawable.block)
            generatedBlock.y = current
            current -= blockHeight
            obstacles.addView(generatedBlock)
        }
    }

    private fun generateBottomBlocks(obstacles: ConstraintLayout) {
        // Find block image height
        val lowerBlock = findViewById<ImageView>(R.id.mainBottomBlock)
        val blockHeight = lowerBlock.measuredHeight

        var current = (lowerBlock.top + blockHeight).toFloat()
        var limit = lowerBlock.top

        while (limit <= displayHeight) {
            val generatedBlock = ImageView(this)
            generatedBlock.setImageResource(R.drawable.block)
            generatedBlock.y = current
            current += blockHeight
            limit += blockHeight
            obstacles.addView(generatedBlock)
        }

    }

    private fun cloneObstacles(
        topObstacle: ConstraintLayout,
        bottomObstacle: ConstraintLayout
    ): List<ConstraintLayout> {
        val topSet = ConstraintSet()
        val bottomSet = ConstraintSet()

        val generatedTopObstacle = ConstraintLayout(this)
        val generatedBottomObstacle = ConstraintLayout(this)


//        val location = IntArray(2)
//        findViewById<ImageView>(R.id.mainTopBlock).getLocationOnScreen(location)
//        val topBlock = ImageView(this)
//        topBlock.setImageResource(R.drawable.block)
//        topBlock.x = location[0].toFloat()
//        topBlock.y = location[1].toFloat()
//        topBlock.id = View.generateViewId();
//        generatedTopObstacle.addView(topBlock);
//
//        findViewById<ImageView>(R.id.mainBottomBlock).getLocationOnScreen(location)
//        val bottomBlock = ImageView(this)
//        bottomBlock.setImageResource(R.drawable.block)
//        bottomBlock.x = location[0].toFloat()
//        bottomBlock.y = location[1].toFloat()
//        bottomBlock.id = View.generateViewId();
//        generatedBottomObstacle.addView(bottomBlock);
//
        topSet.clone(topObstacle)
//        topSet.connect(topBlock.id, ConstraintSet.TOP, generatedTopObstacle.id, ConstraintSet.BOTTOM, 0);
        topSet.applyTo(generatedTopObstacle)
//
//        bottomSet.clone(bottomObstacle)
//        bottomSet.connect(bottomBlock.id, ConstraintSet.TOP, generatedBottomObstacle.id, ConstraintSet.BOTTOM, 0);
//        bottomSet.applyTo(generatedBottomObstacle)
//
//        // Create blocks dynamically
//        generateTopBlocks(generatedTopObstacle)
//        generateBottomBlocks(generatedBottomObstacle)
//
//
//        Log.i("asdasd", generatedTopObstacle.toString())
        generatedTopObstacle.setBackgroundColor(0xFF00FF00.toInt())

        return listOf(generatedTopObstacle, generatedBottomObstacle)
    }

    fun moveSprite(sprite: ImageView) {
        val positions = IntArray(2)
        sprite.getLocationOnScreen(positions)
        val y = positions[1]
        if (y <= displayHeight) {
            sprite.translationY += 8
        }
    }

    private fun moveSpriteOnClick(sprite: ImageView) {
        sprite.translationY -= 230
    }

    fun moveObstacles(upperObstacles: ConstraintLayout, lowerObstacles: ConstraintLayout) {
        upperObstacles.translationX -= 5
        lowerObstacles.translationX -= 5
    }
}