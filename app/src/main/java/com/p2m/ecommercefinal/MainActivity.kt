package com.p2m.ecommercefinal

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.p2m.ecommercefinal.ui.theme.ECommerceFinalTheme
import java.io.File

class MainActivity : ComponentActivity() {
    val myViewModel by viewModels<MyViewModel>()

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ECommerceFinalTheme {

                val modifier = Modifier

                Surface(modifier = modifier.fillMaxSize()) {

                    Navigation(modifier = modifier, myViewModel = myViewModel)

                }

            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun Navigation(modifier: Modifier, myViewModel: MyViewModel){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.FirstEntryScreen.route){

        composable(route = Screen.FirstEntryScreen.route){
            FirstEntryScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }
        
        composable(route = Screen.MainScreen.route){
            MainScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.PhoneAuthScreen.route){
            PhoneEntered(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.AccountScreen.route){
            AccountScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.ProductList.route.plus("/{cat}")){
            ProductList(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController,
                category = it.arguments?.getString("cat")!!
            )
        }

        val uri = "https://google.com"
        composable(
            route = Screen.ProductScreen.route.plus("/{cat}/{id}"),
            arguments = listOf(
                navArgument("cat"){
                    type = NavType.StringType
                    defaultValue = "toys"
                    nullable = true
                },
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = "ty01"
                    nullable = true
                }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "$uri/category={cat}/productID={id}" }
            )
        ){
            ProductDetails(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController,
                category = it.arguments?.getString("cat")!!,
                productID = it.arguments?.getString("id")!!
            )
        }

        composable(route = Screen.CartScreen.route){
            CartScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.WishlistScreen.route){
            WishlistScreen(
                myViewModel = myViewModel,
                modifier = modifier,
                navController = navController
            )
        }

        composable(route = Screen.OrderScreen.route){
            OrderScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.MyAddressScreen.route){
            MyAddressScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

        composable(route = Screen.EditAddressScreen.route){
            EditAddressScreen(
                modifier = modifier,
                myViewModel = myViewModel,
                navController = navController
            )
        }

    }

}
