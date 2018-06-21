import javafx.scene.paint.Color
import tornadofx.*

class MyStyle: Stylesheet() {

    companion object {
        val tackyButton by cssclass()

        private val topColor = c("#FF0000")
        private val rightColor = c("#006400")
        private val leftColor = c("#FFA500")
        private val bottomColor = c("#800080")
    }

    init {
        tackyButton {
            rotate = 10.deg
            borderColor += box(topColor,rightColor,bottomColor,leftColor)
            fontFamily = "Comic Sans MS"
            fontSize = 20.px
        }
    }
}