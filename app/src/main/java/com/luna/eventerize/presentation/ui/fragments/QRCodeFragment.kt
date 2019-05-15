package com.luna.eventerize.presentation.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.QRCodeViewModel
import kotlinx.android.synthetic.main.fragment_qr_code.*
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val INTENT_QR_ID_EXTRA = "INTENT_QR_ID_EXTRA"
private const val BASE_URL = "https://bigoud.games/eventerizeapp/"

class QRCodeFragment: BaseFragment<QRCodeViewModel>(), View.OnClickListener {
    lateinit var qrBitmap: Bitmap

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.fragment_qr_code_button_share -> {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "image/jpeg"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Je t'invites à mon event !")
                val bytes = ByteArrayOutputStream()
                qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val f = File(Environment.getExternalStorageDirectory(), File.separator + "qrcode.jpg")
                try {
                    f.createNewFile()
                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                } catch (e: IOException) {
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory().path + "/qrcode.jpg"))
                try {
                    startActivity(
                        Intent.createChooser(
                            sharingIntent,
                            "Partager avec"
                        )
                    )

                } catch (ex: android.content.ActivityNotFoundException) {
                    AlertDialog.Builder(context!!)
                        .setMessage("Share failed")
                        .setPositiveButton("OK",
                            { dialog, whichButton -> }).create().show()
                }
            }
        }
    }

    override val viewModelClass = QRCodeViewModel::class

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr_code, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        fragment_qr_code_button_share.setOnClickListener(this)

        activity!!.title = getString(R.string.qr_code_title)
        fragment_qr_code_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(fragment_qr_code_toolbar)

        val bitMatrix: BitMatrix
        val qrCodeString = BASE_URL + arguments?.getString(INTENT_QR_ID_EXTRA)
        try {
            bitMatrix = MultiFormatWriter().encode(
                qrCodeString,
                BarcodeFormat.QR_CODE,
                200, 200, null
            )

        } catch (illegalArgumentException: IllegalArgumentException) {
            TODO()
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    resources.getColor(R.color.black)
                else
                    resources.getColor(R.color.white)
            }
        }
        qrBitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        qrBitmap.setPixels(pixels, 0, 200, 0, 0, bitMatrixWidth, bitMatrixHeight)
        qrBitmap = getResizedBitmap(qrBitmap, 1000, 1000)
        fragment_qr_code_image.setImageBitmap(qrBitmap)
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    companion object {
        fun newInstance(identifier: String = ""): QRCodeFragment {
            val fragment = QRCodeFragment()
            val args = Bundle()
            args.putString(INTENT_QR_ID_EXTRA, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}
