import retrofit2.Call
import retrofit2.http.GET


interface CatalogAPI {
    @GET("catalog.json")
    fun getBibles(): Call<Root>
}