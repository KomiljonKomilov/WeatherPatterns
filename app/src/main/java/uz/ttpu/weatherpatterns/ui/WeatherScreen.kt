package uz.ttpu.weatherpatterns.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.mvc.view.MvcActivity
import uz.ttpu.weatherpatterns.mvp.view.MvpActivity
import uz.ttpu.weatherpatterns.mvvm.view.MvvmActivity
import uz.ttpu.weatherpatterns.mvi.view.MviActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    patternTitle: String,
    isLoading: Boolean,
    weather: Weather?,
    errorMessage: String?,
    onFetchWeather: (String) -> Unit,
    onBackClick: (() -> Unit)? = null,
    onSwitchPattern: ((Class<*>) -> Unit)? = null
) {
    var selectedCity by remember { mutableStateOf("Tashkent") }
    val quickCities = listOf("Tashkent", "Samarkand", "Bukhara", "Atlantis")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(patternTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    if (onSwitchPattern != null) {
                        var menuExpanded by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { menuExpanded = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text("Switch Pattern ▾", fontWeight = FontWeight.Bold)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("1. MVC Pattern") },
                                onClick = {
                                    menuExpanded = false
                                    onSwitchPattern(MvcActivity::class.java)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("2. MVP Pattern") },
                                onClick = {
                                    menuExpanded = false
                                    onSwitchPattern(MvpActivity::class.java)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("3. MVVM Pattern") },
                                onClick = {
                                    menuExpanded = false
                                    onSwitchPattern(MvvmActivity::class.java)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("4. MVI Pattern") },
                                onClick = {
                                    menuExpanded = false
                                    onSwitchPattern(MviActivity::class.java)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (onSwitchPattern != null) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Switch Architecture Pattern:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AssistChip(
                                onClick = { onSwitchPattern(MvcActivity::class.java) },
                                label = { Text("MVC") }
                            )
                            AssistChip(
                                onClick = { onSwitchPattern(MvpActivity::class.java) },
                                label = { Text("MVP") }
                            )
                            AssistChip(
                                onClick = { onSwitchPattern(MvvmActivity::class.java) },
                                label = { Text("MVVM") }
                            )
                            AssistChip(
                                onClick = { onSwitchPattern(MviActivity::class.java) },
                                label = { Text("MVI") }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // City Input Box
            OutlinedTextField(
                value = selectedCity,
                onValueChange = { selectedCity = it },
                label = { Text("Enter City Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Quick Selection Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                quickCities.forEach { city ->
                    FilterChip(
                        selected = (selectedCity.lowercase() == city.lowercase()),
                        onClick = {
                            selectedCity = city
                            onFetchWeather(city)
                        },
                        label = { Text(city) }
                    )
                }
            }

            // Fetch Button
            Button(
                onClick = { onFetchWeather(selectedCity) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fetch Weather")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading / Content / Error State Display
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Text(
                        text = "Loading weather for $selectedCity...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                errorMessage != null -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                weather != null -> {
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = weather.city,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${weather.temperatureC}°C",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = weather.condition,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Select or type a city and press 'Fetch Weather'",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
