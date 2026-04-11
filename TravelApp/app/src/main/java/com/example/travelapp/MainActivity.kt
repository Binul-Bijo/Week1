package com.example.travelapp
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ModernTravelCompanionApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTravelCompanionApp() {
    val context = LocalContext.current

    val categories = listOf(
        "Currency",
        "Fuel / Distance / Liquid",
        "Temperature"
    )

    val currencyUnits = listOf("USD", "AUD", "EUR", "JPY", "GBP")
    val fuelUnits = listOf("mpg", "km/L", "Gallon (US)", "Liters", "Nautical Mile", "Kilometers")
    val temperatureUnits = listOf("Celsius", "Fahrenheit", "Kelvin")

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedFromUnit by remember { mutableStateOf(currencyUnits[0]) }
    var selectedToUnit by remember { mutableStateOf(currencyUnits[1]) }
    var inputValue by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Converted value will appear here") }

    val currentUnits = when (selectedCategory) {
        "Currency" -> currencyUnits
        "Fuel / Distance / Liquid" -> fuelUnits
        "Temperature" -> temperatureUnits
        else -> currencyUnits
    }

    LaunchedEffect(selectedCategory) {
        selectedFromUnit = currentUnits.first()
        selectedToUnit = if (currentUnits.size > 1) currentUnits[1] else currentUnits.first()
        inputValue = ""
        resultText = "Converted value will appear here"
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F7FF),
            Color(0xFFEAF4FF),
            Color(0xFFFDFBFF)
        )
    )

    val accentColor = when (selectedCategory) {
        "Currency" -> Color(0xFF3F51B5)
        "Fuel / Distance / Liquid" -> Color(0xFF00897B)
        "Temperature" -> Color(0xFFE65100)
        else -> Color(0xFF3F51B5)
    }

    val categoryIcon = when (selectedCategory) {
        "Currency" -> "💱"
        "Fuel / Distance / Liquid" -> "⛽"
        "Temperature" -> "🌡️"
        else -> "✈️"
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        Text(
                            text = "$categoryIcon Travel Companion",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C1C1C)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Smart travel conversions for international travellers",
                            fontSize = 15.sp,
                            color = Color(0xFF5F6368)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    color = accentColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Current category: $selectedCategory",
                                color = accentColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        SectionLabel("Choose category")
                        ModernDropdownSelector(
                            options = categories,
                            selectedOption = selectedCategory,
                            onOptionSelected = { selectedCategory = it },
                            accentColor = accentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionLabel("Convert from")
                        ModernDropdownSelector(
                            options = currentUnits,
                            selectedOption = selectedFromUnit,
                            onOptionSelected = { selectedFromUnit = it },
                            accentColor = accentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionLabel("Convert to")
                        ModernDropdownSelector(
                            options = currentUnits,
                            selectedOption = selectedToUnit,
                            onOptionSelected = { selectedToUnit = it },
                            accentColor = accentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionLabel("Enter value")
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Type value here") },
                            shape = RoundedCornerShape(18.dp),
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = {
                                val trimmedInput = inputValue.trim()

                                if (trimmedInput.isEmpty()) {
                                    Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                                    resultText = "Error: Input cannot be empty"
                                    return@Button
                                }

                                val numericValue = trimmedInput.toDoubleOrNull()
                                if (numericValue == null) {
                                    Toast.makeText(context, "Please enter a valid numeric value", Toast.LENGTH_SHORT).show()
                                    resultText = "Error: Non-numeric input is not allowed"
                                    return@Button
                                }

                                if (selectedFromUnit == selectedToUnit) {
                                    Toast.makeText(context, "Source and destination are the same", Toast.LENGTH_SHORT).show()
                                    resultText = "Result: %.2f %s".format(numericValue, selectedToUnit)
                                    return@Button
                                }

                                if ((selectedCategory == "Currency" || selectedCategory == "Fuel / Distance / Liquid") && numericValue < 0) {
                                    Toast.makeText(context, "Negative values are not allowed for this category", Toast.LENGTH_SHORT).show()
                                    resultText = "Error: Negative value is invalid for $selectedCategory"
                                    return@Button
                                }

                                val result = when (selectedCategory) {
                                    "Currency" -> convertCurrency(selectedFromUnit, selectedToUnit, numericValue)
                                    "Fuel / Distance / Liquid" -> convertFuelDistanceLiquid(selectedFromUnit, selectedToUnit, numericValue)
                                    "Temperature" -> convertTemperature(selectedFromUnit, selectedToUnit, numericValue)
                                    else -> null
                                }

                                if (result == null) {
                                    Toast.makeText(context, "This conversion is not supported", Toast.LENGTH_SHORT).show()
                                    resultText = "Error: Conversion not supported"
                                } else {
                                    resultText = "Result: %.2f %s".format(result, selectedToUnit)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Convert Now",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = accentColor.copy(alpha = 0.10f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = accentColor.copy(alpha = 0.25f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Conversion Result",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = resultText,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Quick Tips",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1C1C1C)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• Currency and fuel values cannot be negative\n" +
                                    "• Same-unit conversion returns the same value\n" +
                                    "• Unsupported conversions show an error safely",
                            fontSize = 14.sp,
                            color = Color(0xFF5F6368),
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF4B5563),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernDropdownSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    accentColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(18.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onOptionSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertCurrency(from: String, to: String, value: Double): Double {
    val usdValue = when (from) {
        "USD" -> value
        "AUD" -> value / 1.55
        "EUR" -> value / 0.92
        "JPY" -> value / 148.50
        "GBP" -> value / 0.78
        else -> value
    }

    return when (to) {
        "USD" -> usdValue
        "AUD" -> usdValue * 1.55
        "EUR" -> usdValue * 0.92
        "JPY" -> usdValue * 148.50
        "GBP" -> usdValue * 0.78
        else -> usdValue
    }
}

fun convertFuelDistanceLiquid(from: String, to: String, value: Double): Double? {
    return when {
        from == "mpg" && to == "km/L" -> value * 0.425
        from == "km/L" && to == "mpg" -> value / 0.425
        from == "Gallon (US)" && to == "Liters" -> value * 3.785
        from == "Liters" && to == "Gallon (US)" -> value / 3.785
        from == "Nautical Mile" && to == "Kilometers" -> value * 1.852
        from == "Kilometers" && to == "Nautical Mile" -> value / 1.852
        else -> null
    }
}

fun convertTemperature(from: String, to: String, value: Double): Double? {
    return when {
        from == "Celsius" && to == "Fahrenheit" -> (value * 1.8) + 32
        from == "Fahrenheit" && to == "Celsius" -> (value - 32) / 1.8
        from == "Celsius" && to == "Kelvin" -> value + 273.15
        from == "Kelvin" && to == "Celsius" -> value - 273.15
        from == "Fahrenheit" && to == "Kelvin" -> ((value - 32) / 1.8) + 273.15
        from == "Kelvin" && to == "Fahrenheit" -> ((value - 273.15) * 1.8) + 32
        else -> null
    }
}
