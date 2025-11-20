package com.eatfair.shared.model.address

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import com.eatfair.shared.constants.AddressType

@Entity(tableName = "addresses")
data class AddressDto(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val locationName: String,

    val completeAddress: String, // from location.address

    val houseNumber: String,

    val apartmentRoad: String,

    val directions: String?,

    val type: AddressType,

    // Storing lat/lng for map integration
    val latitude: Double,

    val longitude: Double,

    val phoneNumber: String?,

    val isDefault: Boolean = false
) {
    fun addressLine1() = "$houseNumber $apartmentRoad"

    fun addressLine2() = completeAddress ?: ""
}
