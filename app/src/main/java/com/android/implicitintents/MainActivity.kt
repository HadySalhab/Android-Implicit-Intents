package com.android.implicitintents

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var pickContactButton:Button
    private lateinit var webEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var textEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webEditText = findViewById(R.id.editText_web)
        locationEditText = findViewById(R.id.editText_location)
        textEditText = findViewById(R.id.editText_text)
        pickContactButton = findViewById(R.id.button_contact)
    }

    fun showWeb(view: View) {
        val webURL: String? = webEditText.text.toString()
        if (webURL == null) {
            Toast.makeText(this, "Please insert a url", Toast.LENGTH_LONG).show()
            return
        }
        val webIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(webURL)
        }
        val safe = resolveIntent(webIntent)
        if (safe) {
            startIntent(webIntent, "Select Your prefered Browser")
        } else {
            Toast.makeText(this, "Sorry!! can't handle you request", Toast.LENGTH_LONG).show()
        }
    }

    fun pickContact(view:View) {
        val intent:Intent = Intent(Intent.ACTION_PICK).apply {
            data = ContactsContract.Contacts.CONTENT_URI
        }
            val safe = resolveIntent(intent)
            if(safe){
                startActivity(intent)
            }

    }

    fun showLocation(view: View) {
       // val location = "geo:0,0?q=" + locationEditText.text.toString()
       // val uri = Uri.Builder().scheme("geo").path("0,0").query(locationEditText.text.toString()).build()
        val uri = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",locationEditText.text.toString()).build()

        Log.d("MainActivity","Uri value is ${uri}")


        val locationIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = uri
        }
        val safe = resolveIntent(locationIntent)
        if (safe) {
            startIntent(locationIntent, "Select Your Prefered Map")
        } else {
            Toast.makeText(this, "Sorry!! can't handle you request", Toast.LENGTH_LONG).show()
        }
    }

    fun sendText(view:View){
        val text = textEditText.text.toString()
        ShareCompat.IntentBuilder.from(this).setText(text).setType("text/plain").startChooser()

    }


    fun startIntent(intent: Intent, chooserTitle: String) {
        val chooser = Intent.createChooser(intent, chooserTitle)
        startActivity(chooser)
    }

    fun resolveIntent(intent: Intent): Boolean {
        return intent.resolveActivity(packageManager) != null
    }


}
