package com.abnuj.marketgadsimplebillingapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.abnuj.marketgadsimplebillingapp.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    var stringBuilder: StringBuilder = java.lang.StringBuilder()


    companion object {
        val imagebitmap = mutableListOf<Bitmap>()
        var finalBalance: Float = 0f
        val pricelist = mutableListOf<Float>()
    }

    var currentvalue: String = "0"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvtotal.setText(resources.getString(R.string.ruppes) + "0")
        binding.tvcurrent.setText(resources.getString(R.string.ruppes) + "0")
        setUpClickListener()
    }


    private fun setUpClickListener() {
        binding.tv0.setOnClickListener {
            stringBuilder.append("0")
            setcurrentvalues(stringBuilder)
        }
        binding.tv1.setOnClickListener {
            stringBuilder.append("1")
            setcurrentvalues(stringBuilder)
        }
        binding.tv2.setOnClickListener {
            stringBuilder.append("2")
            setcurrentvalues(stringBuilder)
        }
//
        binding.tv3.setOnClickListener {
            stringBuilder.append("3")
            setcurrentvalues(stringBuilder)
        }
        binding.tv4.setOnClickListener {
            stringBuilder.append("4")
            setcurrentvalues(stringBuilder)
        }
        binding.tv5.setOnClickListener {
            stringBuilder.append("5")
            setcurrentvalues(stringBuilder)
        }
        binding.tv6.setOnClickListener {
            stringBuilder.append("6")
            setcurrentvalues(stringBuilder)
        }
        binding.tv7.setOnClickListener {
            stringBuilder.append("7")
            setcurrentvalues(stringBuilder)
        }
        binding.tv8.setOnClickListener {
            stringBuilder.append("8")
            setcurrentvalues(stringBuilder)
        }
        binding.tv9.setOnClickListener {
            stringBuilder.append("9")
            setcurrentvalues(stringBuilder)
        }
        binding.tvclear.setOnClickListener {
            stringBuilder.clear()
            finalBalance = 0f
            pricelist.clear()
            imagebitmap.clear()
            binding.imgitem.setImageDrawable(getDrawable(R.drawable.camera))
            binding.tvtotal.setText(resources.getString(R.string.ruppes) + "0")
            binding.tvcurrent.setText(resources.getString(R.string.ruppes) + "0")
            setcurrentvalues(stringBuilder)
        }

        binding.tvdot.setOnClickListener {
            stringBuilder.append(".")
            setcurrentvalues(stringBuilder)
        }

        binding.imageback.setOnClickListener {

            if (stringBuilder.length == 0) {
                setcurrentvalues(stringBuilder)
                binding.tvcurrent.setText("0")
            } else {
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
                setcurrentvalues(stringBuilder)
            }
        }

        binding.imageright.setOnClickListener {
            Log.d(TAG, "setUpClickListener: Pricelist Size : ${pricelist.size} Image size :${imagebitmap.size}")
            if (imagebitmap.size < 1) {
                Toast.makeText(applicationContext, "Add Products for Generate Bill", Toast.LENGTH_SHORT).show()
            } else {
                val abnuj = currentvalue.toFloat()
                pricelist.add(abnuj)
                Log.d(TAG, "setUpClickListener: $abnuj and price list $pricelist")
                if (imagebitmap.size == pricelist.size) {
                    startActivity(Intent(this, GenerateReceiptActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "ProductImage and Product count not same \n clear all and try again", Toast.LENGTH_LONG).show()
                }
            }
        }


        binding.tvplus.setOnClickListener {
            setUpCamera()
            if (imagebitmap.size == 0) {
                Toast.makeText(applicationContext, "Attach Image of Product", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val abnuj = currentvalue.toFloat()
                pricelist.add(abnuj)
                Log.d(TAG, "setUpClickListener: $abnuj and price list $pricelist")
            }
            if (!(stringBuilder.length == 0)) {
                val total = (currentvalue.toFloat() + finalBalance)
                finalBalance = total
                stringBuilder.clear()
                binding.tvtotal.setText(resources.getString(R.string.ruppes) + String.format("%.2f", total))
                setcurrentvalues(stringBuilder)
            } else {
                setcurrentvalues(stringBuilder)
            }

        }
        binding.tvminus.setOnClickListener {
            if (!(stringBuilder.length == 0)) {
                val total = (finalBalance - currentvalue.toFloat())
                finalBalance = total
                stringBuilder.clear()
                binding.tvtotal.setText(String.format("%.2f", total))
                setcurrentvalues(stringBuilder)
            } else {
                setcurrentvalues(stringBuilder)
            }
        }

        binding.imgcapture.setOnClickListener {
            setUpCamera()
        }

        binding.imgitem.setOnClickListener {
            setUpCamera()
        }
    }


    private fun setUpCamera() {
        cameraRegister.launch(null)
    }

    val cameraRegister = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it == null) {
            finish()
        } else {
            binding.imgitem.setImageBitmap(it)
            imagebitmap.add(it)
            binding.tvitemnumber.text = imagebitmap.size.toString()
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        currentvalue = "0"
        stringBuilder.clear()
        binding.tvcurrent.setText(currentvalue)
    }

    // set current value
    fun setcurrentvalues(st: StringBuilder) {

        if (st.length == 0) {
            st.clear()
            Log.d(TAG, "setUpClickListener: ${st.length}")
            binding.tvcurrent.setText("0")
        } else {
            currentvalue = st.toString()
            binding.tvcurrent.setText("${resources.getString(R.string.ruppes)} ${currentvalue}")
        }
    }
}
