package com.ssafy.fundyou.util.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun Context.showToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message : String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun addComma(number: Int): String = if (number >= 0) {
    "%,d".format(number)
} else {
    "- "
}

fun Context.getColorNoTheme(id : Int) = ResourcesCompat.getColor(resources, id, null)

fun getFormattedCurrentTime(): String{
    val formatter = SimpleDateFormat("yyyyMMdd_hhmmss_", Locale.KOREA)
    return formatter.format(Date(System.currentTimeMillis()))
}