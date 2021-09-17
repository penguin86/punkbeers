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

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import it.danieleverducci.punkbeers.BeerNavigation
import it.danieleverducci.punkbeers.BeersRepository
import it.danieleverducci.punkbeers.R
import it.danieleverducci.punkbeers.databinding.FragmentBeersListBinding
import it.danieleverducci.punkbeers.entities.Beer
import it.danieleverducci.punkbeers.networking.BeersFilter
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class BeersFragment : Fragment(), BeersRepository.Listener, BeerRecyclerAdapter.Listener {

    enum class FILTERTYPE {SINCE, TO, CLEAR}

    companion object {
        val INSTANCESTATE_FILTER_OPEN = "filter_open"
        val INSTANCESTATE_FILTER_SINCE = "filter_since"
        val INSTANCESTATE_FILTER_TO = "filter_to"
    }

    private val rvAdapter = BeerRecyclerAdapter()
    private val repo = BeersRepository()
    private lateinit var binding: FragmentBeersListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);

        if (!this::binding.isInitialized) {
            // Inflate layout
            binding = FragmentBeersListBinding.inflate(
                LayoutInflater.from(container!!.context),
                container,
                false
            )

            // Register filter buttons listeners
            binding.listFilterSinceBt.setOnClickListener { onFilterButtonClicked(FILTERTYPE.SINCE) }
            binding.listFilterToBt.setOnClickListener { onFilterButtonClicked(FILTERTYPE.TO) }
            binding.listFilterClear.setOnClickListener { onFilterButtonClicked(FILTERTYPE.CLEAR) }

            // Set the adapter
            with(binding.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = rvAdapter
            }

            // Register for recyclerview adapter events
            rvAdapter.listener = this

            // Register for network completed events
            repo.listener = this

            // Restore state
            repo.filter.brewedBefore = savedInstanceState?.getString(INSTANCESTATE_FILTER_TO)
            repo.filter.brewedAfter = savedInstanceState?.getString(INSTANCESTATE_FILTER_SINCE)
            binding.listFilterDropdown.visibility =
                if (savedInstanceState?.getBoolean(INSTANCESTATE_FILTER_OPEN) == true) View.VISIBLE else View.GONE

            // Load first page
            loadBeers(true)
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCESTATE_FILTER_OPEN, binding.listFilterDropdown.visibility == View.VISIBLE)
        outState.putString(INSTANCESTATE_FILTER_SINCE, repo.filter.brewedAfter)
        outState.putString(INSTANCESTATE_FILTER_TO, repo.filter.brewedBefore)
        super.onSaveInstanceState(outState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter)
            toggleFilter()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Beers obtained from services callback
     * @param beers List of beers
     * @param more Whether there are more beers to retrieve
     */
    override fun onBeersObtained(beers: List<Beer>, more: Boolean) {
        binding.listProgress.visibility = View.GONE
        rvAdapter.addItems(beers, more)

        binding.listEmpty.visibility =
            if (beers.isEmpty() && repo.page == 1) View.VISIBLE else View.GONE
    }

    /**
     * Unable to obtain beers from services
     */
    override fun onFailure() {
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Called when an item is clicked in the list
     */
    override fun OnItemClicked(item: Beer) {
        if (activity is BeerNavigation)
            (activity as BeerNavigation).showBeerDetail(item);
        else
            throw IllegalStateException("Activity must implement BeerNavigation")
    }

    /**
     * Called when a filter button is clicked
     * @param type whether is start or end date, or the filter has been cleared
     */
    fun onFilterButtonClicked(type: FILTERTYPE) {
        if (type == FILTERTYPE.CLEAR) {
            repo.filter.clear()
            loadBeers(true)
            binding.listFilterSinceBt.setText(R.string.list_filter_brewed_after)
            binding.listFilterToBt.setText(R.string.list_filter_brewed_before)
            return
        }

        val listener = DatePickerDialog.OnDateSetListener { view, selYear, selMonth, dayOfMonth ->
            // On date chosen (month starts from 0)

            if (type == FILTERTYPE.SINCE) {
                repo.filter.setBrewedAfter(selMonth, selYear)
                binding.listFilterSinceBt.setText(repo.filter.brewedAfter)
            } else {
                repo.filter.setBrewedBefore(selMonth, selYear)
                binding.listFilterToBt.setText(repo.filter.brewedBefore)
            }

            // Update list
            loadBeers(true)
        }
        val year = if (type == FILTERTYPE.SINCE) repo.filter.brewedAfterYear else repo.filter.brewedBeforeYear
        val month = if (type == FILTERTYPE.SINCE) repo.filter.brewedAfterMonth else repo.filter.brewedBeforeMonth
        val dpd = DatePickerDialog(requireContext(), R.style.beer_date_picker_dialog, listener, year, month - 1,1)
        // Try to hide unused day spinner
        val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinnerView = dpd.datePicker.findViewById<View>(daySpinnerId)
            if (daySpinnerView != null)
                daySpinnerView.visibility = View.GONE
        }
        dpd.show()
    }

    /**
     * Called when last list item is displayed.
     * Used to fetch more elements
     */
    override fun OnLastItemScrolled() {
        repo.page++
        loadBeers(false)
    }

    /**
     * Toggles beer filter visibility
     */
    private fun toggleFilter() {
        binding.listFilterDropdown.visibility =
            if (binding.listFilterDropdown.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    /**
     * Loads beers from network from first page
     * @param clear: clears the list and fetch from first element
     */
    private fun loadBeers(clear: Boolean) {
        if (clear) {
            binding.listProgress.visibility = View.VISIBLE
            rvAdapter.clear()
            repo.reset()
        }

        // Load with filter
        repo.getBeers()
    }

}