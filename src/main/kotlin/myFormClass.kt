import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import tornadofx.*

/**
 * Creates a title across the top of the app.
 */
class TitleBanner: View() {
    override val root = vbox {
        // style for outside the text
        addClass(MyStyle.titleClass)

        text("Bible Reader") {
            // sets text color
            // because to affect the text it's gotta be in here
            // but for the background it needs to be outside of the text object
            // hence, two different style things
            style {
                fill = Color.BEIGE
            }
        }
        text("Read the Bible. It's pretty good. 10/10\n") {
            style {
                font = Font(10.0)
                fill = Color.BEIGE
            }
        }
    }
}

/**
 * Menu with a form to ask the user for Chapter and Verse.
 */
class LeftSideBar: View() {

    /**
     * Get bible Data from door43
     * needs check for empty list
     */
    private val bibleData = getBibleData()

    val controller: SideBarController by inject()

    val language = SimpleStringProperty()
    val versionSearch = SimpleStringProperty()
    val languageList = FXCollections.observableArrayList(bibleData.map { it.name })
    val versions = FXCollections.observableArrayList<String?>()
    val books = FXCollections.observableArrayList<String?>()
    val bookSelection = SimpleStringProperty()
    val chapter = SimpleStringProperty()

    var curLan = emptyList<Version>()
    var curVer = emptyList<Book>()
    lateinit var curBook : USFMBook

    //val verse = SimpleStringProperty()

    override val root = vbox {
        style {
            padding = box(10.px)
        }
        squeezebox {
            /**
             * Select a language from an auto-complete textbox.
             * (Auto-complete feature IN PROGRESS.)
             */
            fold("Language Selection", expanded = true, closeable = false) {
                form {
                    fieldset("1: Select a Language") {
                        field("Language") {
                            combobox(language, languageList)
                        }
                        button("SELECT") {
                            addClass(MyStyle.niceButton)
                            useMaxWidth = false
                            action {
                                if(language.value == null){
                                    println("No language selected. \n" +
                                            "Default language set to English.")
                                    controller.setLanguage("English")
                                }
                                // else if(language.value is not in array)
                                else {
                                    controller.setLanguage(language.value)

                                    /**
                                     * Save the list of versions for the current language
                                     * and add them to the dropdown menu
                                     * Clear all lower options
                                     */
                                    curLan = bibleData.filter { it.name == language.value }[0].versions
                                    versions.clear()
                                    versions.addAll(curLan.map { it.name })

                                    books.clear()
                                }
                            }
                        }
                    }
                }
            }
            /**
             * Select a version from a dropdown menu.
             */
            fold("Version Selection", expanded = true, closeable = false) {
                form {
                    fieldset("2: Select a Version") {
                        field("Version") {
                            combobox(versionSearch, versions)
                        }
                        button("SUBMIT") {
                            addClass(MyStyle.niceButton)
                            action {
                                if(versionSearch.value == null){
                                    println("No version selected.\n" +
                                            "Default version set to the Message. Are you happy now?")
                                    controller.setVersion("MSG")
                                }
                                else {
                                    controller.setVersion(versionSearch.value)

                                    /**
                                     * Save the list of books for the current version
                                     * and add them to the dropdown menu
                                     * Clear all lower options
                                     */
                                    curVer =  curLan.filter { it.name == versionSearch.value }[0].books

                                    books.clear()
                                    books.addAll(curVer.map { it.name })
                                    //chapters.clear()
                                }
                            }
                        }
                    }
                }
            }
            /**
             * Select a book and type in a chapter number.
             * TONS of error-trapping.
             */
            fold("Book and Chapter", expanded= true, closeable = false) {
                form {
                    fieldset("3: Select a Book and Chapter") {
                        field("Book") {
                            combobox(bookSelection, books)
                        }
                        field("Chapter") {
                            textfield(chapter)
                        }
                        button("GO!") {
                            addClass(MyStyle.niceButton)
                            action {
                                // if book isn't selected
                                if(bookSelection.value == null) {
                                    println("No book selected.\n" +
                                            "Default set to Genesis 1.")
                                    controller.setBookAndChapter("Gen", "1")
                                }
                                // if book but no chapter
                                else if(bookSelection.value != null && chapter.value == null) {
                                    println("Default chapter set to 1.")
                                    controller.setBookAndChapter(bookSelection.value, "1")

                                    /**
                                     * Obtain and store the data from the selected book
                                     */
                                    curBook = getBook(curVer.filter { it.name == bookSelection.value }[0].url)

                                }
                                // if they did the gosh-darn thing correctly
                                else {
                                    controller.setBookAndChapter(bookSelection.value, chapter.value)
                                    //this@fold.isExpanded = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    class SideBarController : Controller() {
        var lang: String = "English"
        var vers: String = "MSG"
        var book: String = "Genesis"
        var chapter: String = "1"

        // sets the language
        fun setLanguage(inputValue: String) {
            println("$inputValue set as language")
            lang = inputValue
        }
        fun setVersion(inputValue: String) {
            println("$inputValue set as version")
            vers = inputValue
        }
        fun setBookAndChapter(bookValue: String, chapterValue: String) {
            println("Opening $bookValue $chapterValue...")
            book = bookValue
            chapter = chapterValue
        }
    }
}

/**
 * Displays the Bible text on screen.
 */
class BibleView: View() {

    // assign MyController to a value
    val controller: MyController by inject()
    var userFontSize : Double = 15.0

    override val root = vbox {
        addClass(MyStyle.bibleViewer)

        text(controller.bookName + " " + controller.chapterNo) {
            addClass(MyStyle.bookNameClass)
            style {
                fontFamily = "Georgia"
            }
        }
        text(controller.textString) {
            // style isn't in stylesheet bc it needs access to userFontSize
            style {
                font = Font(userFontSize)
                fontFamily = "Papyrus"
                textAlignment= TextAlignment.CENTER
            }
            // sets text to wrap at 1000 px
            // which DIDN'T WORK IN THE CLASS (╯°□°）╯︵ ┻━┻
            // BUT IT WORKS HERE FOR SOME REASON (╯°□°）╯︵ ┻━┻
            // I SPENT HOURS ON THIS                (╯°□°）╯︵ ┻━┻
            this.wrappingWidth = 700.0
        }
    }

    fun updateView (bookName: String, chapterNo: String, textString: String) {
        controller.updateView(bookName, chapterNo, textString)
    }

    class MyController : Controller() {
        val otherController: LeftSideBar.SideBarController by inject()

        var bookName: String = otherController.book
        var chapterNo: String = otherController.chapter
        var textString: String = "1:1 The book of the words of Tobit, son of Tobiel, the son of Ananiel, " +
                "the son of Aduel, the son of Gabael, of the seed of Asael, of the tribe of Nephthali;"

        /**
         * may or may not be used to update the bible viewer
         */
        fun updateView(bookName: String, chapterNo: String, textString: String) {
            this.bookName = bookName
            this.chapterNo = chapterNo
            this.textString = textString
        }
    }
}