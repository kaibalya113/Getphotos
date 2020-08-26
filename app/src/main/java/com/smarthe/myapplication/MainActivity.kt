package com.smarthe.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

private const val REQUEST_CODE =42
private lateinit var photoFile: File
private const val File_Name = "1.jpg"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // add onClickListener to the button, when user clicks the button it will navigate to
        // camera app
        takePicture.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(File_Name)

            // This does not work for API >= 24
           // takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(this, "com.smarthe.fileprovider", photoFile)
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // Now check is there any  camera app present in the device? if present dont do anything
            if(takePicture.resolveActivity(this.packageManager)!= null){
                Toast.makeText(this, "camera is opening", Toast.LENGTH_LONG).show()
                startActivityForResult(takePicture, REQUEST_CODE )

            }else{
                Toast.makeText(this, "camera is unable to open", Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun getPhotoFile(fileName: String): File {
        // use getExternalFileDir on context to access package-specific directories
         val storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(takenImage)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }


    }
}