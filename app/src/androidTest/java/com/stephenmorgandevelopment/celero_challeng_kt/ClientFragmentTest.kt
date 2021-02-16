package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.viewModels
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
class ClientFragmentTest {
    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)



}
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class ClientFragmentTest {
//    private lateinit var testActivity: AppCompatActivity
//    private lateinit var fragment: ClientFragment
//
//    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)
//
//    @AndroidEntryPoint
//    class TestActivity : AppCompatActivity() {
//        override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//            super.onCreate(savedInstanceState, persistentState)
//            setContentView(R.layout.activity_main)
//        }
//
//        val fragman = supportFragmentManager
//    }
//
//    @AndroidEntryPoint
//    class TestFragment : Fragment() {
//        @Inject lateinit var viewModel: ClientViewModel
//    }
//
//    @Before
//    fun init() {
//        testActivity = TestActivity()
//        fragment = ClientFragment()
//    }
//
//    @Test
//    fun attachFragment() {
//        val bundle = Bundle().apply {
//            putLong(ClientFragment.IDENTIFIER_TAG, -1)
//        }
//        fragment.arguments = bundle
//
//        val transaction = testActivity.supportFragmentManager.beginTransaction()
//        transaction.add(R.id.container, fragment)
//
//        transaction.commit()
//
//        checkTextById(R.id.clients_name, "Name")
//    }
//
////    @Test
////    fun testUpdateUiChangesText() {
////        val scenario: FragmentScenario<ClientFragment> =
////            launchFragmentInContainer(makeTestBundle(-1L))
////
////        var client: Client = Client.getEmpty()
////        assertTextMatches(client)
////
////        client= Client(
////            1,
////            1,
////            "testName",
////            "testPhone",
////            ProfilePicture.getEmpty(),
////            Location.getEmpty(),
////            "testReason",
////            listOf("test")
////            )
////
////        scenario.onFragment { fragment ->
////            fragment.updateUi(client)
////        }
////
////        assertTextMatches(client)
////    }
//
//    private fun assertTextMatches(client: Client) {
//        checkTextById(R.id.clients_name, client.name)
//        checkTextById(R.id.phone_number, client.phoneNumber)
//        checkTextById(R.id.address_line_one, client.getStreetAddress())
//        checkTextById(R.id.address_line_two, client.getParsedCityStatePostalCode())
//        checkTextById(R.id.service_reason, client.serviceReason)
//    }
//
//    private fun checkTextById(id: Int, string: String) {
//        onView(withId(id)).check(matches(withText(string)))
//    }
//
//    private fun makeTestBundle(id: Long) : Bundle {
//        return Bundle().apply {
//            putLong(ClientFragment.IDENTIFIER_TAG, id)
//        }
//    }
//
//
//}