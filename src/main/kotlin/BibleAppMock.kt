import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

/**
 * Initializes the class and the stylesheet.
 */
class MyApp : App(MyView::class, MyStyle::class) {
    // starts App in maximized window
    /*override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }*/

    // loads the stylesheet
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
            val listView = find(BibleView::class)
            val leftSideBar = find(LeftSideBar::class)
            val titleBanner = find(TitleBanner::class)

            // stick those bois in there
            borderpane {
                center = listView.root
                left = leftSideBar.root
                top = titleBanner.root
            }

            // sets background color for the whole app
            style {
                backgroundColor += Color.DARKSLATEBLUE
                padding = box(5.px, 10.px, 10.px, 10.px)
            }
        }
    }
}

/**
 * Launches the app!
 */
fun main(args: Array<String>) {
    launch<MyApp>(args)
}