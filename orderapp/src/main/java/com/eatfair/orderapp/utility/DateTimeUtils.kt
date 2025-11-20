package com.eatfair.orderapp.utility

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateTimeUtils {

    fun getTimeAgo(orderTime: String): String {
        return try {
            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val orderDate = dateFormat.parse(orderTime)

            // Current time
            val now = Calendar.getInstance().time

            // If order time is from today, but earlier than now
            if (orderDate != null) {
                // Adjust to today’s date (since parse gives today's date already)
                val diffInMillis = now.time - orderDate.time

                when {
                    diffInMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                    diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                        "$minutes mins ago"
                    }

                    diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                        "$hours hrs ago"
                    }

                    else -> {
                        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        "$days days ago"
                    }
                }
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}