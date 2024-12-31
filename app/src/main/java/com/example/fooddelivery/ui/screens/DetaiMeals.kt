package com.example.fooddelivery.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.item
import com.example.fooddelivery.domain.respository.ItemRespository
import com.example.fooddelivery.domain.respository.ItemRespositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


@Composable
fun Displaymeal(navController: NavHostController, ItemId: String) {
    val ItemRespository: ItemRespository = ItemRespositoryImpl()

    val item: item = runBlocking(Dispatchers.IO) {
        ItemRespository.getItemById(ItemId)!!
    }
    var selectedSize by remember { mutableStateOf("Simple") }
    val selectedSauces = remember { mutableStateListOf<String>() }
    var quantity by remember { mutableStateOf(1) }
    var notes by remember { mutableStateOf(TextFieldValue("")) } // Variable d'état pour stocker la valeur du champ

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Image
        Image(
            painter = painterResource(id = R.drawable.burger), // Remplacez par l'image correcte
            contentDescription = "Item Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title and Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            item.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                text = "${item.price} DA",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFFF640D) // Orange
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredients
        Text(text = "Ingredients", style = MaterialTheme.typography.titleMedium)
        item.ingredient?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Divider(modifier = Modifier.padding(vertical = 6.dp))

        Spacer(modifier = Modifier.height(6.dp))

        val sizeOptions = mapOf(
            "Burger" to listOf("Simple", "Double", "Mega"),
            "Tacos" to listOf("M", "L", "XL"),
            "Pizza" to listOf("M", "L", "XL") // Même options que Tacos
        )

        Text(text = "Size", style = MaterialTheme.typography.titleMedium)
        Text(text = "Select the sizing of your item.", style = MaterialTheme.typography.bodyMedium)

// Vérifiez si le type de l'item est dans la liste des options
        if (!item.Type.isNullOrEmpty()) {
            when (item.Type) {
                in sizeOptions.keys -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Affiche les tailles en ligne horizontale
                        sizeOptions[item.Type]?.forEach { size ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedSize == size,
                                    onClick = { selectedSize = size }
                                )
                                Text(text = size)
                                Spacer(modifier = Modifier.width(16.dp)) // Espacement entre les options
                            }
                        }
                    }
                }

            }}
        Spacer(modifier = Modifier.height(6.dp))

        Divider(modifier = Modifier.padding(vertical = 6.dp))

        Spacer(modifier = Modifier.height(6.dp))
        // Sauce options
        Text(text = "Sauces", style = MaterialTheme.typography.titleMedium)
        Text(text = "Select up to 3 sauces", style = MaterialTheme.typography.bodyMedium)
        Column {
            listOf("Mayonnaise sauce", "Spicy sauce", "BBQ sauce").forEach { sauce ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedSauces.contains(sauce),
                        onCheckedChange = {
                            if (selectedSauces.contains(sauce)) {
                                selectedSauces.remove(sauce)
                            } else if (selectedSauces.size < 3) {
                                selectedSauces.add(sauce)
                            }
                        }
                    )
                    Text(text = sauce)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Divider(modifier = Modifier.padding(vertical = 6.dp))

        Spacer(modifier = Modifier.height(6.dp))
        // Additional notes
        Text(text = "Additional notes", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp)) // Bordure noire avec coins arrondis
                .padding(8.dp) // Espacement intérieur entre la bordure et le texte
        ) {
            BasicTextField(
                value = notes, // TextFieldValue contenant l'état du champ
                onValueChange = { newValue -> notes = newValue }, // Fonction pour gérer les changements
                modifier = Modifier.fillMaxWidth(), // Remplit l'espace disponible
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default, // Options de saisie clavier
                keyboardActions = KeyboardActions.Default, // Actions clavier
                singleLine = true, // Si c'est un champ en ligne unique
                visualTransformation = VisualTransformation.None, // Transformation visuelle (si nécessaire)
                interactionSource = remember { MutableInteractionSource() } // Gestion de l'interaction
            )
        }







        Spacer(modifier = Modifier.height(6.dp))

        Divider(modifier = Modifier.padding(vertical = 6.dp))

        Spacer(modifier = Modifier.height(6.dp))
        // Quantity and Add to Cart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // First Row: Quantity controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)) // Coins arrondis
                    .background(Color(0xFFFF640D)) // Couleur orange
                    .height(56.dp) // Hauteur définie pour correspondre au bouton
                    .padding(horizontal = 12.dp) // Espacement intérieur
            ) {
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.size(40.dp) // Taille cohérente des boutons
                ) {
                    Text(
                        text = "-",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp) // Espacement autour du texte
                )

                IconButton(
                    onClick = { quantity++ },
                    modifier = Modifier.size(40.dp) // Taille cohérente des boutons
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp) // Taille de l'icône
                    )
                }
            }

            // Second Row: Add to Cart Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp) // Padding autour du contenu
                    .clip(RoundedCornerShape(10.dp)) // Coins arrondis
                    .background(Color(0xFF1F1F1F)) // Couleur orange
            ) {Button(
                onClick = { /* Ajout au panier */ },
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F1F1F), // Fond noir
                    contentColor = Color.White // Texte blanc
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.panier), // Remplacez par l'image correcte
                    contentDescription = "Item Image",
                    colorFilter = ColorFilter.tint(Color.White), // Applique la couleur blanche
                    modifier = Modifier
                        .size(24.dp) // Définit la taille
                )

                Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte

                Text(
                    text = "Add to cart",
                    fontSize = 17.sp, // Texte légèrement plus grand
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                }}
            }


        }
}
