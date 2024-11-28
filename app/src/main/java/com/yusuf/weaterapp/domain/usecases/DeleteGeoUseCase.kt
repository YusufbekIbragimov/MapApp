package com.yusuf.weaterapp.domain.usecases

import com.yusuf.weaterapp.domain.model.GeoModel
import com.yusuf.weaterapp.domain.model.toEntity
import com.yusuf.weaterapp.domain.repository.LocalRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class DeleteGeoUseCase @Inject constructor(
    private val localCardRepository: LocalRepository
) {
    suspend operator fun invoke(geoModel: GeoModel) {
        return withContext(Dispatchers.IO) {
            localCardRepository.deleteGeo(geoModel.toEntity())
        }
    }
}