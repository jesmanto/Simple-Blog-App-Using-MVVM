package com.jesse.simpleblogappmvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.jesse.simpleblogappmvvm.R

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make full screen
        makeFullScreen()

        setContentView(R.layout.activity_splash)

        // Perform a transition to the MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)

            //Animate the transition
            animateTransition()

            finish()
        },2000)

    }

    /**
     * Called immediately after one of the flavors of startActivity(Intent) or finish
     * to specify an explicit transition animation to perform next.
     */
    private fun animateTransition(){
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    /**
     * Called to enable a fullscreen without toolBar and actionBar
     */
    private fun makeFullScreen(){

        //Make full screen
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)

        // remove Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Hide status bar
        supportActionBar?.hide()
    }
}