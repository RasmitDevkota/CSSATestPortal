import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

                Column(Modifier.fillMaxSize(), Arrangement.spacedBy(15.dp)) {

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

                        Text("Username", Modifier.align(Alignment.CenterHorizontally))

                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = username,
                            onValueChange = { username = it },
                        )

                        Text("Email", Modifier.align(Alignment.CenterHorizontally))

                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = email,
                            onValueChange = { email = it },
                        )

                        Text("Password", Modifier.align(Alignment.CenterHorizontally))

                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = password,
                            onValueChange = { password = it },
                        )

                        Text("Confirm Password", Modifier.align(Alignment.CenterHorizontally))

                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it

                                if (confirmPassword != password) {
                                    print("Passwords don't match!")
                                }
                            },
                        )

                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                auth.usernameSignIn(username, password)

                                authenticated = true
                            }) {
                            Text("Sign Up")
                        }

                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                noUsername = false
                            }) {
                            Text("Already have an account? Sign in!")
                        }
                    } else {
                        var username by remember {
                            mutableStateOf("")
                        }

                        var password by remember {
                            mutableStateOf("")
                        }

                        Text("Username", Modifier.align(Alignment.CenterHorizontally))

                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = username,
                            onValueChange = { username = it },
                        )

                        Text("Password", Modifier.align(Alignment.CenterHorizontally))

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

                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                noUsername = true
                            }) {
                            Text("Don't have an account? Sign up!")
                        }
                    }

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

fun changeContent() {

}

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource at path '$path' not found" }
    return resource.openStream().use(ImageIO::read)
}