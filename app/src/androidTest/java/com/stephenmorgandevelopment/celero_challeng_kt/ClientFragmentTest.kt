package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ClientFragmentTest {

    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)

    @Test
    fun launchFragment() {
        val scenario: FragmentScenario<ClientFragment> =
            launchFragmentInContainer(makeTestBundle(-1L))

        onView(withId(R.id.clients_name)).check(matches(withText("Name")))
    }

    @Test
    fun testUpdateUiChangesText() {
        val scenario: FragmentScenario<ClientFragment> =
            launchFragmentInContainer(makeTestBundle(-1L))

        var client: Client = Client.getEmpty()
        assertTextMatches(client)

        client= Client(
            1,
            1,
            "testName",
            "testPhone",
            ProfilePicture.getEmpty(),
            Location.getEmpty(),
            "testReason",
            listOf("test")
            )

        scenario.onFragment { fragment ->
            fragment.updateUi(client)
        }

        assertTextMatches(client)
    }

    private fun assertTextMatches(client: Client) {
        checkTextById(R.id.clients_name, client.name)
        checkTextById(R.id.phone_number, client.phoneNumber)
        checkTextById(R.id.address_line_one, client.getStreetAddress())
        checkTextById(R.id.address_line_two, client.getParsedCityStatePostalCode())
        checkTextById(R.id.service_reason, client.serviceReason)
    }

    private fun checkTextById(id: Int, string: String) {
        onView(withId(id)).check(matches(withText(string)))
    }

    private fun makeTestBundle(id: Long) : Bundle {
        return Bundle().apply {
            putLong(ClientFragment.IDENTIFIER_TAG, id)
        }
    }


}