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

package it.danieleverducci.punkbeers

import android.util.Log
import android.widget.Toast
import it.danieleverducci.punkbeers.entities.Beer
import it.danieleverducci.punkbeers.networking.BeersFilter
import it.danieleverducci.punkbeers.networking.RetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Beers repository
 * Allows to retrieve beers from Punk API via its Retrofit implementation
 */
class BeersRepository {

    companion object {
        val TAG = "BeersRepository"
    }

    var listener: Listener? = null
    var page = 1
    var filter = BeersFilter()
    private var more = true

    /**
     * Obtain a paginated list of beers from the server
     * Use page property to set the page
     * Async: will call listener's onResponse() or onFailure()
     */
    fun getBeers() {
        // Obtain beers
        val call = RetrofitProvider.getClient.getBeers(page, filter.brewedBefore, filter.brewedAfter)
        call.enqueue(object: Callback<List<Beer>> {
            override fun onResponse(call: Call<List<Beer>>, response: Response<List<Beer>>) {
                if (response.body() != null) {
                    if (response.body()!!.isEmpty())
                        more = false
                    listener?.onBeersObtained(response.body()!!, more)
                } else {
                    listener?.onFailure()
                }
            }

            override fun onFailure(call: Call<List<Beer>>, t: Throwable) {
                if (t.message != null)
                    Log.e(TAG, "Unable to obtain beers: ${t}")
                listener?.onFailure()
            }
        })
    }

    /**
     * Discard page and restarts from first
     */
    fun reset() {
        page = 1
        more = true
    }

    /**
     * Listener for api callbacks
     */
    interface Listener {
        fun onBeersObtained(beers: List<Beer>, more: Boolean)
        fun onFailure()
    }
}