package com.p2m.ecommercefinal

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun FirstEntryScreen(modifier: Modifier, myViewModel: MyViewModel, navController: NavController){


    val user = FirebaseAuth.getInstance().currentUser
    if (user != null){
        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
    }else {

        if (myViewModel.snakBarLoginFalse){
            myViewModel.timerForLoginFailed.start()

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {

                Snackbar (
                    shape = RoundedCornerShape(3.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    content = { Text(
                        text = "Failed to login!",
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        textAlign = TextAlign.Start
                    )
                    }
                )


            }
        }

        if (myViewModel.snakBarSignUpFalse){
            myViewModel.timerForLoginFailed.start()

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {

                Snackbar (
                    shape = RoundedCornerShape(3.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    content = { Text(
                        text = "Failed to Sign Up!",
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        textAlign = TextAlign.Start
                    )
                    }
                )


            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Welcome",
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 70.sp,
                textAlign = TextAlign.Center
            )




            Spacer(modifier = modifier.height(5.dp))

            Card(modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(Color.White),
                elevation = 5.dp
            ) {


                val selectedTab = remember{ mutableStateOf(1) }
                fun signUpClick(){
                    myViewModel.loginBool = false
                    myViewModel.signUpBool = true
                    selectedTab.value = 1
                }

                fun loginClick(){
                    myViewModel.signUpBool = false
                    myViewModel.loginBool = true
                    selectedTab.value = 0
                }


                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab.value
                    ) {
                        Tab(
                            selected = selectedTab.value == 0,
                            onClick = { loginClick() },
                            modifier = modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Login", modifier = modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                        }

                        Tab(
                            selected = selectedTab.value == 1
                            , onClick = { signUpClick() },
                            modifier = modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Sign Up",
                                modifier = modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center
                            )
                        }

                        Text(
                            text = "Due to some problem with SHA Certificate you won't be able to register with google and phone.\n Sample account email: pink@gmail.com and password: PInkesh1820@",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }


                    if (myViewModel.loginBool){
                        LoginCompo(
                            modifier = modifier,
                            myViewModel = myViewModel,
                            navController = navController
                        )
                    }else if (myViewModel.signUpBool){
                        SignUpCompo(
                            modifier = modifier,
                            myViewModel = myViewModel,
                            navController = navController
                        )
                    }
                }



            }
        }
    }
}

@Composable
fun LoginCompo(modifier: Modifier, myViewModel: MyViewModel, navController: NavController){

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val scroll =  rememberScrollState()
    val scroll2 =  rememberScrollState()
    val scroll3 =  rememberScrollState()

    fun loginWithEmailAndPasswordFunction(){
        myViewModel.processBool = true
        auth.signInWithEmailAndPassword(myViewModel.emailLoginTF, myViewModel.passwordLoginTF).addOnCompleteListener {
            if (it.isSuccessful){
                myViewModel.processBool = false
                myViewModel.loginInvalidTextShowBool = false
                navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
            }else{

                myViewModel.processBool = false
                myViewModel.loginInvalidTextShowBool = true
                myViewModel.emailLoginTF = ""
                myViewModel.passwordLoginTF = ""
                myViewModel.snakBarLoginFalse = true

            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (myViewModel.loginInvalidTextShowBool){
            Text(
                text = myViewModel.loginInvalidTextShowText,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }


        OutlinedTextField(
            value = myViewModel.emailLoginTF,
            onValueChange = { myViewModel.emailLoginTF = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(text = "Email") }
        )

        OutlinedTextField(value = myViewModel.passwordLoginTF, onValueChange = {myViewModel.passwordLoginTF = it}, modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
            label = { Text(text = "Password")
            })

        Button(onClick = {loginWithEmailAndPasswordFunction()}, modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)) {
            Text(text = "Login")
        }
    }


}

@Composable
fun SignUpCompo(modifier: Modifier, myViewModel: MyViewModel, navController: NavController){

    //----------------------------------------------------Sign Up with email and password------------------------------------------------//
    val auth = FirebaseAuth.getInstance()
    var password:String = ""

    fun checkPass(){

        if (myViewModel.passwordSignUpTE.equals(myViewModel.confirmPassWordTE)){
            myViewModel.passConfirmBool = true
        }else {
            myViewModel.passConfirmBool = false
        }

        if (!myViewModel.passConfirmBool){
            myViewModel.passConfirmBoolText = "Please write the password that you have entered previously!"
        }

        if (myViewModel.passConfirmBool){
            if (myViewModel.passwordSignUpTE.contains("~")||
                myViewModel.passwordSignUpTE.contains("!")||
                myViewModel.passwordSignUpTE.contains("@")||
                myViewModel.passwordSignUpTE.contains("#")||
                myViewModel.passwordSignUpTE.contains("$")||
                myViewModel.passwordSignUpTE.contains("%")||
                myViewModel.passwordSignUpTE.contains("^")||
                myViewModel.passwordSignUpTE.contains("&")||
                myViewModel.passwordSignUpTE.contains("*")
            ){

                if (myViewModel.passwordSignUpTE.contains("A")||
                    myViewModel.passwordSignUpTE.contains("B")||
                    myViewModel.passwordSignUpTE.contains("C")||
                    myViewModel.passwordSignUpTE.contains("D")||
                    myViewModel.passwordSignUpTE.contains("E")||
                    myViewModel.passwordSignUpTE.contains("F")||
                    myViewModel.passwordSignUpTE.contains("G")||
                    myViewModel.passwordSignUpTE.contains("H")||
                    myViewModel.passwordSignUpTE.contains("I")||
                    myViewModel.passwordSignUpTE.contains("J")||
                    myViewModel.passwordSignUpTE.contains("K")||
                    myViewModel.passwordSignUpTE.contains("L")||
                    myViewModel.passwordSignUpTE.contains("M")||
                    myViewModel.passwordSignUpTE.contains("N")||
                    myViewModel.passwordSignUpTE.contains("O")||
                    myViewModel.passwordSignUpTE.contains("P")||
                    myViewModel.passwordSignUpTE.contains("Q")||
                    myViewModel.passwordSignUpTE.contains("R")||
                    myViewModel.passwordSignUpTE.contains("S")||
                    myViewModel.passwordSignUpTE.contains("T")||
                    myViewModel.passwordSignUpTE.contains("U")||
                    myViewModel.passwordSignUpTE.contains("V")||
                    myViewModel.passwordSignUpTE.contains("W")||
                    myViewModel.passwordSignUpTE.contains("X")||
                    myViewModel.passwordSignUpTE.contains("Y")||
                    myViewModel.passwordSignUpTE.contains("Z")
                ){

                    if (
                        myViewModel.passwordSignUpTE.contains("0")||
                        myViewModel.passwordSignUpTE.contains("1")||
                        myViewModel.passwordSignUpTE.contains("2")||
                        myViewModel.passwordSignUpTE.contains("3")||
                        myViewModel.passwordSignUpTE.contains("4")||
                        myViewModel.passwordSignUpTE.contains("5")||
                        myViewModel.passwordSignUpTE.contains("6")||
                        myViewModel.passwordSignUpTE.contains("7")||
                        myViewModel.passwordSignUpTE.contains("8")||
                        myViewModel.passwordSignUpTE.contains("9")
                    ){
                        password = myViewModel.passwordSignUpTE
                        myViewModel.passSignUpCharErrorBool = true
                    }else myViewModel.spellCheckBool = true

                }else myViewModel.spellCheckBool = true

            }else myViewModel.spellCheckBool = true
        }else myViewModel.spellCheckBool = true


    }

    fun signUpWithEmailAndPasswordFunction(){

        checkPass()

        if (myViewModel.passSignUpCharErrorBool){

            myViewModel.processBool= true
            auth.createUserWithEmailAndPassword(myViewModel.emailSignUpTF, myViewModel.passwordSignUpTE)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        myViewModel.processBool = false
                        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
                    }else{

                        myViewModel.processBool = false
                        myViewModel.snakBarSignUpFalse = true
                        Log.d("Pinkesh", "signUpWithEmailAndPasswordFunction: Unsuccessful SignUP")
                    }
                }
        }
    }





    //----------------------------------------------------Google Sign In--------------------------------------------------------------------//

    val TAG = "Pinkesh"
    val id = "991430819686-4hek21iaur6abenhuv38npd2tv1mvmsl.apps.googleusercontent.com"
    val context = LocalContext.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(id)
        .requestEmail()
        .build()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        Log.d(TAG, "SignUpCompo: Reached at result.")
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            myViewModel.processBool= true
            val account = task.getResult(ApiException::class.java)
            val credentials= GoogleAuthProvider.getCredential(account.idToken!!, null)
            FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener {
                if (it.isSuccessful){
                    myViewModel.processBool = false
                    navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
                }else{
                    myViewModel.processBool = false
                    myViewModel.snakBarSignUpFalse = true
                }
            }
        }catch (e: ApiException){
            Log.d(TAG, "SignUpCompo: API Exception::: ${e.message}")
        }
    }

    fun launchForGoogleSignIn(){
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }


    if (myViewModel.processBool) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
            myViewModel.timerForFinishProcessBar.start()
        }
    }else{

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = myViewModel.emailSignUpTF,
                onValueChange = { myViewModel.emailSignUpTF = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text(text = "Enter Your Email") }
            )

            if (myViewModel.spellCheckBool){
                Text(text = myViewModel.passSignUpCharErrorText,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 3.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Start,
                    color = Color.Red
                )
            }else {
                Text(text = myViewModel.passSignUpCharErrorText,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 3.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Start
                )
            }

            OutlinedTextField(
                value = myViewModel.passwordSignUpTE,
                onValueChange = { myViewModel.passwordSignUpTE = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 10.dp),
                label = {
                    Text(text = "Enter Your Password")
                }
            )

            OutlinedTextField(
                value = myViewModel.confirmPassWordTE,
                onValueChange = { myViewModel.confirmPassWordTE = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 2.dp),
                label = {
                    Text(text = "Confirm Your Password")
                },
                trailingIcon = {
                    if (!myViewModel.passConfirmBool)
                        Icon(Icons.Default.Warning,"error", tint = MaterialTheme.colors.error)
                }
            )
            if (!myViewModel.passConfirmBool){
                Text(
                    text = "Password does not match!",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 0.dp, bottom = 10.dp, end = 10.dp)
                        .align(Alignment.End),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }


            Button(onClick = {signUpWithEmailAndPasswordFunction()}, modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)) {
                Text(text = "Sign Up")
            }


            Text(text = "Or Sign Up with",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, bottom = 5.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                IconButton(onClick = { launchForGoogleSignIn() }, modifier = modifier
                    .size(65.dp)
                    .padding(10.dp)) {
                    Image(painter = painterResource(id = R.drawable.google), contentDescription = "Sign up with google")
                }

                IconButton(onClick = { navController.navigate(Screen.PhoneAuthScreen.route){launchSingleTop = true} }, modifier = modifier
                    .size(65.dp)
                    .padding(10.dp)) {
                    Image(painter = painterResource(id = R.drawable.phone), contentDescription ="Sign up with phone" )
                }
            }
        }
    }

}

@Composable
fun PhoneEntered(modifier: Modifier, myViewModel: MyViewModel, navController: NavController){
    val maxCharOfCountryCode = 2
    val maxCharOfPhone = 10
    val context = LocalContext.current

    if (myViewModel.processBool) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
            myViewModel.timerForFinishProcessBar.start()
        }
    }else{

        if (myViewModel.goToMainBool){
            myViewModel.processBool = false
            navController.navigate(Screen.MainScreen.route) {
                launchSingleTop = true
            }
        }

        val callbackForPhone = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                myViewModel.signInWithPhoneAuthCred(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("Pinkesh", "onVerificationFailed: Failed to sign Up ::::  ${p0.message}")
            }


            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                myViewModel.showOTPSectionBool = true
                myViewModel.verID = p0
            }
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+${myViewModel.countryCode+myViewModel.phone}")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(LocalContext.current as Activity)
            .setCallbacks(callbackForPhone)
            .build()

        fun processPhoneSignIn(){
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        fun finalPhoneSignIn() {

            myViewModel.processBool = true

            val cred = PhoneAuthProvider.getCredential(myViewModel.verID, myViewModel.oneTimePass)
            myViewModel.signInWithPhoneAuthCred(cred)
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 50.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Welcome",
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 70.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = modifier.height(5.dp))

            Card(modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(Color.White),
                elevation = 5.dp
            ) {

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Sign up with phone",
                            modifier.padding(15.dp),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "+",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                modifier = modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                            )

                            OutlinedTextField(value = myViewModel.countryCode,
                                onValueChange = { if (it.length<=maxCharOfCountryCode) myViewModel.countryCode = it },
                                modifier = modifier
                                    .width(110.dp)
                                    .padding(top = 10.dp, bottom = 10.dp, end = 2.dp), label = {
                                    Text(text = "Country Code", fontSize = 12.sp) },
                                textStyle = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                            )
                        }


                        OutlinedTextField(value = myViewModel.phone,
                            onValueChange = { if (it.length<= maxCharOfPhone) myViewModel.phone = it },
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp), label = {
                                Text(text = "Enter your phone") },
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        )
                        Text(
                            text = "Due to some problem with SHA Certificate you won't be able to register with google and phone.\n Sample account email: pink@gmail.com and password: PInkesh1820@",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )

                    }

                    if (myViewModel.showOTPSectionBool){
                        myViewModel.timer.start()
                        Button(
                            onClick = {  },
                            modifier = modifier.padding(10.dp)
                        ) {
                            Text(text = "Code sent")
                        }

                        Text(
                            text = myViewModel.otpMSG,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 30.dp),
                            textAlign = TextAlign.Center
                        )

                    }else{
                        Button(
                            onClick = { processPhoneSignIn() },
                            modifier = modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)
                        ) {
                            Text(text = "Send Code")
                        }
                    }



                    if (myViewModel.showOTPSectionBool){

                        OutlinedTextField(
                            value = myViewModel.oneTimePass,
                            onValueChange = { myViewModel.oneTimePass = it },
                            modifier = modifier
                                .width(150.dp)
                                .padding(10.dp),
                            label = { Text(text = "Enter code") }
                        )

                        Button(
                            onClick = {
                                finalPhoneSignIn()
                            },
                            modifier = modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)
                        ) {
                            Text(text = "Sign up")
                        }

                    }

                }
            }
        }
    }

}