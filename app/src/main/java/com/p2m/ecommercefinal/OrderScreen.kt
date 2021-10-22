package com.p2m.ecommercefinal

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay

@Composable
fun OrderScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController
){

    val TAG = "Pinkesh"
    var cat = ""

    val cont = LocalContext.current as Context

    fun deteleFromOrderFunction(product :SmartphoneCatModel){
        myViewModel.deletToOrder(product = product)
        Toast.makeText(cont,"Removed from Order successfully",Toast.LENGTH_LONG).show()
    }

    fun getCat(productID :String) :String{

        if (productID.subSequence(0,2).equals("bt")){
            cat = "beauty"
        }else if (productID.subSequence(0,2).equals("et")){
            cat = "electronics"
        }else if (productID.subSequence(0,2).equals("fd")){
            cat = "food"
        }else if (productID.subSequence(0,2).equals("he")){
            cat = "home_essentials"
        }else if (productID.subSequence(0,2).equals("me")){
            cat = "man's_essentials"
        }else if (productID.subSequence(0,2).equals("sm")){
            cat = "smartphone"
        }else if (productID.subSequence(0,2).equals("st")){
            cat = "sports"
        }else if (productID.subSequence(0,2).equals("ty")){
            cat = "toys"
        }else if (productID.subSequence(0,2).equals("we")){
            cat = "women's_essentials"
        }
        return cat
    }

    var refreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(refreshing){
        delay(2000)
        refreshing = false
    }

    fun onRefreshFunction(){
        refreshing = true
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing) ,
        onRefresh = { onRefreshFunction() },
        indicator = { state, refreshTrigger ->
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
        if (myViewModel.orderProcessBool && myViewModel.userDataBool){

            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                Text(text = "Your Orders",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 35.sp,
                    fontFamily = FontFamily.Cursive,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 0.dp, start = 10.dp, end = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Image(
                    painter = painterResource(id = R.drawable.your_order),
                    contentDescription = "Order Image",
                    modifier = modifier
                        .size(150.dp)
                        .padding(top = 0.dp, bottom = 0.dp, start = 5.dp, end = 5.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = modifier.height(10.dp))

                LazyColumn{

                    myViewModel.getOrderProduct().forEach {

                        item {

                            val productID = it.productID
                            fun go(){
                                val category =getCat(productID = productID!!)
//                            myViewModel.timerr.start()
//                            if (myViewModel.justBool)
                                navController.navigate(Screen.ProductScreen.route.plus("/$category/${it.productID}"))
                            }

                            Row(modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp)
                                .background(colorResource(id = R.color.very_light_gray))
                                .clickable { go() },
                                verticalAlignment = Alignment.CenterVertically,
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
                                        .align(Alignment.CenterVertically)
                                        .background(colorResource(id = R.color.very_light_gray))
                                    ,
                                    contentScale = ContentScale.Crop
                                )

                                Column(
                                    modifier = modifier
                                        .fillMaxWidth()

                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
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

                                    Button(onClick = { deteleFromOrderFunction(product = it) },
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                                    ) {
                                        Text(text = "Remove From Orders", color = Color.White)
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
                Text(
                    text = "May Be\nYour Order List\nIs\nEmpty\n or Something went wrong!",
                    fontSize = 30.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }


}