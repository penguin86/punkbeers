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

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.danieleverducci.punkbeers.MainActivity
import it.danieleverducci.punkbeers.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BeersFragmentTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun filter_BeersFragmentTest() {
        // Filters for the current date as brewed start date (we expect 0 results)

        // Click menu item
        onView(withId(R.id.action_filter)).perform(click())
        // Click filter button
        onView(withId(R.id.list_filter_since_bt)).perform(click())
        // Click ok on datepicker
        onView(withText("OK")).perform(click())
        Thread.sleep(500)
        // Check the list empty message is correctly displayed
        onView(withId(R.id.list_empty))
            .check(matches(isDisplayed()))
    }

}