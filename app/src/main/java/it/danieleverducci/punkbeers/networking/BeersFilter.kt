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

import android.util.Log
import java.util.*

/**
 * Class to represent a beer filter. Filters by brewing date.
 */
class BeersFilter {
    // The date format is like "09/2007" (MM/YYYY). Month start from 1.
    var brewedBefore: String? = null
    var brewedAfter: String? = null

    val brewedBeforeYear: Int
            get() = getYear(brewedBefore)
    val brewedAfterYear: Int
            get() = getYear(brewedAfter)
    val brewedBeforeMonth: Int
            get() = getMonth(brewedBefore)
    val brewedAfterMonth: Int
            get() = getMonth(brewedAfter)

    /**
     * Clears filter
     */
    fun clear() {
        brewedBefore = null
        brewedAfter = null
    }

    /**
     * Sets the end filter date.
     * Uses Calendar month format (January is 0, December is 11).
     * @param month Month starting from 0
     * @param year Year as 4-digit
     */
    fun setBrewedBefore(month: Int, year: Int) {
        brewedBefore = "${String.format("%02d", month + 1)}-${String.format("%04d", year)}"
    }

    /**
     * Sets the start filter date.
     * Uses Calendar month format (January is 0, December is 11).
     * @param month Month starting from 0
     * @param year Year as 4-digit
     */
    fun setBrewedAfter(month: Int, year: Int) {
        brewedAfter = "${String.format("%02d", month + 1)}-${String.format("%04d", year)}"
    }

    /**
     * Returns the selected year, if any, otherwise returns current year
     * @param filter value String?
     * @return the selected year
     */
    private fun getYear(value: String?): Int {
        if (value == null) return Calendar.getInstance().get(Calendar.YEAR)
        return value.substring(3).toInt()
    }

    /**
     * Returns the selected month, if any, otherwise returns current month
     * Uses Calendar month format (January is 0, December is 11)
     * @param filter value String?
     * @return the selected month
     */
    private fun getMonth(value: String?): Int {
        if (value == null) return Calendar.getInstance().get(Calendar.MONTH)
        return value.substring(0,2).toInt()
    }
}