package com.abnuj.marketgadsimplebillingapp

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.abnuj.marketgadsimplebillingapp.databinding.ActivitySendInvoiceBinding
import java.io.File

class SendInvoiceActivity : AppCompatActivity() {
    lateinit var filename: String
    lateinit var binding: ActivitySendInvoiceBinding
    var phonenumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        filename = intent.getStringExtra("name").toString()
        phonenumber = binding.edphonenumber.text.toString()
        if (phonenumber.equals("") || phonenumber.length < 10) {
            Toast.makeText(
                applicationContext,
                "Enter Phone number before continue",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.imgwhatsapp.setOnClickListener {
                whatsappIntent()
            }
            binding.imgsms.setOnClickListener {
                whatsappIntent()
            }
        }


    }

    private fun whatsappIntent() {

        val pdfFolder: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString();
        val file = File(pdfFolder, filename)
        if (file.exists()) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("*/*")
            shareIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    applicationContext,
                    applicationContext.packageName + ".provider",
                    file
                )
            )
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Invoice bill for you Purchase")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Extra text")
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent);
            }
        } else {
            Toast.makeText(applicationContext, "file does not exist", Toast.LENGTH_SHORT).show()
        }
    }


}