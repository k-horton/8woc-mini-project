import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*

class MyApp : App(MyView::class, MyStyle::class) {
    // initialize the stylesheet
    init {
        reloadStylesheetsOnFocus()
    }
}

/**
 * The main view for the UI. (Think React-style.)
 */
class MyView : View() {
    override val root = VBox()
    init {
        with(root) {
            // pull some views
            val listView = find(ListView::class)
            val leftSideBar = find(LeftSideBar::class)

            // stick those bois in there
            borderpane {
                right = listView.root
                left = leftSideBar.root
            }
        }
    }
}

/**
 * !-- Experimenting with pop-up window feature. !--
 */
class PopUpFragment : Fragment() {
    override val root = label("This is a popup")
}

/**
 * Launches the app!
 */
fun main(args: Array<String>) {
    launch<MyApp>(args)
}