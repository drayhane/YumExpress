package com.example.fooddelivery.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.R

@Composable
fun OfferFood() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
    ) {

        Image(
            painter = painterResource(id = com.example.fooddelivery.R.drawable.offer),
            contentDescription = "Offer Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(0.9f)
                .fillMaxWidth(0.95f)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp))
        )


        Column(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .fillMaxWidth(0.95f)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Special Offer For March",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp, // Taille dynamique
                modifier = Modifier.padding(start = 16.dp),
                maxLines = 2, // Limiter à 1 ligne
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "We are here with the best desserts in town",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp),
                maxLines = 2, // Limiter à 1 ligne
                overflow = TextOverflow.Ellipsis
            )
            // Bouton "Buy Now" avec bords arrondis
            Button(
                onClick = { /* Action ici */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFFF640D)                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = "Buy Now",
                    fontSize = 14.sp
                )
            }
        }
    }
}

