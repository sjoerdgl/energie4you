package com.sjoerdgl.energie4you

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var faultyItemRepository: FaultyItemRepository
    lateinit var allFaultyItems: LiveData<List<FaultyItem>>
    lateinit var adapter: FaultyItemAdapter

    companion object {
        const val TAKE_PICTURE_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val faultyItemDao = FaultyItemDatabase.getDatabase(application).faultyItemDao()
        faultyItemRepository = FaultyItemRepository(faultyItemDao)


        allFaultyItems = faultyItemRepository.allFaultyItems
        allFaultyItems.observe(this, Observer {
            adapter.setNewList(ArrayList(it))
        })

        adapter = FaultyItemAdapter(ArrayList())
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PICTURE_CODE);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            TAKE_PICTURE_CODE -> {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val intent = Intent(this, DetailActivity::class.java)

                intent.putExtra("imageBitmap", imageBitmap);
                this.startActivity(intent)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Hallo sjoerd!", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
