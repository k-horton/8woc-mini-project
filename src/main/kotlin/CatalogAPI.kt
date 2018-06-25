import retrofit2.Call
import retrofit2.http.GET

/**
 * Used to pull the Json catalog into the JsonCatalog.kt structs
 */

interface CatalogAPI {
    @GET("catalog.json")
    fun getBibles(): Call<Root>
}