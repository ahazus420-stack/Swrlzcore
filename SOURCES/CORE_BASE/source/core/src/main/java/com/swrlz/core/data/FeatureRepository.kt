package com.swrlz.core.data

import com.swrlz.core.domain.CoreFeature

interface FeatureRepository {
    fun getFeatures(): List<CoreFeature>
}

class FeatureRepositoryImpl : FeatureRepository {
    override fun getFeatures(): List<CoreFeature> {
        return listOf(
            CoreFeature("foundation", "Foundation", "Reusable Android foundation for SWRLZ apps"),
            CoreFeature("compose", "Compose", "Composable UI layer for fast iteration"),
            CoreFeature("architecture", "Architecture", "Modular and testable core services")
        )
    }
}
