
data class Language (val name: String, val versions: List<Version>)

data class Version (val name: String, val books: List<Book>)

data class Book (val id: String, val name: String, val url: String)




data class USFMBook (val name: String, val chapters: List<USFMChpt>)

data class USFMChpt (val num: Int, val verses: String)

