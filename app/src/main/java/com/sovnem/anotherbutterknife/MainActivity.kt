package com.sovnem.anotherbutterknife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sovnem.annotation.BindView
import com.sovnem.lib.OtherBuffer

class MainActivity : AppCompatActivity() {


    @BindView(R.id.haha)
    lateinit var tv: TextView

    @BindView(R.id.hoho)
    lateinit var et: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OtherBuffer.bind(this)
        tv.text = "我就是哈哈"

        et.text="你好啊"
    }
}