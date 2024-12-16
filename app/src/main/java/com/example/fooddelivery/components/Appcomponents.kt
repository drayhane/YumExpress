package com.example.fooddelivery.components
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.ui.theme.GreyText
import com.example.fooddelivery.ui.theme.MainOrange

// to define componenets

// normal text and titles of authentification pages

@Composable
fun NormaleTexte (value:String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(), // to  align it to the left
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,

        )
    )
}


@Composable
fun NormaleBoldTexte (value:String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(), // to  align it to the left
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        )
    )
}

@Composable
fun TitleTexte (value:String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(), // to  align it to the left
        style = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// text fields
fun MyTextField (labelValue:String){
    val textValue = remember{
        mutableStateOf("") // the last value entered by the user will be remembered
    }

    OutlinedTextField(
        label = {Text(text = labelValue,color = Color.Gray)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = Color.LightGray,
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Black, // Black cursor
        ),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun passwordTextField (labelValue:String){
    val password = remember{
        mutableStateOf("") // the last value entered by the user will be remembered
    }

    val passwordVisible = remember {
        mutableStateOf(false) // password not visivle initially
    }
    OutlinedTextField(
        label = {Text(text = labelValue,color = Color.Gray)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = Color.LightGray,
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Black, // Black cursor
        ),
        value = password.value,
        onValueChange = {
            password.value = it
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),

        // to toggle the password
        trailingIcon={
            val iconImage = if (passwordVisible.value){
                Icons.Filled.Visibility
            }else{
                Icons.Filled.VisibilityOff
            }
            val description = if(passwordVisible.value){
                "Hide password"
            }else{
                "Show password"
            }

            IconButton(onClick = {passwordVisible.value = !passwordVisible.value }){
                Icon(imageVector = iconImage , contentDescription = description)

            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else
        PasswordVisualTransformation(),
    )

    Spacer(modifier = Modifier.height(12.dp))


}

