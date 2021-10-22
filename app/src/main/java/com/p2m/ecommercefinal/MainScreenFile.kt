package com.p2m.ecommercefinal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun MainScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController
){

    Log.d("Pinkesh", "MainScreen: ::::               COME BACK  OUTER ")
    //-----------------------------------------------------For Go Home Screen------------------------------------------------------//

    val mainActivity = MainActivity()
    var k: Int = 1
    var TAG = "Pinkesh"
    val activity = LocalContext.current as? Activity

    val application  = LocalContext.current as? Application
    val applicationn  = LocalContext.current as? Context

    myViewModel.getCartProduct()
    myViewModel.getwishlistProduct()
    myViewModel.getOrderProduct()

//    fun getImgBitmap(name :String) :Bitmap{
//
//        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
//        val imgFile = File(path)
//
//        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//
//        if (bitmap == null){
//            bitmap = BitmapFactory.decodeResource(applicationn?.resources, R.drawable.shoestwelve)
//        }
//
//        return  bitmap
//
//    }

    fun goHome() {
        if (k == 2){
            activity?.finish()
        }
        k++
    }

    val callback = object  : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            goHome()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val dispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current){
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    dispatcher.addCallback(lifecycleOwner, callback)


    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    fun logo(){
        scope.launch {

            drawerState.close()
        }
    }

    fun home(){
        scope.launch {
            drawerState.close()
        }
    }

    fun allCat(){
        scope.launch {
            drawerState.close()
        }
    }

    fun exclusive(){
        scope.launch {
            drawerState.close()
        }
    }

    fun myOrders(){
        scope.launch {
            drawerState.close()
        }
        navController.navigate(Screen.OrderScreen.route){launchSingleTop = true }
    }

    fun myCart(){
        scope.launch {
            drawerState.close()
        }
        navController.navigate(Screen.CartScreen.route){launchSingleTop = true}
    }

    fun myWishlist(){
        scope.launch {
            drawerState.close()
        }

        navController.navigate(Screen.WishlistScreen.route){launchSingleTop = true}
    }

    fun myAccount(){
        scope.launch {
            drawerState.close()
        }

        navController.navigate(Screen.AccountScreen.route)
    }

    fun myNotification(){
        scope.launch {
            drawerState.close()
        }
    }

    fun notificationSetting(){
        scope.launch {
            drawerState.close()
        }
    }

    fun helpCentre(){
        scope.launch {
            drawerState.close()
        }
    }

    fun privacyPolicy(){
        scope.launch {
            drawerState.close()
        }
    }

    fun legal(){
        scope.launch {

            drawerState.close()
        }
    }

    fun myAddress(){
        scope.launch{
            drawerState.close()
        }
        navController.navigate(Screen.MyAddressScreen.route){launchSingleTop= true }
    }

    val listImages = mutableListOf<Int>()
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)
    listImages.add(R.drawable.shoestwelve)

    val scrollColumn = rememberScrollState()
    val scrollCategories = rememberScrollState()
    val scrollBeautyCategory = rememberScrollState()

    if (myViewModel.manEssentialsRow &&
        myViewModel.foodSingle &&
        myViewModel.foodFour &&
        myViewModel.beautyRow &&
        myViewModel.homeEssentialsSingle &&
        myViewModel.homeEssentialsFour &&
        myViewModel.electronicsRow &&
        myViewModel.sportsSingle &&
        myViewModel.sportsFour &&
        myViewModel.smartphoneRow &&
        myViewModel.toysSingle &&
        myViewModel.toysFour &&
        myViewModel.womenSingle &&
        myViewModel.womenFour
    ){
        myViewModel.finalBool = true
    }

    var refreshing by remember{ mutableStateOf(false)}
    LaunchedEffect(refreshing){
        delay(2000)
        refreshing = false
    }

    fun onRefreshFunction(){
        refreshing = true
    }

    ModalDrawer(
        drawerContent = {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    IconButton(
                        onClick = { home() },
                        modifier = modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            modifier = modifier.size(33.dp)
                        )
                    }

                    Text(
                        text = "Home",
                        modifier = modifier
                            .padding(20.dp)
                            .clickable { home() },
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = modifier
                        .width(30.dp)
                        .weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Logo",
                        modifier = modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                            .clickable { logo() }
                    )

                }

                Divider()

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { exclusive() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = "Exclusive")
                    }

                    Text(text = "Exclusives")

                }

                Divider()

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myOrders() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "My Order")
                    }

                    Text(text = "My Orders")

                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myCart() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "My Order")
                    }

                    Text(text = "My Cart")

                }


                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myWishlist() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { myWishlist() }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "My Order")
                    }

                    Text(text = "My Wishlist")

                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myAddress() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { myAddress() }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Place, contentDescription = "My Order")
                    }

                    Text(text = "My Addresses")

                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myAccount() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {  }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "My Account")
                    }

                    Text(text = "My Account")

                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { myNotification() }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { /*TODO*/ }, modifier = modifier.padding(0.dp)) {
                        Icon(imageVector = Icons.Filled.Notifications, contentDescription = "My Order")
                    }

                    Text(text = "My Notifications")

                }

                Divider()


                TextButton(
                    onClick = { notificationSetting() },
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notification Setting",
                        fontWeight = FontWeight.Light,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )
                }

                TextButton(
                    onClick = { helpCentre() },
                    modifier = modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Help Centre",
                        fontWeight = FontWeight.Light,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )
                }

                TextButton(
                    onClick = { privacyPolicy() },
                    modifier = modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Privacy Policy",
                        fontWeight = FontWeight.Light,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )
                }

                TextButton(
                    onClick = { legal() },
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Legal",
                        fontWeight = FontWeight.Light,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )
                }


            }
        },
        drawerState = drawerState
    ) {
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

                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = modifier.padding(start = 0.dp, end = 6.dp,  bottom = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Modal Drawer",
                                tint = Color.White
                            )
                        }


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

            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = refreshing),
                onRefresh = { onRefreshFunction() },
                indicator = {state, refreshTrigger ->
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

                    if (myViewModel.categoryBool){

                        myViewModel.viewModelScope.launch {
                            if (myViewModel.number == 10){
                                myViewModel.finalBool = true
                            }else{
                                delay(60000)
                                myViewModel.finalBool = true
                            }
                        }

                        if (myViewModel.finalBool){


                            Log.d(TAG, "MainScreen:The number is :::  ${myViewModel.number}")
                            LazyColumn{

                                item {
                                    Spacer(modifier = modifier.height(15.dp))

                                    Text(
                                        text = "Categories",
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 10.dp,
                                                top = 1.dp,
                                                end = 10.dp,
                                                bottom = 0.dp
                                            ),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 23.sp
                                    )

                                    Row(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(scrollCategories, enabled = true)
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        myViewModel.categoryList.forEach {

                                            Box(
                                                modifier = modifier
                                                    .size(120.dp)
                                                    .padding(5.dp)
                                            ) {


                                                Card(
                                                    modifier = modifier
                                                        .size(110.dp)
                                                        .clickable {
                                                            navController.navigate(
                                                                Screen.ProductList.route.plus(
                                                                    "/${it.name}"
                                                                )
                                                            )
                                                        },
                                                    shape = RoundedCornerShape(5.dp),
                                                    elevation = 2.dp,
                                                    backgroundColor = Color.White
                                                ) {

                                                    Column(
                                                        modifier = modifier
                                                            .size(95.dp)
                                                            .padding(2.dp)
                                                        ,
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {

                                                        GlideImage(
                                                            imageModel = it.image!!,
                                                            modifier = modifier.size(60.dp),
                                                            shimmerParams = ShimmerParams(
                                                                baseColor = MaterialTheme.colors.background,
                                                                highlightColor = Color.LightGray,
                                                                durationMillis = 350,
                                                                dropOff =   0.65f,
                                                                tilt = 20f
                                                            )
                                                        )
                                                        Text(
                                                            text = it.name!!,
                                                            modifier = modifier
                                                                .fillMaxWidth()
                                                                .padding(1.dp)
                                                                .weight(1f),
                                                            textAlign = TextAlign.Center,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                    }


                                                }

                                            }

                                        }
                                    }
                                }

                                item{
                                    //--------------------------------------Man's Essentials row------------------------------//



//                            RowOfAllProducts(
//                                modifier = modifier,
//                                myViewModel = myViewModel,
//                                navController = navController,
//                                listOfProducts = myViewModel.manEssentialRowList,
//                                category = "man's_essentials"
//                            )


                                    //--------------------------------------Food Single------------------------------//

//                            val fBit = getImgBitmap(myViewModel.foodSingleList[0].productID!!)

                                    SingleBox(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        title = myViewModel.foodSingleList[0].title!!,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        img = myViewModel.foodSingleList[0].image1TF!!,
                                        category = "food",
                                        productID = myViewModel.foodSingleList[0].productID!!
                                    )
                                }

                                item{
//--------------------------------------Food four------------------------------//

                                    FourItemCombination(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        listOfProducts = myViewModel.foodFourList,
                                        category = "food"
                                    )


                                    //--------------------------------------Beauty row------------------------------//


//                            RowOfAllProducts2(
//                                modifier = modifier,
//                                myViewModel = myViewModel,
//                                navController = navController,
//                                listOfProducts = myViewModel.beautyRowList,
//                                category = "beauty"
//                            )
                                }

                                item{
//--------------------------------------Home Essential Single------------------------------//

//                            val hBit = getImgBitmap(myViewModel.homeEssentialSingleList[0].productID!!)

                                    SingleBox2(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        title = myViewModel.homeEssentialSingleList[0].title!!,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        img = myViewModel.homeEssentialSingleList[0].image1TF!!,
                                        category = "home_essentials",
                                        productID = myViewModel.homeEssentialSingleList[0].productID!!
                                    )
                                }

                                item{
                                    //--------------------------------------Home Essential Four------------------------------//


                                    FourItemCombination2(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        listOfProducts = myViewModel.homeEssentialFourList,
                                        category ="home_essentials"
                                    )


                                    //--------------------------------------Electronics row------------------------------//


//                            RowOfAllProducts3(
//                                modifier = modifier,
//                                myViewModel = myViewModel,
//                                navController = navController,
//                                listOfProducts = myViewModel.electronicsRowList,
//                                category = "electronics"
//                            )
                                }

                                item{
                                    //--------------------------------------Sports single------------------------------//

//                            val sBit = getImgBitmap(myViewModel.sportsSingleList[0].image1TF!!)

                                    SingleBox3(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        title = myViewModel.sportsSingleList[0].title!!,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        img = myViewModel.sportsSingleList[0].image1TF!!,
                                        category = "sports",
                                        productID = myViewModel.sportsSingleList[0].productID!!
                                    )

                                    //--------------------------------------SmartPhone row------------------------------//

//                            RowOfAllProducts4(
//                                modifier = modifier,
//                                myViewModel = myViewModel,
//                                navController = navController,
//                                listOfProducts = myViewModel.smartphoneRowList,
//                                category = "smartphone"
//                            )
                                }

                                item{
//--------------------------------------Toys single------------------------------//

//                            val tBit = getImgBitmap(myViewModel.toysSingleList[0].productID!!)

                                    SingleBox4(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        title = myViewModel.toysSingleList[0].title!!,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        img = myViewModel.toysSingleList[0].image1TF!!,
                                        category = "toys",
                                        productID = myViewModel.toysSingleList[0].productID!!
                                    )
                                }

                                item{

                                    //--------------------------------------Toys four------------------------------//


                                    FourItemCombination3(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        listOfProducts = myViewModel.toysFourList,
                                        category = "toys"
                                    )
                                }

                                item{
//--------------------------------------Women Single------------------------------//

//                            val wBit = getImgBitmap(myViewModel.womenSingleList[0].productID!!)

                                    SingleBox5(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        title = myViewModel.womenSingleList[0].title!!,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        img = myViewModel.womenSingleList[0].image1TF!!,
                                        category = "women's_essentials",
                                        productID = myViewModel.womenSingleList[0].productID!!
                                    )
                                }

                                item{
//--------------------------------------Women four------------------------------//

                                    FourItemCombination4(
                                        modifier = modifier,
                                        myViewModel = myViewModel,
                                        navController = navController,
                                        backColor = colorResource(id = R.color.very_light_gray),
                                        listOfProducts = myViewModel.womenFourList,
                                        category = "women's_essentials"
                                    )
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

}

@Composable
fun SingleRow(title :String, productID :String, modifier: Modifier, navController: NavController) {

    var cat = ""
    fun getCat(productID: String): String {

        if (productID.subSequence(0, 2).equals("bt")) {
            cat = "beauty"
        } else if (productID.subSequence(0, 2).equals("et")) {
            cat = "electronics"
        } else if (productID.subSequence(0, 2).equals("fd")) {
            cat = "food"
        } else if (productID.subSequence(0, 2).equals("he")) {
            cat = "home_essentials"
        } else if (productID.subSequence(0, 2).equals("me")) {
            cat = "man's_essentials"
        } else if (productID.subSequence(0, 2).equals("sm")) {
            cat = "smartphone"
        } else if (productID.subSequence(0, 2).equals("st")) {
            cat = "sports"
        } else if (productID.subSequence(0, 2).equals("ty")) {
            cat = "toys"
        } else if (productID.subSequence(0, 2).equals("we")) {
            cat = "women's_essentials"
        }
        return cat
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            maxLines = 1,
            modifier = modifier
                .clickable {
                    navController.navigate(Screen.ProductScreen.route.plus("/${getCat(productID = productID)}/$productID")) {
                        launchSingleTop = true
                    }
                }
        )
        Spacer(modifier = modifier.padding(bottom = 10.dp))
        Divider()
    }
}

@ExperimentalCoilApi
@Composable
fun  SingleBox(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    title :String,
    backColor :Color,
    img :String,
    productID :String,
    category :String
){
    val TAG = "Pinkesh"


        Box(modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(backColor)
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(Color.LightGray)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/$productID")) },
                shape = RoundedCornerShape(5.dp),
                elevation = 0.dp
            ) {

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = img,
                        shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ), modifier = modifier
                            .fillMaxWidth()
                            .height(325.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = title,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }


            }
        }

}

@ExperimentalCoilApi
@Composable
fun  SingleBox2(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    title :String,
    backColor :Color,
    img :String,
    category: String,
    productID: String
){
    val TAG = "Pinkesh"

    Box(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(Color.LightGray)
                .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/$productID")) },
            shape = RoundedCornerShape(5.dp),
            elevation = 0.dp
        ) {

            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GlideImage(imageModel = img,
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ), modifier = modifier
                        .fillMaxWidth()
                        .height(325.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }


        }
    }

}

@ExperimentalCoilApi
@Composable
fun  SingleBox3(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    title :String,
    backColor :Color,
    img :String,
    category: String,
    productID: String
){
    val TAG = "Pinkesh"

    Box(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(Color.LightGray)
                .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/$productID")) },
            shape = RoundedCornerShape(5.dp),
            elevation = 0.dp
        ) {

            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GlideImage(imageModel = img,
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ), modifier = modifier
                        .fillMaxWidth()
                        .height(325.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }


        }
    }

}

@ExperimentalCoilApi
@Composable
fun  SingleBox4(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    title :String,
    backColor :Color,
    img :String,
    category: String,
    productID: String
){
    val TAG = "Pinkesh"

    Box(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(Color.LightGray)
                .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/$productID")) },
            shape = RoundedCornerShape(5.dp),
            elevation = 0.dp
        ) {

            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GlideImage(imageModel = img,
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ), modifier = modifier
                        .fillMaxWidth()
                        .height(325.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }


        }
    }

}

@ExperimentalCoilApi
@Composable
fun  SingleBox5(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    title :String,
    backColor :Color,
    img :String,
    category: String,
    productID: String
){
    val TAG = "Pinkesh"

    Box(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(Color.LightGray)
                .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/$productID")) },
            shape = RoundedCornerShape(5.dp),
            elevation = 0.dp
        ) {

            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GlideImage(imageModel = img,
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ), modifier = modifier
                        .fillMaxWidth()
                        .height(325.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = title,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }


        }
    }

}

@ExperimentalCoilApi
@Composable
fun RowOfAllProducts(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val scrollRw = rememberScrollState()
    val applicationn = LocalContext.current as Context

    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollRw, enabled = true)
    ) {
        listOfProducts.forEach {

            Box(modifier = modifier
                .size(150.dp)
                .background(Color.White)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${it.productID}")) },
                    shape = RoundedCornerShape(5.dp),
                    elevation = 0.dp
                ) {

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GlideImage(imageModel = getImgBitmap(it.productID!!),
                            shimmerParams = ShimmerParams(
                                baseColor = MaterialTheme.colors.background,
                                highlightColor = Color.LightGray,
                                durationMillis = 350,
                                dropOff = 0.65f,
                                tilt = 20f
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(115.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = it.title!!,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .background(Color.DarkGray)
                                .padding(3.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RowOfAllProducts2(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val scrollRw = rememberScrollState()
    val applicationn = LocalContext.current as Context

    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollRw, enabled = true)
    ) {
        listOfProducts.forEach {

            Box(modifier = modifier
                .size(150.dp)
                .background(Color.White)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${it.productID}")) },
                    shape = RoundedCornerShape(5.dp),
                    elevation = 0.dp
                ) {

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GlideImage(imageModel = getImgBitmap(it.productID!!),
                            shimmerParams = ShimmerParams(
                                baseColor = MaterialTheme.colors.background,
                                highlightColor = Color.LightGray,
                                durationMillis = 350,
                                dropOff = 0.65f,
                                tilt = 20f
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(115.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = it.title!!,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .background(Color.DarkGray)
                                .padding(3.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RowOfAllProducts3(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val scrollRw = rememberScrollState()
    val applicationn = LocalContext.current as Context

    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollRw, enabled = true)
    ) {
        listOfProducts.forEach {

            Box(modifier = modifier
                .size(150.dp)
                .background(Color.White)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${it.productID}")) },
                    shape = RoundedCornerShape(5.dp),
                    elevation = 0.dp
                ) {

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GlideImage(imageModel = getImgBitmap(it.productID!!),
                            shimmerParams = ShimmerParams(
                                baseColor = MaterialTheme.colors.background,
                                highlightColor = Color.LightGray,
                                durationMillis = 350,
                                dropOff = 0.65f,
                                tilt = 20f
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(115.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = it.title!!,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .background(Color.DarkGray)
                                .padding(3.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RowOfAllProducts4(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val scrollRw = rememberScrollState()
    val applicationn = LocalContext.current as Context

    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollRw, enabled = true)
    ) {
        listOfProducts.forEach {

            Box(modifier = modifier
                .size(150.dp)
                .background(Color.White)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${it.productID}")) },
                    shape = RoundedCornerShape(5.dp),
                    elevation = 0.dp
                ) {

                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        GlideImage(imageModel = getImgBitmap(it.productID!!),
                            shimmerParams = ShimmerParams(
                                baseColor = MaterialTheme.colors.background,
                                highlightColor = Color.LightGray,
                                durationMillis = 350,
                                dropOff = 0.65f,
                                tilt = 20f
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(115.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = it.title!!,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .background(Color.DarkGray)
                                .padding(3.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }


                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FourItemCombination(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    backColor: Color,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val applicationn = LocalContext.current as Context
    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Column(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {
            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[0].productID!!}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[0].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[0].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[1].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[1].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[1].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {

            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[2].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[2].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[2].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[3].productID!!}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[3].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[3].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FourItemCombination2(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    backColor: Color,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val applicationn = LocalContext.current as Context
    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn?.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Column(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {
            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[0].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[0].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[0].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[1].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[1].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[1].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {

            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[2].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[2].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[2].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[3].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[3].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[3].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun FourItemCombination3(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    backColor: Color,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val applicationn = LocalContext.current as Context
    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn?.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Column(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {
            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[0].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[0].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[0].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[1].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[1].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[1].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {

            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[2].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[2].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[2].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[3].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[3].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[3].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FourItemCombination4(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navController: NavController,
    backColor: Color,
    listOfProducts :MutableList<SmartphoneCatModel>,
    category: String
){

    val applicationn = LocalContext.current as Context
    fun getImgBitmap(name :String) :Bitmap{

        val path = "${Environment.getExternalStorageDirectory()}/ECommerce/${name}.jpg"
        val imgFile = File(path)

        var bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(applicationn.resources, R.drawable.shoestwelve)
        }

        return  bitmap

    }

    Column(modifier = modifier
        .fillMaxWidth()
        .height(400.dp)
        .background(backColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {
            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[0].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[0].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[0].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[1].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[1].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[1].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {

            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[2].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[2].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[2].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Card(
                modifier = modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
                    .clickable { navController.navigate(Screen.ProductScreen.route.plus("/$category/${listOfProducts[3].productID}")) },
                elevation = 0.2.dp,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GlideImage(imageModel = listOfProducts[3].image1TF!!,
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = listOfProducts[3].title!!,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
    }
}















