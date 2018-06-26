/**
 * this file contains the classes used to store the JSON data
 * pulled through Moshi and Retrofit
 */

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
        val identifier: String,
        val direction: String,
        val resources: List<Resource>
)

//@JsonClass(generateAdapter = true)
data class Resource (
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




