import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.codec.http2.model.HttpMethod
import com.firefly.kotlin.ext.http.HttpServer
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.Desktop;
import java.net.URI;

var auth = Authentication()
var firebase = Firebase()
var firebaseAuth = firebase.Authentication()
var firestore = firebase.Firestore()

fun main() = Window(title = "CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(1080, 712)) {
    var noUsername by remember {
        mutableStateOf(false)
    }

    var authenticated by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var currentPage by remember {
        mutableStateOf(0)
    }

    MaterialTheme() {
        if (authenticated) {
            Row {
                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (expanded) 0.169f else 0.08f)
                    .background(Color(66, 133, 244))
                    .pointerMoveFilter(
                        onEnter = {
                            expanded = true
                            false
                        },
                        onExit = {
                            expanded = false
                            false
                        }
                    ),
                    Arrangement.spacedBy(30.dp)) {
                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        currentPage = 0
                    }) {
                        Icon(bitmap = imageFromResource("Home Icon.png"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        currentPage = 1
                    }) {
                        Icon(bitmap = imageFromResource("Events Icon.png"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {
                        currentPage = 2
                    }) {
                        Icon(bitmap = imageFromResource("Settings Icon.png"))
                    }
                }

                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (expanded) 0.831f else 0.92f),
                    Arrangement.spacedBy(50.dp)) {

                    when (currentPage) {
                        0 -> { // Home Page

                            Text(text = "Welcome, ${auth.username}!", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            Column(Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.6f)
                                .align(Alignment.CenterHorizontally)
                                .background(Color(243, 243, 243))) {
                                Text("Competition 1")
                                Text("Competition 2")
                                Text("Competition 3")
                            }

                        }

                        1 -> { // Competition Page

                            Text(text = "My Competitions", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                            Column(Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.6f)
                                .align(Alignment.CenterHorizontally)
                                .background(Color(243, 243, 243))) {
                                TextButton(onClick = {
                                    currentPage = 3
                                }) {
                                    Text("Event 1")
                                }

                                TextButton(onClick = {
                                    currentPage = 3
                                }) {
                                    Text("Event 2")
                                }

                                TextButton(onClick = {
                                    currentPage = 3
                                }) {
                                    Text("Event 3")
                                }

                                TextButton(onClick = {
                                    currentPage = 3
                                }) {
                                    Text("Event 4")
                                }
                            }

                        }

                        2 -> { // Settings Page

                            Text(text = "Settings", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

                        }

                        3 -> {

//                            Text(text = Test().loadTestFromFirebase("example"))

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
                            .border(3.dp, Color(33, 33, 33), RoundedCornerShape(8.dp))) {

                            Column(Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp, 15.dp, 10.dp, 15.dp), Arrangement.spacedBy(15.dp)) {

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
                                        Row() {
                                            Text(text = "Passwords do not match!", textAlign = TextAlign.Left, color = Color(188, 88, 88))
                                        }
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                                        Column() {
                                            Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                                onClick = {
                                                    if(auth.checkUsername(username)) {
                                                        if(auth.checkEmail(email)) {
                                                            if(auth.createAccount(fName, lName, username, email, password)) {
                                                                authenticated = true
                                                            } else {
                                                                print("Error in creating account")
                                                            }
                                                        } else {
                                                            print("Email is already in use")
                                                        }
                                                    } else {
                                                        print("Username is already in use")
                                                    }
                                                }) {
                                                Text("Sign Up")
                                            }

                                            TextButton(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                                onClick = {
                                                    noUsername = false
                                                }) {
                                                Text("Already have an account? Sign in!")
                                            }
                                        }
                                    }
                                } else {
                                    var username by remember {
                                        mutableStateOf("")
                                    }

                                    var password by remember {
                                        mutableStateOf("")
                                    }

                                    Text("Username", textAlign = TextAlign.Left)

                                    TextField(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        value = username,
                                        onValueChange = { username = it },
                                    )

                                    Text("Password", textAlign = TextAlign.Left)

                                    TextField(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        value = password,
                                        onValueChange = { password = it },
                                    )

                                    Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 5.dp, 0.dp, 0.dp),
                                        onClick = {

                                            if (auth.manualSignIn(username, password)) {
                                                authenticated = true
                                            } else {
                                                print("Invalid Username or Password!");
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
                                }

                                Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.width(250.dp).align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 10.dp))

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                            auth.googleHttpServer()

                                            Desktop.getDesktop().browse(URI("https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?response_type=code&scope=openid%20profile&redirect_uri=http%3A%2F%2F127.0.0.1%3A8310%2F&client_id=834594227639-b7pj2rqb1eijd2pbfvice7bp0ndsdp7i.apps.googleusercontent.com&flowName=GeneralOAuthFlow"));
                                        }

                                        runBlocking {
                                            val host = "localhost"
                                            val port = 8081

                                            HttpServer {
                                                router {
                                                    httpMethod = HttpMethod.GET
                                                    path = "/get-code/4/:code"

                                                    asyncHandler {
                                                        val codeId = getPathParameter("code")

                                                        println(codeId)

                                                        auth.googleSignIn("4/$codeId")

                                                        end("Done")
                                                    }
                                                }
                                            }.listen(host, port)
                                        }

                                        authenticated = true
                                    }) {
                                    Text("Google")
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

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily = fontFamily(androidx.compose.ui.text.platform.font("Quicksand", "Quicksand-Medium.ttf")),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = AmbientTextStyle.current
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