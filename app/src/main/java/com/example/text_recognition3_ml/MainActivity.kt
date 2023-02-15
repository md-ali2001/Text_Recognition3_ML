package com.example.text_recognition3_ml

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText


@Suppress("DEPRECATION")
 class MainActivity : AppCompatActivity() {
    private var imageBitmap:Bitmap?=null
    var img:ImageView ?=null
    var Extracttext:TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val detect:Button=findViewById(R.id.detect)
        val snap:Button=findViewById(R.id.snap)
       img=findViewById(R.id.img)
        Extracttext=findViewById(R.id.extracttext2)
        snap.setOnClickListener({ dispatchTakePictureIntent() })

        detect.setOnClickListener({ detectText() })


    }

    val REQUEST_IMAGE_CAPTURE=1

    @SuppressLint("QueryPermissionsNeeded")
    private fun  dispatchTakePictureIntent()
    {
        val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(packageManager)!=null)
        {
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode== Activity.RESULT_OK){
            val extras=data!!.extras
            imageBitmap=extras!!.get("data") as Bitmap
            img!!.setImageBitmap(imageBitmap)

    }
}

    private fun detectText()
    {
        val image=FirebaseVisionImage.fromBitmap(imageBitmap!!)

        val detector=FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image).addOnSuccessListener(OnSuccessListener <FirebaseVisionText>{
            firebaseVisionText ->

            processText(firebaseVisionText)}).addOnFailureListener(OnFailureListener {  })

        }

    private fun processText(Text: FirebaseVisionText) {

        val blocks=Text.textBlocks
        if(blocks.size==0){
            Toast.makeText(this,"no text found",Toast.LENGTH_LONG).show()
            return
        }

        for(block in Text.textBlocks)
        {
            val text=block.getText()
            Extracttext!!.textSize=16f
            Extracttext!!.setText(text)

        }

   }
    }

