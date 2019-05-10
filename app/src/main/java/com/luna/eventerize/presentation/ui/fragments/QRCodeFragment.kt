package com.luna.eventerize.presentation.ui.fragments

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


private const val INTENT_QR_ID_EXTRA = "INTENT_QR_ID_EXTRA"
private const val BASE_URL = "https://bigoud.games/eventerizeapp/"

class QRCodeFragment: BaseFragment<QRCodeViewModel>() {

    override val viewModelClass = QRCodeViewModel::class

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

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
        var bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 200, 0, 0, bitMatrixWidth, bitMatrixHeight)
        bitmap = getResizedBitmap(bitmap, 1000, 1000)
        fragment_qr_code_image.setImageBitmap(bitmap)
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
