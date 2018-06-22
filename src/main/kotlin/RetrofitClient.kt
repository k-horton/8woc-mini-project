import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {

    private val ROOT_URL = "https://api.door43.org/v3/"

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    fun getCatalogAPI(): CatalogAPI {
        return getRetrofitInstance().create(CatalogAPI::class.java)
    }

}