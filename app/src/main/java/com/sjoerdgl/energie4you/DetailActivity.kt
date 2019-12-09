package com.sjoerdgl.energie4you

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText

class DetailActivity : AppCompatActivity() {

    lateinit var nameTextView: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val imageView: ImageView = findViewById(R.id.preview)
        imageView.setImageBitmap(imageBitmap)

        nameTextView = findViewById(R.id.name)
    }
}
