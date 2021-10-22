package com.p2m.ecommercefinal

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.p2m.ecommercefinal.R
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun AccountScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController
){

    val scroll = rememberScrollState()
    val user = myViewModel.getUserData()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        myViewModel.selectedImageUri = it.toString()
    }

    fun back(){
        myViewModel.editProfile = false
    }



    fun saveProfInfo(){
        myViewModel.viewModelScope.launch {

            myViewModel.editProfileProcessBool = true

            val userID = FirebaseAuth.getInstance().currentUser?.uid
            val name = myViewModel.nameEditTF
            val email = myViewModel.emailEditTF
            val phone = myViewModel.phoneEditTF

            FirebaseStorage.getInstance().getReference("ProfImageECommerce/$userID").putFile(Uri.parse(
                myViewModel.selectedImageUri
            )).addOnSuccessListener {
                if (it.task.isSuccessful){
                    FirebaseStorage.getInstance().getReference("ProfImageECommerce/$userID").downloadUrl.addOnSuccessListener {
                        myViewModel.uploadUserData(it.toString(),name= name,email = email,phone = phone)
                        myViewModel.editProfile = false
                    }
                }
            }
        }
    }

    var refreshing by remember{ mutableStateOf(false)}

    fun onRefreshFunction(){
        refreshing = true
    }

    LaunchedEffect(refreshing){
        delay(2000)
        refreshing = false
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { onRefreshFunction() },
        indicator = {
            state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                scale = true,
                arrowEnabled = false,
                backgroundColor = Color.Transparent,
                shape = MaterialTheme.shapes.small,
                largeIndication = true,
                elevation = 0.dp
            )
        }
    ) {

        if (myViewModel.userDataBool){

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scroll, enabled = true),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {



                if (myViewModel.editProfileProcessBool){
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator()
                    }
                }else {
                    if (myViewModel.editProfile){
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.very_light_gray)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = modifier.height(40.dp))
                            Card(
                                modifier = modifier
                                    .size(150.dp)
                                    .clickable { launcher.launch("image/*") }
                                ,
                                shape = CircleShape,
                                elevation = 1.dp
                            ) {


                                if (myViewModel.selectedImageUri == ""){
                                    Image(
                                        painter = rememberImagePainter(R.drawable.add_prof_image),
                                        contentDescription ="Upload Photo",
                                        modifier = modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }else{
                                    GlideImage(
                                        imageModel = myViewModel.selectedImageUri,
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier.fillMaxSize(),
                                        contentDescription = "Upload Photo"
                                    )
                                }

                            }

                            Spacer(modifier = modifier.height(15.dp))

                            OutlinedTextField(
                                value = myViewModel.nameEditTF,
                                onValueChange = {myViewModel.nameEditTF= it},
                                label = { Text(text = "Name")}
                            )

                            Spacer(modifier = modifier.height(10.dp))

                            OutlinedTextField(
                                value = myViewModel.emailEditTF,
                                onValueChange = { myViewModel.emailEditTF = it },
                                label = { Text(text = "Email") }
                            )

                            Spacer(modifier = modifier.height(5.dp))

                            Row(
                                modifier = modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(text = "+", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = modifier.padding(5.dp))

                                OutlinedTextField(
                                    value = myViewModel.phoneEditTF,
                                    onValueChange = { myViewModel.phoneEditTF = it },
                                    label = { Text(text = "Phone With Country code") }
                                )
                            }

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Button(onClick = { back() }, modifier = modifier.padding(10.dp)) {
                                    Text(text = "Back")
                                }

                                Button(onClick = { saveProfInfo() }, modifier = modifier.padding(10.dp)) {
                                    Text(text = "Save")
                                }
                            }


                            Spacer(modifier = modifier.height(10.dp))

                        }
                    }else{

                        if (myViewModel.userDataBool){

                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .background(colorResource(id = R.color.very_light_gray)),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Spacer(modifier = modifier.height(30.dp))

                                Card(
                                    modifier = modifier
                                        .size(150.dp),
                                    shape = CircleShape,
                                    elevation = 1.dp
                                ) {
                                    if (myViewModel.userData.profPic != null){

                                        Image(
                                            painter = rememberImagePainter(data = myViewModel.userData.profPic!!),
                                            contentDescription ="Profile Picture" ,
                                            contentScale = ContentScale.Crop,
                                            modifier = modifier.fillMaxSize()
                                        )
                                    }

                                }
                                Spacer(modifier = modifier.height(15.dp))

                                if (myViewModel.userData.name != null){

                                    Text(
                                        text = myViewModel.userData.name!!,
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontFamily = FontFamily.Cursive
                                    )
                                }


                                Spacer(modifier = modifier.height(10.dp))

                                if (myViewModel.userData.email != null){

                                    Text(
                                        text = myViewModel.userData.email!!,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.Gray
                                    )
                                }

                                Spacer(modifier = modifier.height(5.dp))

                                if (myViewModel.userData.phone != null){

                                    Text(
                                        text = myViewModel.userData.phone!!,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.Gray
                                    )
                                }

                                Row(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp, end = 5.dp, bottom = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { myViewModel.editProfile = true }) {
                                        Text(
                                            text = "Edit or enter Profile image, Name, Email, Phone.",
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Blue,
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                Spacer(modifier = modifier.height(10.dp))

                            }
                        }else{

                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }

                        }
                    }
                }


                Spacer(modifier = modifier.height(40.dp))

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                        .background(colorResource(id = R.color.very_light_gray)),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 10.dp, start = 10.dp, end = 10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Orders", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { navController.navigate(Screen.OrderScreen.route){launchSingleTop = true} }) {

                                    Text(text = "View All Orders",fontWeight = FontWeight.Normal,color = Color.Blue,fontSize = 15.sp)
                                }
                            }

                        }

                    }

                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Wishlist", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { navController.navigate(Screen.WishlistScreen.route){launchSingleTop = true} }) {

                                    Text(text = "View Your Wishlist",fontWeight = FontWeight.Normal,color = Color.Blue, fontSize = 15.sp)
                                }

                            }

                        }

                    }

                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Cart", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { navController.navigate(Screen.CartScreen.route){launchSingleTop = true} }) {

                                    Text(text = "View Your Cart",fontWeight = FontWeight.Normal,color = Color.Blue, fontSize = 15.sp)
                                }

                            }

                        }

                    }

                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Cards & Wallet", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { /*TODO*/ }) {

                                    Text(text = "View Details",fontWeight = FontWeight.Normal,color = Color.Blue,fontSize = 15.sp)
                                }
                            }

                        }

                    }


                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Reviews", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { /*TODO*/ }) {

                                    Text(text = "View Your Reviews",fontWeight = FontWeight.Normal,color = Color.Blue,fontSize = 15.sp)
                                }
                            }

                        }

                    }

                    Card(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp), elevation = 2.dp
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "My Addresses", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }

                            Divider(modifier = modifier.padding(15.dp))

                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(onClick = { /*TODO*/ }) {

                                    Text(text = "View Addresses",fontWeight = FontWeight.Normal,color = Color.Blue,fontSize = 15.sp)
                                }
                            }

                        }

                    }

                }

            }
        }
    }


}






















