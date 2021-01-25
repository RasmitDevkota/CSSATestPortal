import androidx.compose.desktop.AppManager
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
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

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

    var auth = Authentication()

    MaterialTheme {
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

                    }) {
                        Icon(bitmap = imageFromResource("Home Icon.png"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {

                    }) {
                        Icon(bitmap = imageFromResource("Events Icon.png"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.0f), onClick = {

                    }) {
                        Icon(bitmap = imageFromResource("Settings Icon.png"))
                    }
                }

                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (expanded) 0.831f else 0.92f),
                    Arrangement.spacedBy(50.dp)) {
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

                                    var password by remember {
                                        mutableStateOf("")
                                    }

                                    var confirmPassword by remember {
                                        mutableStateOf("")
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {
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

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {
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

                                        Column() {
                                            Text(text = (if ((password == confirmPassword && confirmPassword != "") || confirmPassword == "") "" else "Passwords do not match!"), textAlign = TextAlign.Left, color = Color(188, 88, 88))
                                        }
                                    }

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                                        Column() {
                                            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                                onClick = {
                                                    auth.usernameSignIn(username, password)

                                                    authenticated = true
                                                }) {
                                                Text("Sign Up")
                                            }

                                            TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),
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

                                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                        onClick = {
                                            auth.usernameSignIn("Username", "Password")

                                            authenticated = true
                                        }) {
                                        Text("Sign In")
                                    }

                                    TextButton(modifier = Modifier
                                        .align(Alignment.CenterHorizontally),
                                        onClick = {
                                            noUsername = true
                                        }) {
                                        Text("Don't have an account? Sign up!")
                                    }
                                }

                                Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.width(250.dp).align(Alignment.CenterHorizontally))

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        auth.googleSignIn("params")

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