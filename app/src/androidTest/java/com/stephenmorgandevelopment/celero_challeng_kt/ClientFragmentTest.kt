package com.stephenmorgandevelopment.celero_challeng_kt


import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@HiltAndroidTest
@ExperimentalCoroutinesApi
class ClientFragmentTest {

    @get:Rule val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltAndroidRule.inject()

    }

    @Test
    fun clickFirstClientItemInList_navigateToFirstClientDetailPage() {
        val fragmentManager = mock(FragmentManager::class.java)
        lateinit var fragment: ClientFragment

        launchFragmentInHiltContainer<ClientListFragment> {
            fragment = ClientFragment().apply {
                arguments = Bundle().apply {
                    putLong(ClientFragment.IDENTIFIER_TAG, 100L)
                }
            }
        }

        onData(anything()).inAdapterView(withId(R.id.client_listview))
            .atPosition(0).perform(click())

        verify(fragmentManager).beginTransaction()
            .replace(R.id.container, fragment, ClientFragment.TAG)
    }

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