package com.p2m.ecommercefinal

import android.app.*
import android.content.Context
import android.content.Context.*
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MyViewModel(application: Application) :AndroidViewModel(application) {

    var categoryList :MutableList<CategoryModel> = mutableListOf<CategoryModel>()
    var categoryBool by mutableStateOf(false)

    var productBool by mutableStateOf(false)
    var itemDetails :SmartphoneCatModel? = null
    var editProfile by mutableStateOf(false)

    var nameEditTF by mutableStateOf("")
    var emailEditTF by mutableStateOf("")
    var phoneEditTF by mutableStateOf("")

    var editProfileProcessBool by mutableStateOf(false)
    var selectedImageUri by mutableStateOf("")
    var userData :UserModel = UserModel()

    var userDataBool by mutableStateOf(false)
    var nullInCartBool by mutableStateOf(false)

    init {

        categoryList.add(CategoryModel("Beauty",R.drawable.beauty_category))
        categoryList.add(CategoryModel("Electronics",R.drawable.electronics_category))
        categoryList.add(CategoryModel("Food",R.drawable.food_category))
        categoryList.add(CategoryModel("Home Essentials",R.drawable.home_accessories_category))
        categoryList.add(CategoryModel("Man's Essentials",R.drawable.man_fashion_category))
        categoryList.add(CategoryModel("Smartphone",R.drawable.smartphone_category))
        categoryList.add(CategoryModel("Sports",R.drawable.sports_category))
        categoryList.add(CategoryModel("Toys",R.drawable.toys_category))
        categoryList.add(CategoryModel("Women Essentials",R.drawable.women_fashion_category))


        getAllProductsName()

        getAllCategories()


        getStarter(application)

        getUserData()

        getAddresses()

    }

    val TAG  = "Pinkesh"

    var signUpBool by mutableStateOf(true)
    var loginBool by mutableStateOf(false)
    var emailLoginTF by mutableStateOf("")
    var passwordLoginTF by mutableStateOf("")

    var emailSignUpTF by mutableStateOf("")
    var passwordSignUpTE by mutableStateOf("")
    var confirmPassWordTE by mutableStateOf("")

    var phone by mutableStateOf("")
    var oneTimePass by mutableStateOf("")
    var countryCode by mutableStateOf("")

    var verID :String = ""
    var phoneCred :PhoneAuthCredential? = null
    var goToMainBool by mutableStateOf(false)
    var showOTPSectionBool by mutableStateOf(false)

    var otpMSG by mutableStateOf("")

    var loginInvalidTextShowBool by mutableStateOf(false)
    var loginInvalidTextShowText by mutableStateOf("Invalid email or password!")

    var passConfirmBool by mutableStateOf(true)
    var passConfirmBoolText by mutableStateOf("")

    var passSignUpCharErrorText by mutableStateOf("Password must contain a number(0,1,2 etc.), a symbol(@,#,$ etc.) and a capital letter(A,B,C etc.).")
    var passSignUpCharErrorBool by mutableStateOf(false)

    var processBool by mutableStateOf(false)
    var snakBarLoginFalse by mutableStateOf(false)
    var snakBarSignUpFalse by mutableStateOf(false)
    var spellCheckBool by mutableStateOf(false)

    var searchTF by mutableStateOf("")
    var searchBool by mutableStateOf(false)

    val timerForFinishProcessBar = object : CountDownTimer(10000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            Log.d("Pinkesh", "onTick: Tick of Process bool timer.")
        }

        override fun onFinish() {
            processBool = false
        }
    }

    
    var intt = 0
    val timer = object : CountDownTimer(120000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            otpMSG = "Click again to resend code after ${120-intt} seconds."
            intt ++
        }

        override fun onFinish() {
            showOTPSectionBool = false
            intt = 0
        }
    }

    val timerForLoginFailed = object :CountDownTimer(3000,1000){
        override fun onFinish() {
            snakBarLoginFalse = false
            snakBarSignUpFalse = false
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d("Pinkesh", "onTick: Login Failed snakebar")
        }
    }

    /*
    Beauty 10
Electronics 10
Man's Essentials 10
Smartphone 10
Food 5
Home Essential 5
Sports 5
Toys 5
Women's essentials 5
     */

    var specialCatAllProduct by mutableStateOf(false)

    //Bools for all
    var manEssentialsRow by mutableStateOf(false)
    var foodSingle by mutableStateOf(false)
    var foodFour by mutableStateOf(false)
    var beautyRow by mutableStateOf(false)
    var homeEssentialsSingle by mutableStateOf(false)
    var homeEssentialsFour by mutableStateOf(false)
    var electronicsRow by mutableStateOf(false)
    var sportsSingle by mutableStateOf(false)
    var sportsFour by mutableStateOf(false)
    var smartphoneRow by mutableStateOf(false)
    var toysSingle by mutableStateOf(false)
    var toysFour by mutableStateOf(false)
    var womenSingle by mutableStateOf(false)
    var womenFour by mutableStateOf(false)

    var finalBool by mutableStateOf(false)

    var manEssentialRowList :MutableList<SmartphoneCatModel> = mutableListOf()
    var foodSingleList :MutableList<SmartphoneCatModel> = mutableListOf()
    var foodFourList :MutableList<SmartphoneCatModel> = mutableListOf()
    var beautyRowList :MutableList<SmartphoneCatModel> = mutableListOf()
    var homeEssentialSingleList :MutableList<SmartphoneCatModel> = mutableListOf()
    var homeEssentialFourList :MutableList<SmartphoneCatModel> = mutableListOf()
    var electronicsRowList :MutableList<SmartphoneCatModel> = mutableListOf()
    var sportsSingleList :MutableList<SmartphoneCatModel> = mutableListOf()
    var sportsFourList :MutableList<SmartphoneCatModel> = mutableListOf()
    var smartphoneRowList :MutableList<SmartphoneCatModel> = mutableListOf()
    var toysSingleList :MutableList<SmartphoneCatModel> = mutableListOf()
    var toysFourList :MutableList<SmartphoneCatModel> = mutableListOf()
    var womenSingleList :MutableList<SmartphoneCatModel> = mutableListOf()
    var womenFourList :MutableList<SmartphoneCatModel> = mutableListOf()

    var number :Int by mutableStateOf(0)
    var numberTwo :Int by mutableStateOf(0)


    /*
    Diagram of the screen
    - New Arrivals
    - Man's Essentials row
    - Food Single
    - Food four
    - Beauty row
    - Home Essential Single
    - Home Essential Four
    - Electronics row
    - Sports single
    - Sports four
    - SmartPhone row
    - Toys single
    - Toys four
    - Women Single
    - Women four
     */

    fun getStarter(cont :Application){
        getManEssentialRow(cont)
        getFoodSingle(cont)
        getFoodFour(cont)
        getBeautyRow(cont)
        getHomeEssentialSingle(cont)
        getHomeEssentialFour(cont)
        getElectronicsRow(cont)
        getSportsSingle(cont)
        getSportsFour(cont)
        getSmartphoneRow(cont)
        getToysSingle(cont)
        getToysFour(cont)
        getWomenSingle(cont)
        getWomenFour(cont)

    }

    fun getManEssentialRow(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: Man'S Essentials ${error.message}")
                    manEssentialsRow = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (items in snapshot.children){
//                        val product = items.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        manEssentialRowList.add(items.getValue(SmartphoneCatModel::class.java)!!)

                    }

                    Log.d(TAG, "onDataChange:manEssentialRow  $number")

                    if (manEssentialRowList.size == 10){
                        manEssentialsRow = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("man's_essentials").addValueEventListener(listioner)
        }

    }

    fun getFoodSingle(context: Application) {

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: form FoodSingle ${error.message}")
                    foodSingle = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val product = snapshot.getValue(SmartphoneCatModel::class.java)!!
//                    downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                    foodSingleList.add(snapshot.getValue(SmartphoneCatModel::class.java)!!)
                    foodSingle = true
                    number++
                    Log.d(TAG, "onDataChange:getFoodSingle $number")

                }
            }

            FirebaseDatabase.getInstance().getReference("food/fd05").addValueEventListener(listioner)
        }

    }

    fun getFoodFour(context: Application){

        viewModelScope.launch {

            val listener = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: FoodFour ${error.message}")
                    foodFour = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (items in snapshot.children){
//                        val product = items.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        foodFourList.add(items.getValue(SmartphoneCatModel::class.java)!!)

                    }
                    Log.d(TAG, "onDataChange:getFoodFour() $number")
                    if (foodFourList.size == 5){
                        foodFour = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("food").addValueEventListener(listener)
        }

    }

    fun getBeautyRow(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: BeautyRow ${error.message}")
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for(items in snapshot.children){
//                        val product = items.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        beautyRowList.add(items.getValue(SmartphoneCatModel::class.java)!!)

                    }

                    Log.d(TAG, "onDataChange:getBeautyRow $number")
                    if (beautyRowList.size ==10) {
                        beautyRow = true
                        number++
                    }
                }
            }
            FirebaseDatabase.getInstance().getReference("beauty").addValueEventListener(listioner)
        }


    }

    fun getHomeEssentialSingle(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: HomeEssentialSingle ${error.message}")
                    homeEssentialsSingle = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val product = snapshot.getValue(SmartphoneCatModel::class.java)!!
//                    downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                    homeEssentialSingleList.add(snapshot.getValue(SmartphoneCatModel::class.java)!!)
                    homeEssentialsSingle = true
                    number++
                    Log.d(TAG, "onDataChange:getHomeEssentialSingle $number")
                }
            }

            FirebaseDatabase.getInstance().getReference("home_essentials/he05").addValueEventListener(listioner)
        }

    }

    fun getHomeEssentialFour(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: HomeEssentialFour ${error.message}")
                    homeEssentialsFour = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        homeEssentialFourList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    Log.d(TAG, "onDataChange:getHomeEssentialFour $number")
                    if (homeEssentialFourList.size == 4){
                        homeEssentialsFour = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("home_essentials").addValueEventListener(listioner)
        }

    }

    fun getElectronicsRow(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ElectronicsRow ${error.message}")
                    electronicsRow = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        electronicsRowList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    Log.d(TAG, "onDataChange:getElectronicsRow $number")
                    if (electronicsRowList.size == 10) {
                        electronicsRow = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("electronics").addValueEventListener(listioner)
        }

    }

    fun getSportsSingle(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: SportSingle ${error.message}")
                    sportsSingle = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val product = snapshot.getValue(SmartphoneCatModel::class.java)!!
//                    downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                    sportsSingleList.add(snapshot.getValue(SmartphoneCatModel::class.java)!!)
                    sportsSingle = true
                    number++
                    Log.d(TAG, "onDataChange:getSportsSingle $number")
                }
            }

            FirebaseDatabase.getInstance().getReference("sports/st05").addValueEventListener(listioner)
        }

    }

    fun getSportsFour(context: Application){

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: SportsFour ${error.message}")
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        sportsFourList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    Log.d(TAG, "onDataChange:getSportsFour $number")
                    if (sportsFourList.size == 4){
                        sportsFour = true
                        number++
                    }
                }
            }
            FirebaseDatabase.getInstance().getReference("sports").addValueEventListener(listioner)
        }

    }

    fun getSmartphoneRow(context: Application) {

        viewModelScope.launch {

            val listener = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: SmartphoneRow ${error.message}")
                    smartphoneRow = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        smartphoneRowList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    Log.d(TAG, "onDataChange: $number")
                    if (smartphoneRowList.size == 10){
                        smartphoneRow = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("smartphone").addValueEventListener(listener)
        }

    }

    fun getToysSingle(context: Application){


        viewModelScope.launch {

            val listener = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ToysSingle ${error.message}")
                    toysSingle = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val product = snapshot.getValue(SmartphoneCatModel::class.java)!!
//                    downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                    toysSingleList.add(snapshot.getValue(SmartphoneCatModel::class.java)!!)
                    toysSingle = true
                    number++
                    Log.d(TAG, "onDataChange:getToysSingle $number")
                }
            }

            FirebaseDatabase.getInstance().getReference("toys/ty05").addValueEventListener(listener)
        }

    }

    fun getToysFour(context: Application){

        viewModelScope.launch {

            val listener = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ToysFour ${error.message}")
                    toysFour = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        toysFourList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }


                    Log.d(TAG, "onDataChange:getToysFour $number")

                    if (toysFourList.size==4){
                        toysFour = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("toys").addValueEventListener(listener)
        }

    }

    fun getWomenSingle(context: Application){

        viewModelScope.launch {

            val listener = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: WomenSingle ${error.message}")
                    womenSingle = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
//                    val product = snapshot.getValue(SmartphoneCatModel::class.java)!!
//                    downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                    womenSingleList.add(snapshot.getValue(SmartphoneCatModel::class.java)!!)
                    womenSingle = true
                    number++
                    Log.d(TAG, "onDataChange:getWomenSingle $number")
                }
            }

            FirebaseDatabase.getInstance().getReference("women's_essentials/we05").addValueEventListener(listener)
        }

    }

    fun getWomenFour(context: Application){

        viewModelScope.launch {

            val listener = object  :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: WomenFour ${error.message}")
                    womenFour = true
                    number++
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
//                        val product = item.getValue(SmartphoneCatModel::class.java)!!
//                        downloadImageFromUrl(product.image1TF!!,context,product.productID!!)
                        womenFourList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }
                    Log.d(TAG, "onDataChange:getWomenFour $number")
                    if (womenFourList.size == 4){
                        womenFour = true
                        number++
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("women's_essentials").addValueEventListener(listener)
        }

    }





    fun signInWithPhoneAuthCred(credential: PhoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                processBool = false
                goToMainBool= true
                Log.d("Pinkesh", "signInWithPhoneAuthCred: Verification Success!!!")
            }else{

                snakBarSignUpFalse = true
                Log.d("Pinkesh", "signInWithPhoneAuthCred: Verification Failed!!!")
            }
        }
    }


    fun getAllCategories() :MutableList<CategoryModel> {

        categoryBool = true
        val list: MutableList<CategoryModel> = mutableListOf()

//        viewModelScope.launch {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (item in dataSnapshot.children){
//                    list.add(item.getValue(CategoryModel::class.java)!!)
//                    if (list.size==10){
//
//                    }
//                }
//
//                Log.d(TAG, "getAllCategories: OnData change.... ${dataSnapshot.toString()}")
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                categoryBool = true
//                Log.d(TAG, "getAllCategories: OnData Error.... ${databaseError.message}")
//
//            }
//        }
//            val path = FirebaseDatabase.getInstance().getReference("category")
//
//            path.addValueEventListener(postListener)
//
//            Log.d(TAG, "getAllCategories: Size of list is ${list.size}")
//        }
        return list
    }



    fun getSingleProduct(cat :String, productID :String) :SmartphoneCatModel?{

        var item :SmartphoneCatModel? = null

        val listener = object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: From getSingle Product::: ${error.message}")
                productBool = true
                productBool = false
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                itemDetails = snapshot.getValue(SmartphoneCatModel::class.java)
                productBool = true
            }
        }

        FirebaseDatabase.getInstance().getReference("${cat}/${productID}").addValueEventListener(listener)

        return item
    }

    //---------------------------Save images in External Storage--------------------------------//

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String, application: Application): File? { // File name like "image.png"

        saveImageIntoPNG(bitmap = bitmap,application,fileNameToSave)

        val ecomFolder = File (Environment.getExternalStorageDirectory(), "ECommerce")
        if (!ecomFolder.exists()){
            ecomFolder.mkdirs()
        }

        var file: File? = null
        return try {

            val path = "${Environment.getExternalStorageDirectory()}/ECommerce"
            file = File(path, "${ fileNameToSave }.jpg")
            file.createNewFile()
            viewModelScope.launch {



                var ff :FileOutputStream? = null

                try {
                    ff =   FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,ff)
                }catch (e:Exception){
                    e.printStackTrace()
                }finally {
                    try {
                        ff?.close()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }


            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }

    }

    fun downloadImageFromUrl(src:String, application: Application, name:String) : Bitmap {
        numberTwo++
        Log.d(TAG, "downloadImageFromUrl: Start")
        try {

            var bitmap : Bitmap = BitmapFactory.decodeResource(application.resources, R.drawable.shoestwelve)
            viewModelScope.launch {

                val path = "${Environment.getExternalStorageDirectory()}/ECommerce"
                val file = File(path, "${ name }.jpg")

                if (!file.exists()){

                    Thread(Runnable{
                        val url = URL(src)
                        val connection : HttpURLConnection = url.openConnection() as HttpURLConnection

                        try {
                            connection.doInput = true
                            connection.connect()

                            val inputStream  = connection.inputStream
                            bitmap = BitmapFactory.decodeStream(inputStream)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                        bitmapToFile(bitmap = bitmap, name,application = application)
                    }).start()

                    var i = 0
                    i++
                    Log.d("DKK", "downloadImageFromUrl: TTTTTTTTTTTTTTTTTTTTTTTTTTTTT  ::::::  $numberTwo")
                }else{
                    var i = 0
                    i++
                    Log.d("DKK", "downloadImageFromUrl: FFFFFFFFFFFFFFFFFFFFFFFFFFFFF  :::::: $numberTwo")
                }

                if (numberTwo == 70) {
                    finalBool = true
                }
            }

            return bitmap


        }catch (e: IOException){
            e.printStackTrace()

            if (numberTwo == 70) {
                finalBool = true
            }
            return BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.shoestwelve)
        }


    }


    fun saveImageIntoPNG(bitmap: Bitmap, application: Application, name: String) :String{
        val cw :ContextWrapper = ContextWrapper(application.baseContext)
        val directory :File = cw.getDir("ECom", MODE_PRIVATE)
        val myPath = File(directory,"${name}.jpg")

        var fos :FileOutputStream? = null

        try {
            viewModelScope.launch {

                fos = FileOutputStream(myPath)
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
                Log.d(TAG, "saveImageIntoPNG: :: :: :: ${myPath.absolutePath}")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            try {
              fos?.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    val allCatProducts :MutableList<SmartphoneCatModel> = mutableListOf()
    fun getAllProductFromCat(category :String) :MutableList<SmartphoneCatModel> {

        val list :MutableList<SmartphoneCatModel> = mutableListOf()
        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: All Products ${error.message}")
                    electronicsRow = true
                    number++
                    specialCatAllProduct = true
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    allCatProducts.clear()
                    for (item in snapshot.children){
                        list.add(item.getValue(SmartphoneCatModel::class.java)!!)
                        allCatProducts.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    Log.d(TAG, "onCancelled: All Products Complete")
                    specialCatAllProduct = true

                }
            }

            FirebaseDatabase.getInstance().getReference(category).addValueEventListener(listioner)
        }

        return list

    }

    @JvmName("getUserData1")
    fun getUserData()  {

        viewModelScope.launch {

            val uid = FirebaseAuth.getInstance().currentUser?.uid

            try {

                FirebaseDatabase
                    .getInstance()
                    .getReference("Users")
                    .child(uid!!).get()
                    .addOnSuccessListener{
                        try {

                            userData = it.getValue(UserModel::class.java)!!
                        }catch (e:NullPointerException){
                            Log.d(TAG, "getUserData: From Internal ${e.message}")
                        }finally {
                            userDataBool = true
                            editProfileProcessBool = false
                        }
                    }
            }catch (e :Exception){
                Log.d(TAG, "getUserData: ${e.message}")
                editProfileProcessBool = false
            }
        }


    }


    fun uploadUserData(
        profPic :String?,
        name :String?,
        email :String?,
        phone :String?
    ){
        viewModelScope.launch {
            val userID :String? = FirebaseAuth.getInstance().currentUser?.uid
            val user = UserModel(userID = userID,profPic = profPic,name=name,email = email,phone = phone)

            FirebaseDatabase.getInstance().getReference("Users").child(userID!!).setValue(user).addOnSuccessListener {
                Log.d(TAG, "uploadUserData: User data upload successfull")

                getUserData()
                editProfileProcessBool = false

            }.addOnFailureListener {

                editProfileProcessBool = false
                Log.d(TAG, "uploadUserData: User data upload failed")
            }
        }
    }


    //--------------------------Cart Section----------------------------------------//

    val cartProducts :MutableList<SmartphoneCatModel> = mutableListOf()
    var cartProcessBool by mutableStateOf(false)

    fun getCartProduct() :MutableList<SmartphoneCatModel>{

        viewModelScope.launch {

            val listioner = object  :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: From Cart ${error.message} ")
                    cartProcessBool = false
                    nullInCartBool = false
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    cartProducts.clear()
                    for (item in snapshot.children){
                        cartProducts.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    cartProcessBool = true
                    nullInCartBool = false

                }
            }

            try {
                FirebaseDatabase.getInstance().getReference("cart").child(FirebaseAuth.getInstance().currentUser?.uid!!).addValueEventListener(listioner)

            }catch (
                e:Exception
            ){
                nullInCartBool = true
                Log.d(TAG, "getCartProduct: Null in cart")
            }
        }

        return cartProducts

    }

    var addToCartBool by mutableStateOf(false)

    val timerForAddToCart = object :CountDownTimer(4000,1000){
        override fun onFinish() {
            addToCartBool = false
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "onTick: AddToCartBool Timer Tick")
        }
    }

    fun addToCart(product: SmartphoneCatModel){

        viewModelScope.launch {

            val userID = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseDatabase
                .getInstance()
                .getReference("cart")
                .child(userID!!)
                .child(product.productID!!)
                .setValue(product)
                .addOnSuccessListener {
                    addToCartBool = true
                    timerForAddToCart.start()
                    Log.d(TAG, "addToCart: ADDED TO CART")
                }
        }

    }

    fun deletToCart(product: SmartphoneCatModel){

        viewModelScope.launch {

            FirebaseDatabase
                .getInstance()
                .getReference("cart")
                .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                .child(product.productID!!)
                .removeValue()
                .addOnSuccessListener {
                    addToCartBool = true
                    timerForAddToCart.start()
                }
        }

    }



    //--------------------------------Wishlist Section--------------------------------//
    var wishlistProducts :MutableList<SmartphoneCatModel> = mutableListOf()
    var wishlistProcessBool by mutableStateOf(false)

    fun getwishlistProduct() :MutableList<SmartphoneCatModel>{

        viewModelScope.launch {

            Log.d(TAG, "getwishlistProduct: Entered")

            val listioner = object  :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: From Cart ${error.message} ")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    wishlistProducts.clear()
                    for (item in snapshot.children){
                        wishlistProducts.add(item.getValue(SmartphoneCatModel::class.java)!!)
                        Log.d(TAG, "WishList Product: From Wishlist Inner ")
                    }

                    wishlistProcessBool = true

                }
            }

            try {
                FirebaseDatabase.getInstance().getReference("wishlist").child(FirebaseAuth.getInstance().currentUser?.uid!!).addValueEventListener(listioner)

            }catch (
                e:Exception
            ){
                Log.d(TAG, "getWishListProduct: Null in Wishlist or something went wrong")
            }
        }

        return wishlistProducts
    }

    var addTowishlistBool by mutableStateOf(false)

    val timerForAddToWishlist = object :CountDownTimer(4000,1000){
        override fun onFinish() {
            addTowishlistBool = false
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "onTick: AddToCartBool Timer Tick")
        }
    }

    fun addToWishlist(product: SmartphoneCatModel){
        viewModelScope.launch {

                product.productID?.let { it1 ->
                    FirebaseDatabase
                        .getInstance()
                        .getReference("wishlist")
                        .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .child(it1)
                        .setValue(product)
                        .addOnSuccessListener {
                            addTowishlistBool = true
                            timerForAddToWishlist.start()
                            Log.d(TAG, "addToWishlist: Added TO WISHLIST")
                        }
                }

        }
    }

    fun deletToWishlist(product: SmartphoneCatModel){
        viewModelScope.launch {

            FirebaseDatabase
                .getInstance()
                .getReference("wishlist")
                .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                .child(product.productID!!)
                .setValue(null)
                .addOnSuccessListener {
                    addTowishlistBool = true
                    timerForAddToWishlist.start()
                    Log.d(TAG, "deletToWishlist: DELETED FROM WISHLIST")
                }
        }
    }


    //-----------------------------For Search Feature------------------------------------------//

    val searchAllProductsList :MutableList<SmartphoneCatModel> = mutableListOf()
    val filteredList :MutableList<SmartphoneCatModel> = mutableListOf()
    fun searchFilter(newChar :String = "") :MutableList<SmartphoneCatModel>{


        filteredList.clear()
        for (char in searchAllProductsList){
            if (char.title?.lowercase()?.contains(newChar.lowercase()) == true){
                filteredList.add(char)
            }
        }

        return filteredList
    }

    fun getAllProductsName (){

        getAllProductForSearchList("beauty")
        getAllProductForSearchList("electronics")
        getAllProductForSearchList("food")
        getAllProductForSearchList("home_essentials")
        getAllProductForSearchList("man's_essentials")
        getAllProductForSearchList("smartphone")
        getAllProductForSearchList("sports")
        getAllProductForSearchList("toys")
        getAllProductForSearchList("women's_essentials")


    }

    fun getAllProductForSearchList(category :String)  {

        viewModelScope.launch {

            val listioner = object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: All Products From get All Search Items ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        searchAllProductsList.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference(category).addValueEventListener(listioner)
        }
    }


    //---------------Notification Section----------------------------------------------------------//

    fun fireNotificationFunction(context :Context, msg :String, category: String, productID: String){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Creating channel for notification
            val channelName = "BUYCHANNEL"
            val channelID = "NOTIBUY"
            val descriptionText = "This channel fire notification whenever user buy product."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, channelName,importance).apply {
                description = descriptionText
            }

            //Register channel to the notification manager
            val notificationManager:NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://google.com/category=$category/productID=$productID".toUri(),
                context,
                MainActivity::class.java
            )

            val pendingIntent :PendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
            }


            val bit = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
            val builder = NotificationCompat.Builder(context,channelID)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(bit)
                .setSubText(msg)
                .setContentTitle("Fake Payment Successful")
                .setSubText("From MY SHOP")
                .setContentText(msg)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(0,builder)

        }
    }


    //-----------------My Order Section---------------------------------------------//
    val orderProducts :MutableList<SmartphoneCatModel> = mutableListOf()
    var orderProcessBool by mutableStateOf(false)
    var nullInOrderBool by mutableStateOf(false)

    fun getOrderProduct() :MutableList<SmartphoneCatModel>{

        viewModelScope.launch {

            val listioner = object  :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: From Cart ${error.message} ")
                    orderProcessBool = true
                    nullInOrderBool = true
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    orderProducts.clear()
                    for (item in snapshot.children){
                        orderProducts.add(item.getValue(SmartphoneCatModel::class.java)!!)
                    }

                    orderProcessBool = true
                    nullInOrderBool = true

                }
            }

            try {
                FirebaseDatabase.getInstance().getReference("order").child(FirebaseAuth.getInstance().currentUser?.uid!!).addValueEventListener(listioner)

            }catch (
                e:Exception
            ){
                nullInOrderBool = true
                Log.d(TAG, "getCartProduct: Null in Order")
            }
        }

        return orderProducts


    }

    var addToOrderBool by mutableStateOf(false)

    val timerForAddToOrder = object :CountDownTimer(4000,1000){
        override fun onFinish() {
            addToOrderBool = false
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "onTick: AddToOrderBool Timer Tick")
        }
    }

    fun addToOrder(product: SmartphoneCatModel){

        viewModelScope.launch {

            val userID = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseDatabase
                .getInstance()
                .getReference("order")
                .child(userID!!)
                .child(product.productID!!)
                .setValue(product)
                .addOnSuccessListener {
                    addToOrderBool = true
                    timerForAddToOrder.start()
                    Log.d(TAG, "addToCart: ADDED TO Order")
                }
        }

    }

    fun deletToOrder(product: SmartphoneCatModel){

        viewModelScope.launch {

            FirebaseDatabase
                .getInstance()
                .getReference("order")
                .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                .child(product.productID!!)
                .removeValue()
                .addOnSuccessListener {
                    addToOrderBool = true
                    timerForAddToOrder.start()
                }
        }

    }


    //-------------------- Addresses ------------------------------------------//
    var addressBool by mutableStateOf(false)
    var titleForAddress by mutableStateOf("")
    var addressesTF by mutableStateOf("")
    var listOfAddresses :MutableList<AddressModel> = mutableListOf<AddressModel>()
    var selectedAddress by mutableStateOf(AddressModel())


    val timerAddress = object : CountDownTimer(4000,1000) {
        override fun onFinish() {
            addressBool = false
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "onTick: Address Snackbar bool:::")
        }
    }


    fun uploadAddress(){
        viewModelScope.launch {

            val ref = FirebaseDatabase
                .getInstance()
                .getReference("Addresses/${FirebaseAuth.getInstance().currentUser?.uid}")
                .child(titleForAddress)

            val value :AddressModel = AddressModel(title = titleForAddress, stuff = addressesTF)
            ref.setValue(value).addOnSuccessListener {
                addressBool = true
                timerAddress.start()
            }
        }
    }

    fun getAddresses(){
        viewModelScope.launch {
            val listioner = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        listOfAddresses.clear()
                        for (item in snapshot.children){
                            Log.d(TAG, "onDataChange: in Try")
//                        returnList.add(item.getValue(MeasurementModel::class.java)!!)
                            listOfAddresses.add(item.getValue(AddressModel::class.java)!!)
                        }

                    }catch (e:Exception){
                        listOfAddresses.clear()
                        Log.d(TAG, "onDataChange: In Catch")
//                    returnList.add(snapshot.getValue(MeasurementModel::class.java)!!)
                        listOfAddresses.add(snapshot.getValue(AddressModel::class.java)!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: From Addresses ${error.message}")
                }
            }

            val ref = FirebaseDatabase
                .getInstance()
                .getReference("Addresses/${FirebaseAuth.getInstance().currentUser?.uid}")

            ref.addValueEventListener(listioner)
        }
    }
}
























