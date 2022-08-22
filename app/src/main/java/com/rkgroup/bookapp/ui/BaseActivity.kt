package com.rkgroup.bookapp.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rkgroup.bookapp.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: AlertDialog
    fun showProgress(msg: String = getString(R.string.please_wait)) {
        if (!isDestroyed) {
            hideProgress()
            val dialogView = View.inflate(this, R.layout.progress_dialog, null)
            dialogView.findViewById<TextView>(R.id.tvLoaderMSg).text = msg
            progressDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .show()
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    }

    fun hideProgress() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onDestroy() {
        hideProgress()
        super.onDestroy()
    }

}