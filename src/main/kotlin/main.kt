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
    var content by remember {
        mutableStateOf(
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        // Sign in
                        AppWindow(title = "Sign In to the CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(600, 600)).also {

                        }.show {
                            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {

                                    }) {
                                    Text("Email")
                                }

                                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {

                                    }) {
                                    Text("Google")
                                }
                            }
                        }

                        // On successful authentication, take user to home page

                    }) {
                    Text("Sign In")
                }
            }
        )
    }

    MaterialTheme {
        content
    }.also {

    }
}

fun changeContent() {

}

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource at path '$path' not found" }
    return resource.openStream().use(ImageIO::read)
}