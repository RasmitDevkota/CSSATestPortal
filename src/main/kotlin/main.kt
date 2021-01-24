import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.loadVectorXmlResource
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import authentication

fun main() = Window(title = "CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(1080, 712)) {
    var authenticated by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var username = "ExampleUsername"

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
                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(3.0f), onClick = {

                    }) {
                        Icon(imageVector = vectorXmlResource("home_icon.xml"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(3.0f), onClick = {

                    }) {
                        Icon(imageVector = vectorXmlResource("assignment_icon.xml"))
                    }

                    IconButton(modifier = Modifier.align(Alignment.CenterHorizontally).scale(3.0f), onClick = {

                    }) {
                        Icon(imageVector = vectorXmlResource("settings_icon.xml"))
                    }
                }

                Column(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (expanded) 0.831f else 0.92f),
                    Arrangement.spacedBy(50.dp)) {
                    Text(text = "Welcome, $username!", Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp)

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
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        AppWindow(title = "Sign In to the CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(600, 600)).also {
                            //frontend

                        }.show {
                            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        // Authenticate...
                                        authentication()
                                        authenticated = true

                                        AppManager.focusedWindow?.close()
                                    }) {
                                    Text("Email")
                                }

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        // Authenticate...

                                        authenticated = true

                                        AppManager.focusedWindow?.close()
                                    }) {
                                    Text("Google")
                                }
                            }
                        }
                    }) {
                    Text("Sign In")
                }

                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        AppWindow(title = "Sign In to the CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(600, 600)).also {

                        }.show {
                            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        // Authenticate...

                                        authenticated = true

                                        AppManager.focusedWindow?.close()
                                    }) {
                                    Text("Email")
                                }

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        // Authenticate...

                                        authenticated = true

                                        AppManager.focusedWindow?.close()
                                    }) {
                                    Text("Google")
                                }
                            }
                        }
                    }) {
                    Text("Sign Up")
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