package com.eatfair.app.data.repo

import com.eatfair.app.data.dao.AddressDao
import com.eatfair.app.model.address.AddressDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepo @Inject constructor(
    private val addressDao: AddressDao
) {

    fun getAddresses(): Flow<List<AddressDto>> = addressDao.getAllAddresses()

    suspend fun getAddressById(id: Int): AddressDto? = addressDao.getAddressById(id)

    suspend fun insertAddress(addressDto: AddressDto) {
        addressDao.insertAddress(addressDto)
    }

    suspend fun updateAddress(addressDto: AddressDto) {
        addressDao.updateAddress(addressDto)
    }

    suspend fun deleteAddress(addressDto: AddressDto) {
        addressDao.deleteAddress(addressDto)
    }

    suspend fun getDefaultAddress(): AddressDto? {
        return addressDao.getDefaultAddress()
    }
}