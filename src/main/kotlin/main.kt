import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun main() = Window(title = "CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(1080, 712)) {
    var authenticated by remember {
        mutableStateOf(false)
    }

    MaterialTheme {
        if (authenticated) {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Text("Hello!", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        } else {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
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