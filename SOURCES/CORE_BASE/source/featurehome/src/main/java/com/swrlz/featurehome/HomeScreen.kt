package com.swrlz.featurehome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swrlz.core.data.FeatureRepositoryImpl
import com.swrlz.designsystem.SwrlzTheme

@Composable
fun HomeScreen() {
    SwrlzTheme {
        val features = FeatureRepositoryImpl().getFeatures()
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("SWRLZ Core", style = MaterialTheme.typography.headlineMedium)
            Text("Reusable Android foundation", style = MaterialTheme.typography.bodyMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(features) { feature ->
                    Text("• ${feature.title}: ${feature.description}")
                }
            }
        }
    }
}
