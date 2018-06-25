import com.squareup.moshi.Json

/**
 * Classes used to obtain the data form the Json catalog through Moshi
 */

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
        val identifier: String,
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




