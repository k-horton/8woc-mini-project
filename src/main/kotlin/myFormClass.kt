import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox
import tornadofx.*

/**
 * Creates a form to ask the user for Chapter and Verse.
 * Eventually this field wil be the text displayed.
 */
class MyForm : View() {
    val controller: MyController by inject()
    val chapter = SimpleStringProperty()
    val verse = SimpleStringProperty()

    override val root = form {
        fieldset {
            field("Chapter") {
                textfield(chapter)
            }
            field("Verse(s)") {
                textfield(verse)
            }

            button("Go to ListView") {
                action {
                    replaceWith(ListView::class, ViewTransition.Slide(0.8.seconds, ViewTransition.Direction.LEFT))
                }
            }

            // this totally screws over the formatting of the entire app... why?
            button("Open editor") {
                action {
                    openInternalWindow<PopUpFragment>()
                }
            }

            button("Submit") {
                action {
                    controller.getChapter(chapter.value)
                    controller.getVerse(verse.value)
                    chapter.value = ""
                    verse.value = ""
                }
            }
        }
    }

    class MyController : Controller() {

        // functions for form submit
        fun getChapter(inputValue: String) {
            print("Displaying Genesis $inputValue:")
        }
        fun getVerse(inputValue: String) {
            println("$inputValue on screen!")
        }
    }
}


// MOSTLY A TEST

class LeftSideBar: View() {
    val controller: SideBarController by inject()
    val language = SimpleStringProperty()
    val versionSearch = SimpleStringProperty()
    val versions = FXCollections.observableArrayList("NIV",
            "ESV","NLT", "NRSV","MSG")
    val chapter = SimpleStringProperty()
    val verse = SimpleStringProperty()

    override val root = vbox {
        // LANGUAGE SELECTOR
        val languageSelect = form {
            hbox {
                label("Language")
                textfield(language)
            }
            button("SELECT") {
                addClass(MyStyle.tackyButton)
                useMaxWidth = false
                action {
                    controller.setLanguage(language.value)
                }
            }
            // VERSION SELECTOR
            combobox(versionSearch, versions)
            button("SUBMIT") {
                action {
                    controller.setVersion(versionSearch.value)
                }
            }
            // CHAPTER && VERSE SELECTOR
            hbox {
                label("Chapter")
                textfield(chapter)
                label("Verse")
                textfield(verse)
            }
            button("GO!") {
                action {
                    controller.setChapterAndVerse(chapter.value, verse.value)
                    chapter.value = ""
                    verse.value = ""
                }
            }
        }
    }

    class SideBarController : Controller() {
        // sets the language
        fun setLanguage(inputValue: String) {
            println("$inputValue set as language")
        }
        fun setVersion(inputValue: String) {
            println("$inputValue set as version")
        }
        fun setChapterAndVerse(chapterValue: String, verseValue: String) {
            // FIGURE OUT HOW TO ACCEPT NULL VALUES
            println("Opening $chapterValue:$verseValue...")
        }
    }
}

/**
 * Creates a list on-screen of the ArrayList inside the contained controller.
 */
class ListView: View() {
    // assign MyController to a value
    val controller: MyController by inject()

    override val root = vbox {
        label("My items")
        listview(controller.values)
        button("Go to Form") {
            action {
                replaceWith(MyForm::class, ViewTransition.Slide(0.8.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }

    class MyController : Controller() {
        // values for dropdown list
        val values = FXCollections.observableArrayList("Alpha", "Beta", "Gamma", "Delta")
    }
}