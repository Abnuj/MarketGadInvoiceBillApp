package com.abnuj.marketgadsimplebillingapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.abnuj.marketgadsimplebillingapp.Adaptor.ProductModel
import com.abnuj.marketgadsimplebillingapp.Adaptor.ProductRecyclerAdpter
import com.abnuj.marketgadsimplebillingapp.databinding.ActivityGenerateReceiptBinding
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class GenerateReceiptActivity : AppCompatActivity() {
    lateinit var binding: ActivityGenerateReceiptBinding
    var adaptor: ProductRecyclerAdpter? = null
    var productlist = mutableListOf<ProductModel>()
    var seriel = 1
    var productplace: Int = 0
    lateinit var ImagetoPdfVIew: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adaptor = ProductRecyclerAdpter()
        for (i in MainActivity.imagebitmap) {

            val product = ProductModel(seriel, i, MainActivity.pricelist[productplace])
            productlist.add(product)
            seriel++
            productplace++
        }
        adaptor!!.submitList(productlist)

        binding!!.itemrecyclerview.apply {
            layoutManager = LinearLayoutManager(this@GenerateReceiptActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = adaptor
        }

        setupOtherViews()

        ImagetoPdfVIew = findViewById<ConstraintLayout>(R.id.ImagetoPdfVIew) as View

        binding.exportbtn.setOnClickListener {
            writeexternalPermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    }

    val writeexternalPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            binding.bottomlinear.visibility = View.GONE
            ConvertItemViewtoPDf(ImagetoPdfVIew)
        } else {
            Toast.makeText(applicationContext, "We Need Permission for store pdf", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ConvertItemViewtoPDf(item: View) {
        Handler(mainLooper).postDelayed({
            val bitmap: Bitmap = createBitmapFromView(item, item.measuredWidth, item.measuredHeight)
//        creating our first pdf file in android
            generatePdf(bitmap)
        }, 1000)
    }


    fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        if (width > 0 && height > 0) {
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(
                            DynamicUnitUtils
                                    .convertDpToPixels(width.toFloat()), View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                            DynamicUnitUtils
                                    .convertDpToPixels(height.toFloat()), View.MeasureSpec.EXACTLY
                    )
            )
        }
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background: Drawable = view.getBackground()
        background.draw(canvas)
        view.draw(canvas)
        return bitmap
    }

    private fun generatePdf(bitmap: Bitmap) {
        try {
            val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString()
            val currentTime: Date = Calendar.getInstance().time
            val mypdfName: String = "$currentTime.pdf"
            val filename = File(path,mypdfName)
            val ouputstream = FileOutputStream(filename)

            val pdfwriter: PdfWriter = PdfWriter(filename)
            val pdfdocument = PdfDocument(pdfwriter)
            val document = Document(pdfdocument)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val bitmapdata: ByteArray = byteArrayOutputStream.toByteArray()
            val imageData: ImageData = ImageDataFactory.create(bitmapdata)
            val image = com.itextpdf.layout.element.Image(imageData)
            document.add(image)
            document.close()
            Toast.makeText(applicationContext, "Pdf created", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SendInvoiceActivity::class.java).apply { putExtra("name", mypdfName) })
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupOtherViews() {
        var grandtotal = 0f
        MainActivity.pricelist.forEach {
            grandtotal += it
            Log.d("tag", "$it")
        }
        binding?.tvgrandtotal?.text = applicationContext.resources.getString(R.string.ruppes) + "${String.format("%.2f", grandtotal)}"
        binding?.tvtotal1?.text = applicationContext.resources.getString(R.string.ruppes) + "${String.format("%.2f", grandtotal)}"
        binding?.tvtotalitem?.text = productlist.size.toString()
    }
}