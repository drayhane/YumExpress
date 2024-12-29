import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.data.model.Restaurant
import kotlinx.coroutines.launch


@Composable
fun CountriesListScreen(getRestoUsecase: GetRestoUsecase) {
    var countries by remember { mutableStateOf<List<Restaurant>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Charger les données avec une coroutine
    LaunchedEffect(Unit) {
        try {
            countries = getRestoUsecase()  // Utilisation du cas d'utilisation
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
                items(countries!!) { resto ->
                    CountryItem(resto = resto)
                }
            }
        }
    }
}

@Composable
fun CountryItem(resto: Restaurant) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "ID: ${resto.id_restaurant}")
        Text(text = "Name: ${resto.name}")
    }
}