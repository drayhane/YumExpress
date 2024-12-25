package com.example.fooddelivery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabItem(title: String, isSelected: Boolean, isFirst: Boolean) {
    Tab(
        selected = isSelected,
        onClick = { /* Handle click */ },
        modifier = Modifier
            .background(
                color = if (isSelected) Color(0xFFFF640D) else Color(0xFFFFF9F5),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color(0xFFFFCFB4),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}