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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.danieleverducci.punkbeers.entities.Beer
import it.danieleverducci.punkbeers.ui.BeerDetailFragment

/**
 * App entrypoint
 * An activity with two fragments (list + detail)
 */
class MainActivity : AppCompatActivity(), BeerNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Show beer detail fragment
     * @param beer to show in detail
     */
    override fun showBeerDetail(beer: Beer) {
        val detailFragment = BeerDetailFragment(beer)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        // Manage fragment navigation
        val bsf = supportFragmentManager.backStackEntryCount
        if(bsf == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}