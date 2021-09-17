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

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import it.danieleverducci.punkbeers.R

import it.danieleverducci.punkbeers.databinding.FragmentBeersListitemBinding
import it.danieleverducci.punkbeers.entities.Beer

/**
 * RecyclerAdapter for beers list RecyclerView
 */
class BeerRecyclerAdapter : RecyclerView.Adapter<BeerRecyclerAdapter.ViewHolder>() {

    // The click event listener
    var listener: Listener? = null

    // Whether there are more items in next page
    private var more = true

    private val items: ArrayList<Beer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create viewholder
        val vh = ViewHolder(
            FragmentBeersListitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        // Register click listener
        vh.root.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if (v != null) {
                    listener?.OnItemClicked(v.getTag() as Beer)
                }
            }
        })

        return vh

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Attach item to view to retrieve in case of click
        holder.root.setTag(item)

        // Fill layout with data
        holder.name.text = item.name
        holder.descr.text = item.tagline
        holder.firstbrewed.text = item.firstBrewed
        Picasso.get()
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.pic)

        // If we are drawing the last element, notify
        if (position == items.size - 1 && more) {
            holder.progress.visibility = View.VISIBLE
            listener?.OnLastItemScrolled()
        } else {
            holder.progress.visibility = View.GONE
        }
    }

    /**
     * Add items to the list and updates the view
     * @param ni List of beers to be added
     * @param more Whether there are more beers
     */
    fun addItems(ni: List<Beer>, more: Boolean) {
        this.more = more
        items.addAll(ni)
        notifyDataSetChanged()
    }

    /**
     * Clears the list and updates the view
     */
    fun clear() {
        items.clear()
        more = true
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: FragmentBeersListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        val root: View = binding.root
        val name: TextView = binding.beerItemName
        val descr: TextView = binding.beerItemDescr
        val firstbrewed: TextView = binding.beerItemFirstbrewed
        val pic: ImageView = binding.beerItemPic
        val progress: ProgressBar = binding.beerItemProgress
    }

    /**
     * List event listener
     */
    interface Listener {
        fun OnItemClicked(item: Beer)
        fun OnLastItemScrolled()
    }

}