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

package it.danieleverducci.punkbeers.entities

import com.google.gson.annotations.SerializedName

/**
 * Represents a Punk API's (https://punkapi.com/documentation/v2) beer item
 */
class Beer {
    val id: Int = 0
    val name: String = ""
    val tagline: String = ""
    val description: String = ""
    @SerializedName("image_url")
    val imageUrl: String = ""
    @SerializedName("first_brewed")
    val firstBrewed: String = ""
}