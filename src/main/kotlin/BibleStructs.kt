import java.net.URL

data class Language (val name: String, val versions: List<Version>)

data class Version (val name: String, val books: List<Book>)

data class Book (val id: String, val name: String, val url: String)




data class USFMBook (val name: String, val chapters: List<USFMChpt>)

data class USFMChpt (val num: Int, val verses: String)

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