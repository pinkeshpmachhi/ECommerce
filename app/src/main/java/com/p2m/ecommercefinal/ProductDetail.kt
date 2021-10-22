package com.p2m.ecommercefinal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import java.io.File


@ExperimentalPagerApi
@Composable
fun ProductDetails(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    category :String,
    productID :String
){
//    myViewModel.backProductScreen  = false
    val applicationn = LocalContext.current as Context

    val item = myViewModel.getSingleProduct(cat = category,productID = productID)

    fun getImageName(int :Int) :String{
        if (int == 0){
            return  myViewModel.itemDetails?.image1TF!!
        }else if (int == 1){
            return myViewModel.itemDetails?.image2TF!!
        }else if (int == 2){
            return myViewModel.itemDetails?.image3TF!!
        }else if (int == 3){
            return  myViewModel.itemDetails?.image4TF!!
        }else
            return myViewModel.itemDetails?.image1TF!!
    }

    val scroll = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = 4,0)
    var addToCartText by remember{ mutableStateOf("Add To Cart")}
    var buyTitle by remember{ mutableStateOf("Buy Now")}

    fun addToCartFunction(){
        myViewModel.addToCart(myViewModel.itemDetails!!)
        addToCartText = "Added To Cart"
    }

    val contextApp = LocalContext.current as Context

    fun buyFunction(){
        val msg = myViewModel.itemDetails!!.title
        val finalMSG = "Payment completed and you have bought $msg"

        myViewModel.addToOrder(myViewModel.itemDetails!!)
        buyTitle = "Bought"

        myViewModel.fireNotificationFunction(context = contextApp, finalMSG, category = category, productID = productID)
    }

    myViewModel.getAllProductFromCat(category = category)

    val scrollHorizontalAddressRow = rememberScrollState()


    fun goTakeAddress(){
        myViewModel.titleForAddress = ""
        myViewModel.addressesTF = ""
        navController.navigate(Screen.EditAddressScreen.route){
            launchSingleTop = true
        }
    }

    if (myViewModel.productBool){

            Surface(modifier = modifier.fillMaxSize()) {

                Column(modifier = modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(scroll, enabled = true)
                ) {

                    Text(
                        text = myViewModel.itemDetails?.title!!,
                        modifier = modifier
                            .fillMaxWidth()
                            .align(Start),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalPager(state = pagerState, modifier = modifier
                        .fillMaxWidth()
                        .height(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.Top
                    ) {

                        GlideImage(
                            imageModel = getImageName(it),
                            modifier = modifier
                                .fillMaxWidth(0.8f)
                                .height(300.dp),
                            contentScale = ContentScale.Inside,
                            loading = {
                                CircularProgressIndicator()
                            }
                        )

                    }

                    Divider(modifier = modifier.padding(top = 10.dp, bottom = 10.dp))

                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(5.dp),
                        backgroundColor = Color.White
                    ) {

                        Text(
                            text = myViewModel.itemDetails?.price!!,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = modifier.padding(10.dp)
                        )

                    }

                    Spacer(modifier = modifier.height(20.dp))

                    Divider()
                    Spacer(modifier = modifier.height(6.dp))

                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = modifier.padding(start = 8.dp, end = 15.dp)
                    ) {

                        Text(
                            text = myViewModel.itemDetails?.stockInfo!!,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = modifier.height(1.dp))
                    Text(
                        text = "In Stock",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Green,
                        modifier = modifier.padding(start = 15.dp, end = 15.dp)
                    )

                    Spacer(modifier = modifier.height(15.dp))
                    Divider()

                    //---Address Section

                    fun onClickAddressFunction(item :AddressModel){
                        myViewModel.selectedAddress = item
                        Log.d(
                            "Pinkesh",
                            "onClickAddressFunction: Selected Address is ${myViewModel.selectedAddress.title}"
                        )
                    }

                    Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .horizontalScroll(scrollHorizontalAddressRow, enabled = true),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                        myViewModel.listOfAddresses.forEach {

                            if (myViewModel.selectedAddress.title.equals(it.title)){

                                Card(modifier = modifier
                                    .padding(5.dp)
                                    .size(150.dp)
                                    .clickable { onClickAddressFunction(it) },
                                    backgroundColor = colorResource(id = R.color.cyan_very_thin)
                                ) {
                                    Column(
                                        modifier = modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = CenterHorizontally
                                    ) {
                                        Text(
                                            text = it.title!!,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            maxLines = 2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = it.stuff!!,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.DarkGray,
                                            maxLines = 6,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }else{
                                Card(modifier = modifier
                                    .padding(5.dp)
                                    .size(150.dp)
                                    .clickable { onClickAddressFunction(it) },
                                    backgroundColor = colorResource(id = R.color.very_light_gray)
                                    ) {
                                    Column(
                                        modifier = modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = CenterHorizontally
                                    ) {
                                        Text(
                                            text = it.title!!,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            maxLines = 2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = it.stuff!!,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.DarkGray,
                                            maxLines = 6,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }


                        }
                    }

                    TextButton(
                        onClick = { goTakeAddress() }
                    ) {
                        Text(text ="+ Add Address",modifier= modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),textAlign = TextAlign.Start)
                    }

                    Divider()
                    Spacer(modifier = modifier.height(15.dp))



                    Spacer(modifier = modifier.height(10.dp))

                    OutlinedButton(onClick = { buyFunction() }, modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                        border = BorderStroke(1.dp,Color.Black),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Black)
                    ) {
                        Text(
                            text = buyTitle,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                                .align(CenterVertically)
                                .padding(3.5.dp),
                            color = Color.White,
                        )
                    }

                    Spacer(modifier = modifier.height(8.5.dp))

                    OutlinedButton(onClick = { addToCartFunction() }, modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp,Color.Black)
                    ) {
                        Text(
                            text = addToCartText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                                .align(CenterVertically)
                                .padding(3.5.dp),
                            color = Color.Black
                        )
                    }

                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { myViewModel.addToWishlist(myViewModel.itemDetails!!) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Add to wishlist button",
                                tint = Color.Black,
                                modifier = modifier.size(25.dp)
                            )
                        }


                        TextButton(onClick = { myViewModel.addToWishlist(myViewModel.itemDetails!!) }) {

                            Text(
                                text = "Add To Wishlist",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 19.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = Color.Black
                            )
                        }
                    }

                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(5.dp),
                        backgroundColor = Color.White
                    ) {

                        Text(
                            text = myViewModel.itemDetails?.specification!!,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = modifier.padding(10.dp)
                        )

                    }

                    Spacer(modifier = modifier.height(20.dp))
                    Divider()
                    Text(
                        text = myViewModel.itemDetails?.aboutItem!!,
                        modifier = modifier.padding(
                            start = 15.dp,
                            end = 15.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        )
                    )
                    Divider()

                    Spacer(modifier = modifier.height(10.dp))

                    LazyRow(modifier = modifier.background(colorResource(id = R.color.very_light_gray))){
                        myViewModel.allCatProducts.forEach {
                            item {
                                CardProductItem(
                                    modifier = modifier,
                                    myViewModel = myViewModel,
                                    navController = navController,
                                    product = it,
                                    category = category
                                )
                            }
                        }
                    }

                    Spacer(modifier = modifier.height(20.dp))

                }

            }

    }else{
        CircularProgressIndicator()
    }


}

@Composable
fun CardProductItem(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    product: SmartphoneCatModel,
    category: String
){

    Card(
        modifier = modifier
            .width(140.dp)
            .height(210.dp)
            .padding(10.dp)
            .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${product.productID}")) }
        ,
        shape = RoundedCornerShape(5.dp),
        elevation = 1.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {

            GlideImage(
                imageModel = product.image1TF!!,
                modifier = modifier
                    .width(140.dp)
                    .height(162.dp),
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                contentScale = ContentScale.Crop
            )

            Text(
                text = product.title!!,
                modifier = modifier
                    .width(140.dp)
                    .height(48.dp)
                    .padding(1.dp),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }


    }

}

















