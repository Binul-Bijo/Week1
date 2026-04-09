package com.example.travelleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ConverterHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                JourneyConverterScreen()
            }
        }
    }
}

private enum class ConversionGroup {
    MONEY,
    TRAVEL_MEASURES,
    HEAT
}

@Composable
fun JourneyConverterScreen() {
    val appContext = LocalContext.current

    val groupLabels = mapOf(
        ConversionGroup.MONEY to "Currency",
        ConversionGroup.TRAVEL_MEASURES to "Fuel / Distance / Liquid",
        ConversionGroup.HEAT to "Temperature"
    )

    val moneyOptions = listOf("USD", "AUD", "EUR", "JPY", "GBP")
    val travelOptions = listOf("mpg", "km/L", "Gallon (US)", "Liters", "Nautical Mile", "Kilometers")
    val heatOptions = listOf("Celsius", "Fahrenheit", "Kelvin")

    var activeGroup by remember { mutableStateOf(ConversionGroup.MONEY) }
    var sourceUnit by remember { mutableStateOf(moneyOptions.first()) }
    var targetUnit by remember { mutableStateOf(moneyOptions[1]) }
    var rawInput by remember { mutableStateOf("") }
    var conversionMessage by remember { mutableStateOf("Converted value will be shown here") }

    val visibleUnits = when (activeGroup) {
        ConversionGroup.MONEY -> moneyOptions
        ConversionGroup.TRAVEL_MEASURES -> travelOptions
        ConversionGroup.HEAT -> heatOptions
    }

    LaunchedEffect(activeGroup) {
        sourceUnit = visibleUnits.first()
        targetUnit = visibleUnits.getOrElse(1) { visibleUnits.first() }
        rawInput = ""
        conversionMessage = "Converted value will be shown here"
    }

    val accent = Color(0xFF009688)

    val pageBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7F3FF),
            Color(0xFFF2F8FF),
            Color(0xFFFFFAF4)
        )
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(pageBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.Top
            ) {
                TopBanner(accentColor = accent)

                Spacer(modifier = Modifier.height(18.dp))

                InputSection(
                    groupNames = groupLabels,
                    currentGroup = activeGroup,
                    availableUnits = visibleUnits,
                    fromUnit = sourceUnit,
                    toUnit = targetUnit,
                    inputText = rawInput,
                    accentColor = accent,
                    onGroupChange = { activeGroup = it },
                    onFromUnitChange = { sourceUnit = it },
                    onToUnitChange = { targetUnit = it },
                    onInputChange = { rawInput = it },
                    onConvertClicked = {
                        conversionMessage = performConversion(
                            context = appContext,
                            category = groupLabels[activeGroup] ?: "Currency",
                            fromUnit = sourceUnit,
                            toUnit = targetUnit,
                            typedValue = rawInput,
                            onError = { Toast.makeText(appContext, it, Toast.LENGTH_SHORT).show() }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                ResultSection(
                    accentColor = accent,
                    message = conversionMessage
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoSection()
            }
        }
    }
}

@Composable
private fun TopBanner(accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 14.dp,
            topEnd = 30.dp,
            bottomStart = 30.dp,
            bottomEnd = 14.dp
        ),
        colors = CardDefaults.cardColors(containerColor = accentColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Journey Converter",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Convert travel-related values quickly and clearly",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun InputSection(
    groupNames: Map<ConversionGroup, String>,
    currentGroup: ConversionGroup,
    availableUnits: List<String>,
    fromUnit: String,
    toUnit: String,
    inputText: String,
    accentColor: Color,
    onGroupChange: (ConversionGroup) -> Unit,
    onFromUnitChange: (String) -> Unit,
    onToUnitChange: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onConvertClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            LabelText("Category")
            SimpleDropdown(
                items = groupNames.values.toList(),
                selectedItem = groupNames[currentGroup] ?: "Currency",
                accentColor = accentColor,
                onItemSelected = { selectedLabel ->
                    val chosenGroup = groupNames.entries.firstOrNull { it.value == selectedLabel }?.key
                    if (chosenGroup != null) onGroupChange(chosenGroup)
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            LabelText("Convert From")
            SimpleDropdown(
                items = availableUnits,
                selectedItem = fromUnit,
                accentColor = accentColor,
                onItemSelected = onFromUnitChange
            )

            Spacer(modifier = Modifier.height(14.dp))

            LabelText("Convert To")
            SimpleDropdown(
                items = availableUnits,
                selectedItem = toUnit,
                accentColor = accentColor,
                onItemSelected = onToUnitChange
            )

            Spacer(modifier = Modifier.height(14.dp))

            LabelText("Input")
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Enter numeric value") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color(0xFFD3D3D3),
                    cursorColor = accentColor,
                    focusedLabelColor = accentColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onConvertClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = "Convert Now",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ResultSection(
    accentColor: Color,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Result",
                color = accentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
        }
    }
}

@Composable
private fun InfoSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About This App",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "This converter supports common travel calculations such as currency exchange, fuel and distance conversions, liquid volume conversion, and temperature conversion.",
                fontSize = 13.sp,
                color = Color(0xFF585858)
            )
        }
    }
}

@Composable
private fun LabelText(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color(0xFF4C4C4C),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDropdown(
    items: List<String>,
    selectedItem: String,
    accentColor: Color,
    onItemSelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = Color(0xFFD3D3D3),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = accentColor
            )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

private fun performConversion(
    context: android.content.Context,
    category: String,
    fromUnit: String,
    toUnit: String,
    typedValue: String,
    onError: (String) -> Unit
): String {
    val cleanedInput = typedValue.trim()

    if (cleanedInput.isEmpty()) {
        onError("Please enter a value")
        return "Error: input is empty"
    }

    val amount = cleanedInput.toDoubleOrNull()
    if (amount == null) {
        onError("Please enter a valid number")
        return "Error: invalid numeric input"
    }

    if (fromUnit == toUnit) {
        onError("Both units are the same")
        return "Converted Value: %.2f %s".format(amount, toUnit)
    }

    if ((category == "Currency" || category == "Fuel / Distance / Liquid") && amount < 0) {
        onError("Negative values are not allowed here")
        return "Error: negative values are not valid for $category"
    }

    val output = when (category) {
        "Currency" -> exchangeCurrency(fromUnit, toUnit, amount)
        "Fuel / Distance / Liquid" -> convertTravelMeasure(fromUnit, toUnit, amount)
        "Temperature" -> convertHeatValue(fromUnit, toUnit, amount)
        else -> null
    }

    return if (output == null) {
        Toast.makeText(context, "Conversion not supported", Toast.LENGTH_SHORT).show()
        "Error: unsupported conversion"
    } else {
        "Converted Value: %.2f %s".format(output, toUnit)
    }
}

private fun exchangeCurrency(from: String, to: String, amount: Double): Double {
    val baseUsd = when (from) {
        "USD" -> amount
        "AUD" -> amount / 1.55
        "EUR" -> amount / 0.92
        "JPY" -> amount / 148.50
        "GBP" -> amount / 0.78
        else -> amount
    }

    return when (to) {
        "USD" -> baseUsd
        "AUD" -> baseUsd * 1.55
        "EUR" -> baseUsd * 0.92
        "JPY" -> baseUsd * 148.50
        "GBP" -> baseUsd * 0.78
        else -> baseUsd
    }
}

private fun convertTravelMeasure(from: String, to: String, amount: Double): Double? {
    return when {
        from == "mpg" && to == "km/L" -> amount * 0.425
        from == "km/L" && to == "mpg" -> amount / 0.425
        from == "Gallon (US)" && to == "Liters" -> amount * 3.785
        from == "Liters" && to == "Gallon (US)" -> amount / 3.785
        from == "Nautical Mile" && to == "Kilometers" -> amount * 1.852
        from == "Kilometers" && to == "Nautical Mile" -> amount / 1.852
        else -> null
    }
}

private fun convertHeatValue(from: String, to: String, amount: Double): Double? {
    return when {
        from == "Celsius" && to == "Fahrenheit" -> (amount * 1.8) + 32
        from == "Fahrenheit" && to == "Celsius" -> (amount - 32) / 1.8
        from == "Celsius" && to == "Kelvin" -> amount + 273.15
        from == "Kelvin" && to == "Celsius" -> amount - 273.15
        from == "Fahrenheit" && to == "Kelvin" -> ((amount - 32) / 1.8) + 273.15
        from == "Kelvin" && to == "Fahrenheit" -> ((amount - 273.15) * 1.8) + 32
        else -> null
    }
}