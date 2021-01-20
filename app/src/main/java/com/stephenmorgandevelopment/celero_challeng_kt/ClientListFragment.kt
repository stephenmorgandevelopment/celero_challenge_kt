package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import com.stephenmorgandevelopment.celero_challeng_kt.adapters.AllClientsAdapter
import com.stephenmorgandevelopment.celero_challeng_kt.databinding.ClientListViewBinding
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.AllClientsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListFragment : Fragment() {
    private var _binding: ClientListViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllClientsViewModel by viewModels(
        factoryProducer = { defaultViewModelProviderFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ClientListViewBinding.inflate(inflater, container, false)

        val adapter = AllClientsAdapter(inflater)
        binding.clientListview.adapter = adapter
        binding.clientListview.onItemClickListener = itemClickListener()

        viewModel.clients.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.setClientList(it)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    // TODO Implement this "test" in unit tests.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refreshMenuBtn -> {
                viewModel.viewModelScope.launch {
//                    viewModel.refreshList()
                    // Runs test hosted on my domain to check for list changed update.
                    // Uncomment line 61 and comment line 65 to update list from official challenge.
                    // Vice versa to update list with test list from my domain.
                    viewModel.refreshTestList()
                }

                viewModel.clients.observe(viewLifecycleOwner) {
                    updateList(it)
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList(clients: List<SimpleClient>) {
        if (clients.isNotEmpty()) {
            val adapter = binding.clientListview.adapter as AllClientsAdapter
            adapter.setClientList(clients)
        }
    }

    private fun itemClickListener() =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val fragment = ClientFragment().apply {
                arguments = makeBundleWithIdentifier(id)
            }

            val transaction = parentFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.container, fragment, ClientFragment.TAG)
            transaction.commit()
        }

    private fun makeBundleWithIdentifier(identifier: Long) : Bundle {
        return Bundle().apply {
            putLong(ClientFragment.IDENTIFIER_TAG, identifier)
        }
    }

    companion object {
        val TAG = ClientListFragment::class.java.simpleName
    }
}