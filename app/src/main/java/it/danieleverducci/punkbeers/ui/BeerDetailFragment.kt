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

package it.danieleverducci.punkbeers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import it.danieleverducci.punkbeers.R
import it.danieleverducci.punkbeers.databinding.FragmentBeerDetailBinding
import it.danieleverducci.punkbeers.entities.Beer

/**
 * A simple beer detail fragment, with an image and some textual data
 */
class BeerDetailFragment(val beer: Beer) : Fragment() {

    lateinit var binding: FragmentBeerDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerDetailBinding.inflate(
            LayoutInflater.from(container!!.context),
            container,
            false
        )

        // Populate view
        Picasso.get()
            .load(beer.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.beerDetailPic)
        binding.beerDetailTitle.text = beer.name
        binding.beerDetailShortdesc.text = beer.tagline
        binding.beerDetailDesc.text = beer.description
        binding.beerDetailFirstbrewed.text = beer.firstBrewed

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onStop() {
        super.onStop()
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(false);
    }
}