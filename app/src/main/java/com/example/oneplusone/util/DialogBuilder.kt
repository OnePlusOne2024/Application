package com.example.oneplusone.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.oneplusone.R
import com.example.oneplusone.databinding.ProductDetailViewerBinding
import com.example.oneplusone.model.data.ProductData

class DialogBuilder {
    fun showProductDetailDialog(context: Context, productData: ProductData) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.product_detail_viewer, null)
        val dialogBinding = ProductDetailViewerBinding.bind(mDialogView)
        dialogBinding.productData = productData

        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)

        val dialog = mBuilder.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}