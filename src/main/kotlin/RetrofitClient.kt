/**
 * This file contains the Retrofit Client which is used to build
 * the retrofit instance and use Moshi to as a method to store the
 * JSON catalog
 */

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Uses Retrofit to pull set up a structure to store the Json catalog
 */
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