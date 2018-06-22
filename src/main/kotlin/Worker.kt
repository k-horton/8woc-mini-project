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
        bibleData = D43toBibles(rawData)
    }

    return bibleData
}

fun getBook (url: String) : USFMBook {

    val bibleBook = URL(url).readText()

    return formatUSFM(bibleBook)
}

fun formatUSFM(usfmString: String) : USFMBook {
    val usfmBook = mutableListOf<USFMChpt>()

    val lines = usfmString.lines()

    var chpt = ""
    var name = ""

    for (line in lines) {
        val key = line.substringBefore(' ')

        when (key) {
            "\\h" -> name = line.substring(line.lastIndexOf(" ") + 1)
            "\\c" -> if (chpt != "") {
                val num = line[line.length - 1].toInt() - 1
                usfmBook.add(USFMChpt(num, chpt))
                chpt = ""
            }
            "\\v" -> chpt += line.substring(3)
            "\\p" -> chpt += line.substring(3)
            "\\s5" -> chpt += "\n"
        }
    }


    return USFMBook(name, usfmBook)

}


fun D43toBibles (root: Root) : List<Language> {

    val d43Data = mutableListOf<Language>()

    //println("ln = " + root.languages.size)
    for (language in root.languages) {

        val versions = mutableListOf<Version>()

        val lan = language.title
        //println("$lan = " + language.resources.size)

        for (resource in language.resources) {

            if (resource.projects.size >= 27 && resource.subject == "Bible") {
                val books = mutableListOf<Book>()

                val verName = resource.title

                for (project in resource.projects) {

                    if (project.formats != null) {


                        val bookID = project.identifier
                        val bookName = project.title

                        for (book in project.formats) {

                            if (book.url.contains(".usfm")) {
                                books.add(Book(bookID, bookName, book.url))
                            }
                        }
                    }

                }
                versions.add(Version(verName, books))
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