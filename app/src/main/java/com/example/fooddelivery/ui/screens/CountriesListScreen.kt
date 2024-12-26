import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun CountriesListScreen(getCountriesUseCase: GetCountriesUseCase) {
    var countries by remember { mutableStateOf<List<Country>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Charger les données avec une coroutine
    LaunchedEffect(Unit) {
        try {
            countries = getCountriesUseCase()  // Utilisation du cas d'utilisation
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    // Afficher l'UI
    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (errorMessage != null) {
            Text("Error: $errorMessage", modifier = Modifier.fillMaxWidth())
        } else if (countries != null) {
            LazyColumn {
                items(countries!!) { country ->
                    CountryItem(country = country)
                }
            }
        }
    }
}

@Composable
fun CountryItem(country: Country) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "ID: ${country.id}")
        Text(text = "Name: ${country.name}")
    }
}
