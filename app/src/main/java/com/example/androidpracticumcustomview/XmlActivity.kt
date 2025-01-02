package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer

class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)

        val firstView = TextView(this).apply {
            text = context.getString(R.string.first_view_text)
            textSize = 20F
        }
        customContainer.addView(firstView)

        val secondView = TextView(this).apply {
            text = context.getString(R.string.second_view_text)
            textSize = 20F
        }

        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 1000)
    }
}