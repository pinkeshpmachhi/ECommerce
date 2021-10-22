package com.p2m.ecommercefinal

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditAddressScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController
){
    fun clearAllFunction(){
        myViewModel.titleForAddress = ""
        myViewModel.addressesTF = ""
    }

    Scaffold(
        snackbarHost = {
            if (myViewModel.addressBool){
                Snackbar(modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Text(text = "Saved Successfully")
                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
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
                    text = "The Address",
                    modifier = modifier.padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold
                )
            }


            OutlinedTextField(
                value = myViewModel.titleForAddress,
                onValueChange = {myViewModel.titleForAddress = it},
                label = { Text(text = "Address Title") }
            )

            Spacer(modifier = modifier.height(20.dp))

            OutlinedTextField(
                value = myViewModel.addressesTF,
                onValueChange = {myViewModel.addressesTF = it},
                label = { Text(text = "Address Details") }
            )

            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate(Screen.MyAddressScreen.route) }) {
                    Text(text = "Cancel")
                }

                Button(onClick = { myViewModel.uploadAddress() }) {
                    Text(text = "Save")
                }

                Button(onClick = { clearAllFunction() }) {
                    Text(text = "Clear All")
                }
            }
        }
    }
}