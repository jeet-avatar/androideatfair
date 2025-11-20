package com.eatfair.orderapp.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri

object IntentUtils {

    fun callCustomer(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
        }
        context.startActivity(intent)
    }

    fun openMap(context: Context, address: String) {
        val gmmIntentUri = "geo:0,0?q=${Uri.encode(address)}".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            .apply {
//                setPackage("com.google.android.apps.maps")
//            }

        val packageManager = context.packageManager
        if (mapIntent.resolveActivity(packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Google Maps not available", Toast.LENGTH_SHORT).show()
        }
    }

}