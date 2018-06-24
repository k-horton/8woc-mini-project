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




