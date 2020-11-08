package com.stephenmorgandevelopment.celero_challeng_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listFragment = ClientListFragment()

        setSupportActionBar(findViewById(R.id.toolbar))

        val fragmentManager = supportFragmentManager
        if(!hasFragmentAlready(supportFragmentManager)) {
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.add(R.id.container, listFragment, ClientListFragment.TAG)
            fragmentTransaction.commit()
        }
    }

    private fun hasFragmentAlready(fragmentManager: FragmentManager): Boolean {
        return fragmentManager.findFragmentByTag(ClientListFragment.TAG) != null
                || fragmentManager.findFragmentByTag(ClientFragment.TAG) != null
    }
}
