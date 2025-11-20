package com.eatfair.orderapp.ui.screens.home.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.eatfair.orderapp.model.MapPoint
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

object EFMapUtils {

    suspend fun createCircularImageMarker(context: Context, point: MapPoint): BitmapDescriptor? {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(point.imageUrl ?: point.imageRes)
            .allowHardware(false)
            .build()

        val result = (imageLoader.execute(request) as? SuccessResult)?.drawable ?: return null

        val size = 128 // px, adjust marker size
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = Rect(0, 0, size, size)

        // Draw circular mask
        val path = Path().apply {
            addCircle(size / 2f, size / 2f, size / 2.2f, Path.Direction.CCW)
        }
        canvas.clipPath(path)

        result.setBounds(0, 0, size, size)
        result.draw(canvas)

        // Optional: add white border
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        paint.strokeWidth = 6f
        canvas.drawCircle(size / 2f, size / 2f, size / 2.3f, paint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context, vectorResId)!!
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getFakeRoute(current: LatLng, destination: LatLng): List<LatLng> {
        val points = mutableListOf<LatLng>()
        val step = 0.001
        points.add(current)
        points.add(destination)
        return points

    }
}