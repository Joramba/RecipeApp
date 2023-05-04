package com.example.recipeapp.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backgroundImg: TextView = binding.textv
        val sideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                // Start
            }

            override fun onAnimationEnd(animation: Animation?) {
                when (animation) {
                    sideAnimation -> backgroundImg.startAnimation(fadeInAnimation)
                    fadeInAnimation -> {
                        backgroundImg.visibility = INVISIBLE
                        return
                    }
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //Repeat
            }
        }
        sideAnimation.setAnimationListener(animationListener)
        fadeInAnimation.setAnimationListener(animationListener)

        backgroundImg.startAnimation(sideAnimation)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },3000)
    }
}