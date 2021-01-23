import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    var text by remember { mutableStateOf("Sign In") }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    // Sign in
                    AppWindow(title = "Sign In to the CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(600, 600)).also {
                        this.apply {  }
                    }.show {
                        Text("Authenticate")
                    }

                    // On successful authentication, take user to home page
                }) {
                Text("Sign In")
            }

            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    // Sign Up
                    AppWindow(title = "Sign Up for the CSSA Test Portal", icon = loadImageResource("CSSA.png"), size = IntSize(600, 600)).also {

                    }.show {
                        Text("Authenticate")
                    }

                    // On successful authentication, take user to home page
                }) {
                Text("Sign Up")
            }
        }
    }
}

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource at path '$path' not found" }
    return resource.openStream().use(ImageIO::read)
}