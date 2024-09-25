package com.example.calories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calories.ui.theme.CaloriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalorieScreen()
                }
            }
        }
    }
}

@Composable
fun CalorieScreen() {
    // Tilamuuttujat painolle, sukupuolelle, intensiteetille ja tulokselle
    var weightInput by remember { mutableStateOf("") }
    val weight = weightInput.toIntOrNull() ?: 0

    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf(1.3f) }
    var result by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Otsikko
        Heading(title = "Calories")

        // Painon syöttökenttä
        WeightField(weight = weightInput, onValueChange = { weightInput = it })

        // Sukupuolen valinta radiopainikkeilla
        GenderChoices(male = male, setGenderMale = { male = it })

        // Aktiivisuustason valinta pudotusvalikolla
        IntensityList(onClick = { intensity = it })

        // Tuloksen näyttö
        Text(
            text = "Calories Burned: $result",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Laskentapainike
        Calculation(
            male = male,
            weight = weight,
            intensity = intensity,
            setResult = { result = it }
        )
    }
}

@Composable
fun Heading(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun WeightField(weight: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = weight,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Weight (kg)") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit) {
    Column(Modifier.selectableGroup()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = male,
                onClick = { setGenderMale(true) }
            )
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !male,
                onClick = { setGenderMale(false) }
            )
            Text(text = "Female")
        }
    }
}

@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very Hard")
    val intensities = listOf(1.3f, 1.55f, 1.7f, 2.0f, 2.3f)

    Box {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Activity Level") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        selectedText = label
                        onClick(intensities[index])
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Calculation(male: Boolean, weight: Int, intensity: Float, setResult: (Int) -> Unit) {
    Button(
        onClick = {
            val baseCalories = if (male) {
                879 + 16.2 * weight
            } else {
                795 + 7.18 * weight
            }
            setResult((baseCalories * intensity).toInt())
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "CALCULATE")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CaloriesTheme {
        CalorieScreen()
    }
}
