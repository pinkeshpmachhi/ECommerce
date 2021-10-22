package com.p2m.ecommercefinal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MyAddressScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController
){
    myViewModel.getAddresses()
    val scroll = rememberScrollState()

    fun onClickFunction(title :String, measurements:String){
        myViewModel.titleForAddress = title
        myViewModel.addressesTF = measurements
        navController.navigate(Screen.EditAddressScreen.route) {
            launchSingleTop = true
        }
    }

    fun onFloatingClick(){
        myViewModel.titleForAddress = ""
        myViewModel.addressesTF = ""
        navController.navigate(Screen.EditAddressScreen.route){launchSingleTop = true}
    }

    Scaffold(
        floatingActionButton ={
            FloatingActionButton(
                onClick = {
                    onFloatingClick()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scroll, enabled = true),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "BackArrow",
//                        modifier = modifier.padding(10.dp)
//                    )
//                }

                Text(
                    text = "Your Addresses",
                    modifier = modifier.padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold
                )
            }


            myViewModel.listOfAddresses.forEach {

                Card(modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 20.dp, bottom = 10.dp, start = 12.dp, end = 12.dp)
                    .clickable {
                        onClickFunction(it.title!!, it.stuff!!)
                    },
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = colorResource(id = R.color.very_light_gray)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = it.title!!,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.Black,
                            modifier = modifier.padding(3.dp),
                            maxLines = 2
                        )
                        Text(
                            text = it.stuff!!,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray,
                            maxLines = 5
                        )

                    }
                }
            }


        }
    }
}