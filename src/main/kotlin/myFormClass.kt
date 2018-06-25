import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
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

    val controller: MyController by inject()

    val language = SimpleStringProperty()
    val versionSearch = SimpleStringProperty()
    val languageList = FXCollections.observableArrayList(bibleData.map { it.name })
    val versions = FXCollections.observableArrayList<String?>()
    val books = FXCollections.observableArrayList<String?>()
    val bookSelection = SimpleStringProperty()
    val chapter = SimpleStringProperty()
    val chapters = FXCollections.observableArrayList<String?>()

    var curLan = emptyList<Version>()
    var curVer = emptyList<Book>()
    lateinit var curBook : USFMBook
    
    var curBookName = ""

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
                        button("Select") {
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
                                    chapters.clear()
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
                        button("Select") {
                            addClass(MyStyle.niceButton)
                            action {
                                if(versionSearch.value != null && language.value != null){
                                    controller.setVersion(versionSearch.value)

                                    /**
                                     * Save the list of books for the current version
                                     * and add them to the dropdown menu
                                     * Clear all lower options
                                     */
                                    curVer =  curLan.filter { it.name == versionSearch.value }[0].books

                                    books.clear()
                                    books.addAll(curVer.map { it.name })

                                    chapters.clear()
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
            fold("Book", expanded = true, closeable = false) {
                form {
                    fieldset("3: Select a Book") {
                        field("Book") {
                            combobox(bookSelection, books)
                        }
                        button("Select") {
                            addClass(MyStyle.niceButton)
                            action {
                                // if book isn't selected
                                if (bookSelection.value != null && versionSearch.value != null) {
                                    /**
                                     * Obtain and store the data from the selected book
                                     */
                                    curBook = getBook(curVer.filter { it.name == bookSelection.value }[0].url)
                                    //println(curBook.chapters.size)

                                    chapters.clear()
                                    chapters.addAll(IntRange(1, curBook.chapters.size).map { it.toString() })
                                    curBookName = curBook.name
                                }
                            }
                        }
                    }
                }
            }

            /**
             * Set the Selection for Chapter
             */
            fold("Chapter", expanded = true, closeable = false) {
                form {
                    fieldset("4: Select a Chapter") {
                        field("Chapter") {
                            combobox(chapter, chapters)
                        }
                        button("Submit") {
                            addClass(MyStyle.niceButton)
                            action {
                                if (chapter.value != null && bookSelection.value != null) {
                                    /**
                                     * Obtain the verses and pass them to the controller
                                     */
                                    val scripture = curBook.chapters[chapter.value.toInt() - 1].verses
                                    println(chapter.value)
                                    println(scripture)
                                    controller.setScreen(curBookName, chapter.value, scripture)
                                }
                            }
                        }
                    }
                }
            }
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

    override var root = vbox {

        addClass(MyStyle.bibleViewer)

        text(controller.bookName + " " + controller.chapter) {
            addClass(MyStyle.bookNameClass)
            style {
                fontFamily = "Georgia"
            }
        }
        scrollpane {
            text(controller.verses) {
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
            this.wrappingWidth = 640.0

            }
        }
    }
}


/**
 * Used to store the values and data fto display
 */
class MyController : Controller() {

//    val textView: BibleView by inject()

    var lang = SimpleStringProperty("")
    var vers = SimpleStringProperty("")
    var bookName = SimpleStringProperty("")
    var chapter = SimpleStringProperty("")
    var verses = SimpleStringProperty("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")


    // sets the language
    fun setLanguage(inputValue: String) {
        println("$inputValue set as language")
        lang.value = inputValue
    }
    fun setVersion(inputValue: String) {
        println("$inputValue set as version")
        vers.value = inputValue
    }

    fun setScreen(name: String, chapterNo: String, textValue: String) {
        //println(textValue)
        bookName.value = name
        chapter.value = chapterNo
        verses.value = textValue
        //println(verses)
       // textView.updateScripture()

        //val updateScreen = UpdateScreen(BooknChpt(bookName, chapter, verses))
    }
}
