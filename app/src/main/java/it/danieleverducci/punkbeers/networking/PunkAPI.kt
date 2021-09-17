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

import it.danieleverducci.punkbeers.entities.Beer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Punk API description for Retrofit
 */
interface PunkAPI {

    @GET("/v2/beers?per_page=10")
    fun getBeers(
        @Query("page") page: Int,
        @Query("brewed_before") brewedBefore: String?,
        @Query("brewed_after") brewedAfter: String?
    ): Call<List<Beer>>

}