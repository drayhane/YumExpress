package com.example.fooddelivery.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.theme.GreyText
import com.example.fooddelivery.ui.theme.MainBlack
import com.example.fooddelivery.ui.theme.MainOrange

// to define componenets

// normal text and titles of authentification pages

// normal black text
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
// normal grey text
@Composable
fun NormaleTexteGrey (value:String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(), // to  align it to the left
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = Color.Gray
            )
    )
}

@Composable
fun NormaleTexteGreyCenter (value:String){
    Text(
        text = value,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun LinkText(value: String, onClick: () -> Unit) {
    Text(
        text = value,
        modifier = Modifier
            .clickable(onClick = onClick), // Make the text clickable
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Normal,
            color = Color.Black // Optional: Change text color to indicate it's clickable
        )
    )
}

@Composable
fun ControllerText(value: String,navController: NavController, destination: String) {

    Text(
        text = value,
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,

            ),
        modifier = Modifier
            .fillMaxWidth() // Make the Text fill the available width
            .wrapContentWidth(Alignment.End) // Align the content to the end (right)
            .clickable {
                // Navigate to "SignUp2" screen
                navController.navigate(destination)
            }
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

@Composable
fun TitleTexteCenter (value:String){
    Text(
        text = value,
        style = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
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
                Icons.Outlined.Visibility
            }else{
                Icons.Outlined.VisibilityOff

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


}

// Black button with white text
@Composable
fun ButtonComponent(value: String,navController: NavController, destination: String){

    Button(
        onClick = {
            navController.navigate(destination)
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),

        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Black),
        shape = RoundedCornerShape(8.dp)

    ){
        Box (modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                color = MainBlack,
            ),
            contentAlignment = Alignment.Center)
        {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// White button with black text
@Composable
fun ButtonComponentWhite(value: String,navController: NavController, destination: String){

    Button(
        onClick = {
            navController.navigate(destination)
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),

        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(8.dp)

    ){
        Box (modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                color = MainBlack,
            ),
            contentAlignment = Alignment.Center)
        {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun OrSeparator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically // Align items vertically
    ) {
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f) // Divider takes up equal space
        )
        Text(
            text = "Or",
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp), // Space around the text
            fontWeight = FontWeight.Normal
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f) // Divider takes up equal space
        )
    }
}

@Composable
fun GoogleLoginIn(onLoginClick: () -> Unit)
{    // The parent Row for the Google login button
    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp)) // Rounded corners
            .background(Color.White) // White background
            .clickable(onClick = onLoginClick) // Handle login action
            .border(1.dp, Color.LightGray, RoundedCornerShape(32.dp)), // Light gray border
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Google Logo
        Image(
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = "Google Logo",
            modifier = Modifier
                .height(24.dp) // Set size for the logo
                .padding(end = 8.dp) // Add spacing between logo and text
        )

        // Text on the button
        Text(
            text = "Continue with Google",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Spacer(modifier = Modifier.height(12.dp))

}

// Don't have an account Sign up
@Composable
fun DontHaveAccount(navController: NavController)
{
    Row (){
        Text(
            text = "Don't have an account ?",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,

                )
        )

        Text(
            text = " Sign up",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,

                ),
                    modifier = Modifier
                    .clickable {
                // Navigate to "SignUp2" screen
                navController.navigate("SignUp2")
            }
        )
    }
}

// already have an account
@Composable
fun HaveAccount(navController: NavController)
{
    Row (){
        Text(
            text = "Already have an account ?",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,

                )
        )

        Text(
            text = " Log in",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,

                ),
            modifier = Modifier
                .clickable {
                    // Navigate to "SignUp2" screen
                    navController.navigate("LogIn")
                }
        )
    }
}


@Composable
fun BackArrowButton(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIos,
                contentDescription = "Back",
                tint = Color.Black // You can customize the color
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpTextField(
    otp: MutableList<String>, // La liste des valeurs de OTP
    onOtpChange: (Int, String) -> Unit // Callback pour mettre à jour l'OTP
) {
    // Crée des champs de texte pour chaque caractère du OTP
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Pour espacer les champs
        verticalAlignment = Alignment.CenterVertically // Center vertically in the row
    ) {
        otp.forEachIndexed { index, value ->
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (it.length <= 1) {
                        onOtpChange(index, it) // Met à jour l'OTP et vérifie la longueur
                    }
                },
                label = {},
                modifier = Modifier
                    .width(80.dp)
                    .height(90.dp)
                    .padding(4.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
                    .align(Alignment.CenterVertically), // Ensure it's centered vertically

                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Transparent border when focused
                    unfocusedBorderColor = Color.Transparent, // Transparent border when not focused
                    cursorColor = Color.Black // Set cursor color to black
                ),
                textStyle = TextStyle(
                    fontSize = 27.sp, // Text size inside the field
                    textAlign = TextAlign.Center // Center the text horizontally
                )
            )
        }
    }
}
