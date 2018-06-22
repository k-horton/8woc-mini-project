import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import tornadofx.*

class MyStyle: Stylesheet() {

    companion object {
        val niceButton by cssclass()
        val bookNameClass by cssclass()
        val textClass by cssclass()
        val titleClass by cssclass()
        val bibleViewer by cssclass()
        val buttonRadius = box(25.px)

        val bg by cssproperty<MultiValue<Paint>>("-fx-background-color")  // Define custom property

        private val boxColor = c("#c0c0c0")
    }

    init {
        niceButton {
            backgroundColor += Color.WHITE
            backgroundRadius += buttonRadius
            borderRadius += buttonRadius
            borderColor += box(boxColor)
            fontFamily = "Arial"
            fontSize = 1.5.em
        }

        bibleViewer {
            minWidth = 700.px
            maxWidth = 800.px
            alignment = Pos.TOP_CENTER
            padding = box(10.px)

            backgroundColor += Color.ANTIQUEWHITE
            backgroundRadius += buttonRadius
            borderRadius += buttonRadius
        }

        bookNameClass {
            fill = Color.DARKSLATEBLUE
            font = Font(25.0)
        }

        textClass { // NOT BEING USED CURRENTLY
            fontFamily = "Times New Roman"
            textAlignment= TextAlignment.CENTER
        }

        titleClass {
            minWidth = 1000.px
            maxWidth = 1000.px
            alignment = Pos.TOP_CENTER

            textAlignment = TextAlignment.CENTER
            fill = Color.BEIGE
            font = Font(36.0)
            fontFamily = "Georgia"
            // color is set in a style{} thing in the text object itself
        }
    }
}