/**
 * Copyright 2021 Daniele Verducci
 *
 * This file is part of PunkBeers.
 *
 * PunkBeers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PunkBeers is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PunkBeers.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.danieleverducci.punkbeers.networking

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import it.danieleverducci.punkbeers.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provider for Punk API Retrofit implementation
 */
object RetrofitProvider {

    /**
     * Punk API Retrofit client
     */
    val getClient: PunkAPI
    get() {

        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.API_BASEURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(PunkAPI::class.java)

    }
}