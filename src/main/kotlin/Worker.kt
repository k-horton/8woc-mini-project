/**
 * This file contains the worker functions that are used to save the catalog Bible data
 * into more manageable data structures
 */

import java.net.URL

fun getBibleData() : List<Language> {

    /**
     * Grab the data from the Door43 catalog
     */
    val catalog = RetrofitClient().getCatalogAPI()
    val call = catalog.getBibles()
    val rawData = call.execute().body()

    var bibleData = emptyList<Language>()
    if (rawData != null) {
        bibleData = d43toBibles(rawData)
    }

    return bibleData
}

/**
 * Callable function to obtain the wanted book
 */
fun getBook (url: String) : USFMBook {
    println(url)

    val bibleBook = URL(url).readText()

    return formatUSFM(bibleBook)
}

/**
 * Used to format the obtained book into structs for easy chapter access
 */
private fun formatUSFM(usfmString: String) : USFMBook {
    val usfmBook = mutableListOf<USFMChpt>()

    /**
     * Turn massive string of the book into a list of lines
     */
    val lines = usfmString.lines()

    var chpt = ""
    var name = ""

    /**
     * Go through each line and check if there is any data needed to be stored:
     * \h is the name of the book
     * \c is a new chapter starting
     * \v is a verse
     * \p is a paragraph that might have part of a verse
     * \s5 is a new line
     */
    for (line in lines) {
        val key = line.substringBefore(' ')

        when (key) {
            "\\h" -> if (line.length > 3) name = line.substring(3)
            "\\c" -> if (chpt != "") {
                val num = line.substring(3).filter{ it != ' ' }.toInt() - 1
                usfmBook.add(USFMChpt(num, chpt))
                chpt = ""
            }
            "\\v" -> {
                chpt += line.substring(3)
                if (!chpt.endsWith("\n")) {
                    chpt += "\n"
                }
            }
            "\\p" -> if (line.length > 3 && chpt != "") chpt += line.substring(3)
            "\\s5" -> if (chpt != "") {
                chpt += "\n"
            }
        }
    }
    /**
     * Save the last chapter as the above method always saves the previous one
     */
    usfmBook.add(USFMChpt(usfmBook.size + 1, chpt))


    return USFMBook(name, usfmBook)

}

/**
 * Takes the Json formatted catalog in a root class and stores the languages,
 * versions, and books into a developer friendly class structure (BibleStructs.kt)
 */
fun d43toBibles (root: Root) : List<Language> {

    val d43Data = mutableListOf<Language>()

    //println("ln = " + root.languages.size)
    for (language in root.languages) {

        val versions = mutableListOf<Version>()

        var lan = language.title
        if (!language.title.contains('(')) {
            if (language.direction == "ltr") {
                lan = lan + " (" + language.identifier + ")"
            } else {
                lan = "(" + language.identifier + ") " + lan
            }
        }
        //println("$lan = " + language.resources.size)

        for (resource in language.resources) {

            /**
             * Make sure it's data for actual books of the Bible
             */
            if (resource.projects.size >= 27 && resource.subject == "Bible") {
                val books = mutableListOf<Book>()

                val verName = resource.identifier

                for (project in resource.projects) {

                    /**
                     * Make sure that this version has Books to online
                     */
                    if (project.formats != null) {


                        val bookID = project.identifier
                        val bookName = project.title

                        for (book in project.formats) {

                            /**
                             * Only obtain USFM file data
                             */
                            if (book.url.contains(".usfm")) {
                                books.add(Book(bookID, bookName, book.url))
                            }
                        }
                    }

                }
                if (books.isNotEmpty()) {
                    versions.add(Version(verName, books))
                }
            }

        }
        if (versions.isNotEmpty()) {
            d43Data.add(Language(lan, versions))
        }

    }

    return d43Data
}

/*
fun main(args: Array<String>) {
    val catalog = RetrofitClient().getCatalogAPI()

    val call = catalog.getBibles()



    val root = call.execute().body()
    val d43Data: List<Language>

    if (root != null) {
        d43Data = D43toBibles(root)

        val urlTest = getBook(d43Data[7].versions[0].books[0].url)

        println(urlTest.name)
        for (cht in urlTest.chapters) {
            println("chapter: " + cht.num)
            println(cht.verses)
        }



    }
}

*/