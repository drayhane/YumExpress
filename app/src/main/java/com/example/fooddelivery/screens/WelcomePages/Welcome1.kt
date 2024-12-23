package com.example.fooddelivery.screens.WelcomePages


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.R
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.components.ControllerText
import com.example.fooddelivery.components.NormaleTexteGreyCenter
import com.example.fooddelivery.components.TitleTexteCenter
import kotlinx.coroutines.launch


@Composable
fun Welcome1(navController: NavController) {
    // State to manage the pager
    val pagerState = rememberPagerState(0,0F,
        ){3}

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize() // to cover the whole screen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Makes the Column fill the entire screen
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        ) {

            // The pager with 3 pages (slides)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                // Pass the page number to customize the content for each slide
                SlideContent(page)
            }

            DotIndicators(
                totalSlides = 3,
                currentPage = pagerState.currentPage
            )

                Spacer(modifier = Modifier.height(32.dp))
                NextButton("Next", pagerState ,navController)

            Spacer(modifier = Modifier.height(15.dp))
           // ButtonComponentWhite("Skip", navController, "LogIn")
            ControllerText("Skip",navController,"LogIn")// take to home page
        }
    }
}

@Composable
fun SlideContent(page: Int) {
    // Customize the content for each page
    when (page) {
        0 -> {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.width(190.dp)
                )
                Spacer(modifier = Modifier.height(80.dp))
                Image(
                    painter = painterResource(id = R.drawable.food),
                    contentDescription = "Food",
                    modifier = Modifier.width(250.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                TitleTexteCenter("Discover new tastes")
                Spacer(modifier = Modifier.height(5.dp))
                NormaleTexteGreyCenter("Explore a world of flavors at your fingertips. \n Craving something delicious? We've got it covered!")

            }

        }
        1 -> {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.width(190.dp)
                )
                Spacer(modifier = Modifier.height(100.dp))
                Image(
                    painter = painterResource(id = R.drawable.delivery),
                    contentDescription = "Food",
                    modifier = Modifier.width(250.dp)
                        .height(250.dp)
                )


                TitleTexteCenter("Discover new tastes")
                Spacer(modifier = Modifier.height(5.dp))
                NormaleTexteGreyCenter("Explore a world of flavors at your fingertips. \n Craving something delicious? We've got it covered!")

            }

        }
        2 -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.width(190.dp)
                )
                Spacer(modifier = Modifier.height(90.dp))
                Image(
                    painter = painterResource(id = R.drawable.phoneorder),
                    contentDescription = "Food",
                    modifier = Modifier.width(170.dp)
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))
                TitleTexteCenter("Easy Ordering")
                Spacer(modifier = Modifier.height(5.dp))
                NormaleTexteGreyCenter("Order anytime, anywhere. Browse, choose, and enjoy – all with just a few taps!")
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun DotIndicators(
    totalSlides: Int,
    currentPage: Int,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.Gray,
    activeDotWidth: Dp = 24.dp,  // Wider active dot
    inactiveDotWidth: Dp = 10.dp, // Standard width for inactive dots
    dotHeight: Dp = 10.dp         // Uniform height for all dots
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until totalSlides) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(if (i == currentPage) activeDotWidth else inactiveDotWidth) // Dynamic width
                    .height(dotHeight)
                    .clip(CircleShape)
                    .background(if (i == currentPage) activeColor else inactiveColor)
            )
        }
    }
}

@Composable
fun NextButton(text: String, pagerState: PagerState,navController: NavController) {
    // Remember a coroutine scope to handle scrolling
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                // Animate to the next page if not on the last page
                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
                else {
                    // Navigate to the login page if on the last page
                    navController.navigate("LogIn") // Adjust the destination as per your route
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
            .heightIn(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
