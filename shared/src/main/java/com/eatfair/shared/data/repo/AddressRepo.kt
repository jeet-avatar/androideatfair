package com.eatfair.shared.data.repo

import com.eatfair.shared.data.dao.AddressDao
import com.eatfair.shared.data.local.SessionManager
import com.eatfair.shared.model.address.AddressDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepo @Inject constructor(
    private val addressDao: AddressDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) {

    fun getAddresses(): Flow<List<AddressDto>> = addressDao.getAllAddresses()

    suspend fun getAddressById(id: Int): AddressDto? = addressDao.getAddressById(id)

    suspend fun insertAddress(addressDto: AddressDto) {
        // Save to local database
        addressDao.insertAddress(addressDto)
        
        // Also save to Firestore
        try {
            val userId = sessionManager.getUserId() ?: return
            val addressData = hashMapOf(
                "id" to addressDto.id,
                "locationName" to addressDto.locationName,
                "completeAddress" to addressDto.completeAddress,
                "houseNumber" to addressDto.houseNumber,
                "apartmentRoad" to addressDto.apartmentRoad,
                "directions" to addressDto.directions,
                "type" to addressDto.type.name,
                "latitude" to addressDto.latitude,
                "longitude" to addressDto.longitude,
                "phoneNumber" to addressDto.phoneNumber,
                "isDefault" to addressDto.isDefault,
                "createdAt" to FieldValue.serverTimestamp()
            )
            
            firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(addressDto.id.toString())
                .set(addressData)
                .await()
        } catch (e: Exception) {
            println("Error saving address to Firestore: ${e.message}")
        }
    }

    suspend fun updateAddress(addressDto: AddressDto) {
        // Update in local database
        addressDao.updateAddress(addressDto)
        
        // Also update in Firestore
        try {
            val userId = sessionManager.getUserId() ?: return
            val addressData = hashMapOf(
                "locationName" to addressDto.locationName,
                "completeAddress" to addressDto.completeAddress,
                "houseNumber" to addressDto.houseNumber,
                "apartmentRoad" to addressDto.apartmentRoad,
                "directions" to addressDto.directions,
                "type" to addressDto.type.name,
                "latitude" to addressDto.latitude,
                "longitude" to addressDto.longitude,
                "phoneNumber" to addressDto.phoneNumber,
                "isDefault" to addressDto.isDefault,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            
            firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(addressDto.id.toString())
                .update(addressData as Map<String, Any>)
                .await()
        } catch (e: Exception) {
            println("Error updating address in Firestore: ${e.message}")
        }
    }

    suspend fun deleteAddress(addressDto: AddressDto) {
        // Delete from local database
        addressDao.deleteAddress(addressDto)
        
        // Also delete from Firestore
        try {
            val userId = sessionManager.getUserId() ?: return
            firestore.collection("users")
                .document(userId)
                .collection("addresses")
                .document(addressDto.id.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            println("Error deleting address from Firestore: ${e.message}")
        }
    }

    suspend fun getDefaultAddress(): AddressDto? {
        return addressDao.getDefaultAddress()
    }
}