/**
 * Developer friendly classes to store the Bible data in
 * First 3: Used to store the data from the Json catalog
 * Second 2: Used to store the wanted data fromm the USFM file on a book
 */
data class Language (val name: String, val versions: List<Version>)

data class Version (val name: String, val books: List<Book>)

data class Book (val id: String, val name: String, val url: String)




data class USFMBook (val name: String, val chapters: List<USFMChpt>)

data class USFMChpt (val num: Int, val verses: String)

