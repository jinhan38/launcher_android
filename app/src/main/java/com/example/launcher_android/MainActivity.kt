package com.example.launcher_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.apps_button).setOnClickListener {
            val i = Intent(this, AppsListActivity::class.java)
            startActivity(i)
        }
    }

}