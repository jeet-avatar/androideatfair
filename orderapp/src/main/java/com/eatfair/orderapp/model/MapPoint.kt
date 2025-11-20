package com.eatfair.orderapp.model

import com.google.android.gms.maps.model.LatLng

data class MapPoint(
    val title: String,
    val address: String,
    val latLng: LatLng,
    val imageRes: Int? = null, // for local drawable
    val imageUrl: String? = null, // for remote image
    val isProfile: Boolean = false
)