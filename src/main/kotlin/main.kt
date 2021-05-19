import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.`$`
import kotlinx.coroutines.*
import org.jetbrains.skija.Image
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

var auth = Authentication()
var firebase = Firebase()
var firebaseAuth = firebase.Authentication()
var firestore = firebase.Firestore()
var tests = hashMapOf<String, Test>()

var deactivated = true

fun main() = Window(title = "CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(1080, 712)) {
    var noUsername by remember {
        mutableStateOf(false)
    }

    var authenticated by remember {
        mutableStateOf(false)
    }

    var currentPage by remember {
        mutableStateOf(0)
    }

    var test by remember {
        mutableStateOf("")
    }

    MaterialTheme {
        if (authenticated) {
            Row {
                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.0831f)
                    .background(Color(66, 133, 244)),
                    Arrangement.spacedBy(30.dp)
                ) {
                    var warning by remember {
                        mutableStateOf(3)
                    }

                    IconButton(modifier = Modifier.padding(top = 10.dp).align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        if (deactivated) {
                            currentPage = 0
                        } else {
                            warning = 0
                        }
                    }) {
                        Icon(painter = svgResource("home-icon.svg"), null, modifier = Modifier.size(50.dp, 50.dp))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        if (deactivated) {
                            currentPage = 1
                        } else {
                            warning = 1
                        }
                    }) {
                        Icon(painter = svgResource("assignment-icon.svg"), null, modifier = Modifier.size(50.dp, 50.dp))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        if (deactivated) {
                            currentPage = 2
                        } else {
                            warning = 2
                        }
                    }) {
                        Icon(painter = svgResource("settings-icon.svg"), null, modifier = Modifier.size(50.dp, 50.dp))
                    }

                    if (warning != 3) {
                        Window(
                            title = "CSSA Test Portal | Authentication Error",
                            icon = loadImageResource("CSSA.png"),
                            size = IntSize(450, 195),
                            onDismissRequest = { warning = 3 }
                        ) {
                            Column (Modifier.align(Alignment.CenterHorizontally)) {
                                androidx.compose.material.Text(
                                    text = "You're taking a test! If you exit now, your test will submit. Are you sure you want to exit?",
                                    Modifier.align(Alignment.CenterHorizontally).padding(20.dp),
                                    fontSize = 15.sp, textAlign = TextAlign.Center
                                )

                                Button(
                                    onClick = { currentPage = warning; warning = 3; deactivated = true; AppManager.focusedWindow!!.close() },
                                    Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
                                ) {
                                    androidx.compose.material.Text(text = "Ok", fontSize = 15.sp, textAlign = TextAlign.Center)
                                }

                                Button(
                                    onClick = { warning = 3; AppManager.focusedWindow!!.close() },
                                    Modifier.align(Alignment.CenterHorizontally).padding(top = 50.dp),
                                ) {
                                    androidx.compose.material.Text(text = "Cancel", fontSize = 15.sp, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }

                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                    Arrangement.spacedBy(50.dp)) {

                    when (currentPage) {
                        0 -> { // Home Page

                            Text(text = "Welcome, ${auth.username}!", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            Column(Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.6f)
                                .align(Alignment.CenterHorizontally)
                                .background(Color(243, 243, 243))) {

//                                Text("First Mini-Competition", fontSize = 20.sp)
//
//                                if (tests.size == 0) {
//                                    val userDocResponse = firestore.get("users/${firebase.uid}")
//                                    val eventSequence = Regex("""(?<="event.": \{\n {6}"stringValue": ")(?!None|(.*)!).*(?=")""").findAll(userDocResponse)
//                                    eventSequence.forEach {
//                                        tests[it.value] = Test(it.value)
//                                    }
//                                }
//
//                                tests.forEach { (event, _) ->
//                                    TextButton(onClick = {
//                                        currentPage = 3
//                                        test = event
//                                    }) {
//                                        Text(event, color = Color.Black)
//                                    }
//                                }

                            }

                        }

                        1 -> { // Competition Page

                            Text(text = "My Competitions", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            Column(Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.6f)
                                .align(Alignment.CenterHorizontally)
                                .background(Color(243, 243, 243))
                            ) {

//                                Text("First Mini-Competition", fontSize = 20.sp)
//
//                                if (tests.size == 0) {
//                                    val userDocResponse = firestore.get("users/${firebase.uid}")
//                                    val eventSequence = Regex("""(?<="event.": \{\n {6}"stringValue": ")(?!None|(.*)!).*(?=")""").findAll(userDocResponse)
//                                    eventSequence.forEach {
//                                        tests[it.value] = Test(it.value)
//                                    }
//                                }
//
//                                tests.forEach { (event, _) ->
//                                    TextButton(onClick = {
//                                        currentPage = 3
//                                        test = event
//                                    }) {
//                                        Text(event, color = Color.Black)
//                                    }
//                                }

                            }

                        }

                        2 -> { // Settings Page

                            Text(text = "Settings", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                onClick = {
                                    auth = Authentication()
                                    authenticated = false
                                }
                            ) {
                                Text("Sign Out")
                            }

                        }

                        3 -> { // Test-Taking Page

                            Text(text = test, Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            val testUI = tests[test]!!.UI()

                            Column(Modifier
                                .fillMaxWidth(0.831f)
                                .fillMaxHeight(0.9f)
                                .align(Alignment.CenterHorizontally)
                            ) {

                                testUI.load()

                                GlobalScope.launch {
                                    while (!deactivated) {
                                        delay(1500)

                                        if (deactivated) {
                                            delay(60000)

                                            println("Returning to home screen...")

                                            currentPage = 1

                                            this.cancel()
                                        }
                                    }
                                }

                            }

                        }
                    }
                }
            }
        } else {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {

                Row(Modifier.fillMaxSize().align(Alignment.CenterHorizontally)) {

                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CSSA Test Portal", modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp), fontSize = 40.sp, textAlign = TextAlign.Right)

                        Column(Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(Color(0xF0, 0xF0, 0xF0), RoundedCornerShape(8.dp))
                            .border(3.dp, Color(33, 33, 33), RoundedCornerShape(8.dp))
                        ) {

                            Column(Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp, 15.dp, 10.dp, 15.dp), Arrangement.spacedBy(15.dp)
                            ) {

                                if (noUsername) {
                                    var username by remember {
                                        mutableStateOf("")
                                    }

                                    var email by remember {
                                        mutableStateOf("")
                                    }

                                    var fName by remember {
                                        mutableStateOf("")
                                    }

                                    var lName by remember {
                                        mutableStateOf("")
                                    }

                                    var password by remember {
                                        mutableStateOf("")
                                    }

                                    var confirmPassword by remember {
                                        mutableStateOf("")
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                                        Column(Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)) {
                                            Text("Username", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = username,
                                                onValueChange = { username = it },
                                            )
                                        }

                                        Column(Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp)) {
                                            Text("Email", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = email,
                                                onValueChange = { email = it },
                                            )
                                        }
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                                        Column(Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)) {
                                            Text("First Name", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = fName,
                                                onValueChange = { fName = it },
                                            )
                                        }

                                        Column(Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp)) {
                                            Text("Last Name", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = lName,
                                                onValueChange = { lName = it },
                                            )
                                        }
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                                        Column(Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)) {
                                            Text("Password", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = password,
                                                onValueChange = { password = it },
                                            )
                                        }

                                        Column(Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp)) {
                                            Text("Confirm Password", textAlign = TextAlign.Left)

                                            TextField(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                value = confirmPassword,
                                                onValueChange = {
                                                    confirmPassword = it
                                                },
                                            )
                                        }
                                    }

                                    if ((password != confirmPassword || confirmPassword == "") && confirmPassword != "") {
                                        Row {
                                            Text(text = "Passwords do not match!", textAlign = TextAlign.Left, color = Color(188, 88, 88))
                                        }
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                                        Column {
                                            var signUpPopup by remember {
                                                mutableStateOf(0)
                                            }

//                                            var loading by remember {
//                                                mutableStateOf(false)
//                                            }

                                            Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                                onClick = {
                                                    GlobalScope.launch {
                                                        val authState = auth.createAccount(fName, lName, username, email, password)

                                                        if (authState[0] as Boolean) {
//                                                          loading = false

                                                            authenticated = true
                                                        } else {
//                                                          loading = false

                                                            signUpPopup = 1

                                                            println("Error creating account")
                                                        }

                                                        this.cancel()
                                                    }
                                                }
                                            ) {
                                                Text("Sign Up")
                                            }

                                            TextButton(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                                onClick = {
                                                    noUsername = false
                                                }
                                            ) {
                                                Text("Already have an account? Sign in!")
                                            }

                                            if (signUpPopup != 0) {
                                                Window(
                                                    title = "CSSA Test Portal | Authentication Error",
                                                    icon = loadImageResource("CSSA.png"),
                                                    size = IntSize(450, 195),
                                                    onDismissRequest = { signUpPopup = 0 }
                                                ) {
                                                    Column (Modifier.align(Alignment.CenterHorizontally)) {
                                                        androidx.compose.material.Text(
                                                            text = when (signUpPopup) {
                                                                2 -> "That email is already being used by another account, please sign in or use a different email!"
                                                                3 -> "That username is already being used by another account, please sign in or use a different username!"
                                                                else -> "Unknown error signing up, please retry after a while or restart the testing portal!"
                                                            },
                                                            Modifier.align(Alignment.CenterHorizontally).padding(20.dp),
                                                            fontSize = 15.sp, textAlign = TextAlign.Center
                                                        )

                                                        Button(
                                                            onClick = { signUpPopup = 0; AppManager.focusedWindow!!.close() },
                                                            Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
                                                        ) {
                                                            androidx.compose.material.Text(text = "Ok", fontSize = 15.sp, textAlign = TextAlign.Center)
                                                        }
                                                    }
                                                }
                                            }

//                                            if (loading) {
//                                                Column (Modifier.fillMaxSize().background(Color(0,0,0,4)).zIndex(1f)) {
//                                                    Column(Modifier.fillMaxSize(0.27f).background(Color.White).align(Alignment.CenterHorizontally)) {
//                                                        Text(text = "Loading...")
//
//                                                        CircularProgressIndicator(Modifier.wrapContentWidth(Alignment.CenterHorizontally))
//                                                    }
//                                                }
//                                            }
                                        }
                                    }
                                } else {
                                    var username by remember {
                                        mutableStateOf("")
                                    }

                                    var password by remember {
                                        mutableStateOf("")
                                    }

                                    var signInPopup by remember {
                                        mutableStateOf(0)
                                    }

                                    Text("Username", textAlign = TextAlign.Left)

                                    TextField(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        value = username,
                                        onValueChange = { username = it }
                                    )

                                    Text("Password", textAlign = TextAlign.Left)

                                    TextField(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        value = password,
                                        onValueChange = { password = it }
                                    )

                                    Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                        onClick = {
                                            GlobalScope.launch {
                                                signInPopup = when {
                                                    username == "" -> {
                                                        2
                                                    }

                                                    password == "" -> {
                                                        3
                                                    }

                                                    else -> {
                                                        val trySignIn = auth.manualSignIn(username, password)

                                                        if (trySignIn == 0) {
                                                            authenticated = true
                                                        }

                                                        trySignIn
                                                    }
                                                }

                                                this.cancel()
                                            }
                                        }) {
                                        Text("Sign In")
                                    }

                                    TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                                        onClick = {
                                            noUsername = true
                                        }) {
                                        Text("Don't have an account? Sign up!")
                                    }

                                    if (signInPopup != 0) {
                                        Window(
                                            title = "CSSA Test Portal | Authentication Error",
                                            icon = loadImageResource("CSSA.png"),
                                            size = IntSize(450, 195)
                                        ) {
                                            Column (Modifier.align(Alignment.CenterHorizontally)) {
                                                androidx.compose.material.Text(
                                                    text = when (signInPopup) {
                                                        2 -> "The username field is empty! Please make sure to enter a username!"
                                                        3 -> "The password field is empty! Please make sure to enter a password!"
                                                        4 -> "An error occurred with your account, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                        5 -> "Please enter a valid password without any special characters like \" or }!"
                                                        6 -> "It looks like you don't have an account! Please sign up or contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                        7 -> "Please enter a valid password without any special characters like \" or }!"
                                                        8 -> "Invalid credentials! Please make sure you typed in your username and password correctly!"
                                                        9 -> "Sorry, it looks like your account has been disabled! Please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                        else -> "Unknown error signing in, please retry after a while or restart the testing portal!"
                                                    },
                                                    Modifier.align(Alignment.CenterHorizontally).padding(20.dp),
                                                    fontSize = 15.sp, textAlign = TextAlign.Center
                                                )

                                                Button(
                                                    onClick = { signInPopup = 0; AppManager.focusedWindow!!.close() },
                                                    Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
                                                ) {
                                                    androidx.compose.material.Text(text = "Ok", fontSize = 15.sp, textAlign = TextAlign.Center)
                                                }
                                            }
                                        }
                                    }
                                }

                                Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.width(250.dp).align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 5.dp))

                                var googlePopup by remember {
                                    mutableStateOf(0)
                                }

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 5.dp),
                                    onClick = {
                                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                            val authUrl = "https://accounts.google.com/o/oauth2/auth/oauthchooseaccount?redirect_uri=https%3A%2F%2FVirtuousExpensiveSymbols.roycea.repl.co%2Fhome.html&response_type=code%20permission%20id_token&scope=openid%20profile%20email&openid.realm&client_id=834594227639-8510j9dnu8k6m9t2li9j55eqc5dira87.apps.googleusercontent.com&ss_domain=https%3A%2F%2Fvirtuousexpensivesymbols.roycea.repl.co&access_type=offline&include_granted_scopes=true&prompt=consent&origin=https%3A%2F%2Fvirtuousexpensivesymbols.roycea.repl.co&gsiwebsdk=2&flowName=GeneralOAuthFlow"

                                            Desktop.getDesktop().browse(URI(authUrl))
                                        }

                                        runBlocking {
                                            `$`.httpServer().router().get("/oauth/:data").handler { ctx ->
                                                val data = ctx.getPathParameter("data").replace("#####", "/").split("~")

                                                println(data)

                                                val code = data[0].toInt()

                                                when (code) {
                                                    0 -> {
                                                        val email = data[1]
                                                        val id = data[2]
                                                        val credential = data[3]

                                                        var tryFirebaseAuth = firebaseAuth.authenticate(email, credential)

                                                        authenticated = when (tryFirebaseAuth) {
                                                            0 -> {
                                                                true
                                                            }

                                                            6 -> {
                                                                tryFirebaseAuth = firebaseAuth.create(email, credential)

                                                                when (tryFirebaseAuth) {
                                                                    2 -> {
                                                                        tryFirebaseAuth = 2

                                                                        false
                                                                    }

                                                                    else -> {
                                                                        tryFirebaseAuth = 1

                                                                        false
                                                                    }
                                                                }
                                                            }

                                                            else -> {
                                                                false
                                                            }
                                                        }

                                                        googlePopup = tryFirebaseAuth

                                                        ctx.end("Sign in successful! You may return to the testing portal now.")
                                                    }

                                                    else -> {
                                                        authenticated = false

                                                        googlePopup = code

                                                        ctx.end("Sign in failed! Please restart the testing portal and try again!")
                                                    }
                                                }
                                            }.listen("localhost", 8090)
                                        }

                                        authenticated = true
                                    }
                                ) {
                                    Text("Google")
                                }

                                if (googlePopup != 0) {
                                    Window(
                                        title = "CSSA Test Portal | Authentication Error",
                                        icon = loadImageResource("CSSA.png"),
                                        size = IntSize(450, 195)
                                    ) {
                                        Column (Modifier.align(Alignment.CenterHorizontally)) {
                                            androidx.compose.material.Text(
                                                text = when (googlePopup) {
                                                    4 -> "An error occurred with your account, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                    6 -> "Unknown error signing in, please retry after a while or restart the testing portal!"
                                                    9 -> "Sorry, it looks like your account has been disabled! Please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                    else -> "An error occurred with your account, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                                                },
                                                Modifier.align(Alignment.CenterHorizontally).padding(20.dp),
                                                fontSize = 15.sp, textAlign = TextAlign.Center
                                            )

                                            Button(
                                                onClick = { googlePopup = 0; AppManager.focusedWindow!!.close() },
                                                Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
                                            ) {
                                                androidx.compose.material.Text(text = "Ok", fontSize = 15.sp, textAlign = TextAlign.Center)
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }

                }

            }
        }
    }
}

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource at path '$path' not found" }
    return resource.openStream().use(ImageIO::read)
}

fun imageFromFile(file: File): ImageBitmap {
    return Image.makeFromEncoded(file.readBytes()).asImageBitmap()
}

//val quicksand = FontFamily(
//    androidx.compose.ui.text.platform.Font(
//        file = File("Quicksand-Medium.ttf"),
//        weight = FontWeight.Normal,
//        style = FontStyle.Normal
//    )
//)

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily = FontFamily.Default,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = TextStyle.Default
) {
    Text(
        AnnotatedString(text),
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        emptyMap(),
        onTextLayout,
        style
    )
}

@Composable
fun QuestionText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily = FontFamily.Default,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = TextStyle.Default
) {
    Text(
        AnnotatedString(text),
        modifier.padding(bottom = 10.dp),
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        emptyMap(),
        onTextLayout,
        style
    )
}

@InternalTextApi
@Composable
fun QuestionField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
) {
    BasicTextField(
        value,
        onValueChange,
        modifier.border(1.dp, Color.Black, RoundedCornerShape(2.dp)),
        enabled,
        readOnly,
        textStyle,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        visualTransformation,
        onTextLayout,
        interactionSource,
        cursorBrush,
        decorationBox
    )
}