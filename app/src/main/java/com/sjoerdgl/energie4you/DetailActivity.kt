package com.sjoerdgl.energie4you

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var nameTextView: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val imageView: ImageView = findViewById(R.id.preview)
        imageView.setImageBitmap(imageBitmap)

        nameTextView = findViewById(R.id.name)

        val spinner: Spinner = findViewById(R.id.category_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val id = intent.getIntExtra("id", 0);

        if (id > 0) {
            val faultyItemDao = FaultyItemDatabase.getDatabase(application).faultyItemDao()
            val faultyItemRepository = FaultyItemRepository(faultyItemDao)
            val faultyItem: LiveData<FaultyItem> = faultyItemRepository.findById(id)

            faultyItem.observe(this, Observer {
                nameTextView = findViewById(R.id.name)
                nameTextView.setText(it.name)
            })
        }

        nameTextView.text.toString().isNotEmpty()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    fun createFaultyItem(faultyItem: FaultyItem) = GlobalScope.launch {
        val faultyItemDao = FaultyItemDatabase.getDatabase(application).faultyItemDao()
        val faultyItemRepository = FaultyItemRepository(faultyItemDao)
        faultyItemRepository.create(faultyItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_finish) {
            val faultyItem = FaultyItem(nameTextView.text.toString(), description.text.toString(), "")
            createFaultyItem(faultyItem)
            Toast.makeText(this, "Saved succesfull!", Toast.LENGTH_SHORT).show()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
