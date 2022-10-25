package com.example.practical5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun excusesButton(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun holidayButton(view: View){
        val intent = Intent(this,FindHoliday::class.java)
        startActivity(intent)
    }
}