import com.squareup.moshi.Json

private const val catalogID = "languages"
private const val languageID = "title"
private const val versionID = "title"
private const val bookID = "identifier"
private const val titleID = "title"

//@JsonClass(generateAdapter = true)
data class Root(
        val languages: List<Languages>
)

//@JsonClass(generateAdapter = true)
data class Languages (
        val title: String,
        val resources: List<Recource>
)

//@JsonClass(generateAdapter = true)
data class Recource (
        val title: String,
        val subject: String,
        val projects: List<Project>
)

//@JsonClass(generateAdapter = true)
data class Project (
        val identifier: String,
        val title: String,
        val formats: List<Format>
)

//@JsonClass(generateAdapter = true)
data class Format (
        @Json(name = "url")val url: String
)

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


