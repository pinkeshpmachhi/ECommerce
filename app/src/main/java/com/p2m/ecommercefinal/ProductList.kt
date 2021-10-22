package com.p2m.ecommercefinal

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductList(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    category :String
){
    val TAG = "Pinkesh"
    Log.d(TAG, "ProductList: OutSurface")
    val context = LocalContext.current as Context
    var cat = ""
    fun getCat() :String{

        if (category.equals("Beauty")){
            cat = "beauty"
        }else if (category.equals("Electronics")){
            cat = "electronics"
        }else if (category.equals("Food")){
            cat = "food"
        }else if (category.equals("Home Essentials")){
            cat = "home_essentials"
        }else if (category.equals("Man's Essentials")){
            cat = "man's_essentials"
        }else if (category.equals("Smartphone")){
            cat = "smartphone"
        }else if (category.equals("Sports")){
            cat = "sports"
        }else if (category.equals("Toys")){
            cat = "toys"
        }else if (category.equals("Women Essentials")){
            cat = "women's_essentials"
        }

        return cat
    }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Scaffold(

        topBar = {

            if (myViewModel.searchBool){


                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(6.5.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        value = myViewModel.searchTF,
                        modifier = modifier
                            .fillMaxWidth(),
                        onValueChange = { myViewModel.searchTF = it },
                        label = {
                            Text(
                                text = "Search Item",
                                fontWeight = FontWeight.Light
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { myViewModel.searchBool = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Search"
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            cursorColor = Color.White,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedIndicatorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            backgroundColor = Color.Black,
                            trailingIconColor = Color.White
                        )
                    )

                }
            }else{
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(6.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = "ECommerce",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        color = Color.White,
                        modifier = modifier.padding(start = 1.dp, end = 10.dp, bottom = 7.dp)
                    )

                    Spacer(modifier = modifier.width(80.dp))

                    IconButton(
                        onClick = {  },
                        modifier.weight(0.9f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification Icon",
                            tint = Color.White,
                            modifier = modifier.padding(start = 4.dp, bottom = 7.dp, top = 0.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.CartScreen.route){launchSingleTop= true }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart Icon",
                            tint = Color.White,
                            modifier = modifier.padding(start = 7.dp, end = 15.dp, bottom = 7.dp, top = 0.dp)
                        )
                    }

                    IconButton(
                        onClick = { myViewModel.searchBool = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Button",
                            tint = Color.White,
                            modifier = modifier.padding(start = 7.dp, end = 15.dp, bottom = 7.dp, top = 0.dp)
                        )
                    }


                }
            }
        },

        ) {

        if (myViewModel.searchBool){
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
            LazyColumn{

                myViewModel.searchFilter(myViewModel.searchTF).forEach {
                    item {

                        SingleRow(
                            title = it.title!!,
                            productID = it.productID!!,
                            modifier = modifier,
                            navController = navController
                        )
                    }
                }

            }
        }else{

            Surface(modifier = modifier.fillMaxSize()) {

                Log.d(TAG, "ProductList: InSurface")

                val list :MutableList<SmartphoneCatModel> = myViewModel.getAllProductFromCat(category = getCat())

                if (myViewModel.specialCatAllProduct){

                    Log.d(TAG, "ProductList:SIZE ${myViewModel.allCatProducts.size}")

                    Column(modifier = modifier.fillMaxSize()) {

                        if (myViewModel.addToCartBool){

                            LazyColumn(modifier = modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)){

                                myViewModel.allCatProducts.forEach {

                                    item {

                                        Row(modifier = modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                            .background(colorResource(id = R.color.very_light_gray))
                                            .clickable {
                                                navController.navigate(
                                                    Screen.ProductScreen.route.plus(
                                                        "/$cat/${it.productID}"
                                                    )
                                                )
                                            },
                                            verticalAlignment = CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            GlideImage(
                                                imageModel = it.image1TF!!,
                                                shimmerParams = ShimmerParams(
                                                    baseColor = MaterialTheme.colors.background,
                                                    highlightColor = Color.LightGray,
                                                    durationMillis = 350,
                                                    dropOff = 0.5f,
                                                    tilt = 20f
                                                ),
                                                modifier = modifier
                                                    .width(180.dp)
                                                    .height(200.dp)
                                                    .padding(10.dp)
                                                    .align(CenterVertically)
                                                    .background(colorResource(id = R.color.very_light_gray))
                                                ,
                                                contentScale = ContentScale.Crop
                                            )

                                            Column(
                                                modifier = modifier
                                                    .fillMaxWidth()

                                                    .padding(10.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Start
                                            ) {

                                                Text(
                                                    text = it.title!!,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.Black,
                                                    textAlign = TextAlign.Start,
                                                    maxLines = 3
                                                )

                                                Text(
                                                    text = it.price!!,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Start
                                                )

                                                Button(onClick = { myViewModel.addToCart(it) },
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp),
                                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                                                ) {
                                                    Text(text = "Add to Cart", color = Color.White)
                                                }

                                            }

                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Snackbar(modifier = modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .padding(10.dp)
                                    .background(Color.Black)
                                ) {
                                    Column(
                                        modifier = modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Text(text = "Added to cart successfully!!!", color = Color.White, modifier = modifier.fillMaxWidth().padding(5.dp),textAlign = TextAlign.Start)
                                    }
                                }
                            }
                        }else{
                            LazyColumn{

                                myViewModel.allCatProducts.forEach {

                                    item {

                                        Row(modifier = modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                            .background(colorResource(id = R.color.very_light_gray))
                                            .clickable {
                                                navController.navigate(
                                                    Screen.ProductScreen.route.plus(
                                                        "/$cat/${it.productID}"
                                                    )
                                                )
                                            },
                                            verticalAlignment = CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            GlideImage(
                                                imageModel = it.image1TF!!,
                                                shimmerParams = ShimmerParams(
                                                    baseColor = MaterialTheme.colors.background,
                                                    highlightColor = Color.LightGray,
                                                    durationMillis = 350,
                                                    dropOff = 0.5f,
                                                    tilt = 20f
                                                ),
                                                modifier = modifier
                                                    .width(180.dp)
                                                    .height(200.dp)
                                                    .padding(10.dp)
                                                    .align(CenterVertically)
                                                    .background(colorResource(id = R.color.very_light_gray))
                                                ,
                                                contentScale = ContentScale.Crop
                                            )

                                            Column(
                                                modifier = modifier
                                                    .fillMaxWidth()

                                                    .padding(10.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Start
                                            ) {

                                                Text(
                                                    text = it.title!!,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.Black,
                                                    textAlign = TextAlign.Start,
                                                    maxLines = 3
                                                )

                                                Text(
                                                    text = it.price!!,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Start
                                                )

                                                Button(onClick = { myViewModel.addToCart(it) },
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp),
                                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                                                ) {
                                                    Text(text = "Add to Cart", color = Color.White)
                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }

                }else{
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }


            }
        }
    }

}


















