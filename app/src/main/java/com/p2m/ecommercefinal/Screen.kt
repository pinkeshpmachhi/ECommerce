package com.p2m.ecommercefinal

sealed class Screen(val route :String){
    object MainScreen :Screen("main_screen")
    object FirstEntryScreen :Screen("first_entry_screen")
    object PhoneAuthScreen :Screen("phone_auth_screen")
    object AccountScreen :Screen("account_screen")
    object ProductScreen :Screen("product_screen")
    object ProductList :Screen("product_list_screen")
    object CartScreen :Screen("cart_screen")
    object WishlistScreen :Screen("wishlist_screen")
    object OrderScreen :Screen("order_screen")
    object MyAddressScreen :Screen("myaddress_screen")
    object EditAddressScreen :Screen("edit_address_screen")
}
