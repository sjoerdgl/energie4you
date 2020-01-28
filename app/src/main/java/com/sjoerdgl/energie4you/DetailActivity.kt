package com.sjoerdgl.energie4you

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {
    private var id: Int = 0
    private var imageBitmap: Bitmap? = null
    private lateinit var faultyItem: LiveData<FaultyItem>
    private lateinit var faultyItemRepository: FaultyItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        faultyItemRepository = FaultyItemRepository(FaultyItemDatabase.getDatabase(application).faultyItemDao())

        this.imageBitmap = intent.getParcelableExtra("imageBitmap")
//        val imageView: ImageView = findViewById(R.id.preview)
//        imageView.setImageBitmap(this.imageUri)
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

        id = intent.getIntExtra("id", 0);

        if (id > 0) {
            faultyItem = faultyItemRepository.findById(id)

            faultyItem.observe(this, Observer {
                name.setText(it.name)
                description.setText(it.description)
                category_spinner.setSelection(it.category)
            })
        }

        fab.setOnClickListener {
            if (id > 0) {
                faultyItem.observe(this, Observer {
                    it.name = name.text.toString()
                    it.description = description.text.toString()
                    it.category = category_spinner.selectedItemPosition

                    updateFaultyItem(it)

                    Toast.makeText(this, "Defect bijgewerkt", Toast.LENGTH_SHORT).show()
                    finish()
                })
            } else {
                val pictureFile: File? = getOutputMediaFile()

                if (pictureFile == null) {
                    finish()
                }

                try {
                    val fos = FileOutputStream(pictureFile)
                    imageBitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
                    fos.close()
                } catch (e: FileNotFoundException) {
                    Toast.makeText(this, "Something went wrong saving the photo", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: IOException) {
                    Toast.makeText(this, "Something went wrong saving the photo", Toast.LENGTH_SHORT).show()
                    finish()
                }

                createFaultyItem(name.text.toString(), category_spinner.selectedItemPosition, description.text.toString(), pictureFile?.absolutePath ?: "")
                Toast.makeText(this, pictureFile?.absolutePath, Toast.LENGTH_SHORT).show()

                Toast.makeText(this, "Defect aangemaakt", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)

        if( id == 0) {
            menu.findItem(R.id.action_mail).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_mail -> {
//                faultyItem.observe(this, Observer {
//                    val emailIntent = Intent(
//                        Intent.ACTION_SENDTO, Uri.fromParts(
//                            "mailto", "", null
//                        )
//                    )
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, it.description)
//                    startActivity(Intent.createChooser(emailIntent, "E-mail versturen..."))
//                })

                val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
                }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Toast.makeText(this@DetailActivity, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(
            this.getExternalFilesDir(null),
            "/photos"
        )

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val mImageName = "MI_$timeStamp.jpg"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
    }


    private fun createFaultyItem(name: String, category: Int, description: String, fileLocation: String) = GlobalScope.launch {
        val faultyItem = FaultyItem(name, description, category, fileLocation)

        faultyItemRepository.create(faultyItem)
    }

    private fun updateFaultyItem(faultyItem: FaultyItem) = GlobalScope.launch {
        faultyItemRepository.update(faultyItem)
    }
}
