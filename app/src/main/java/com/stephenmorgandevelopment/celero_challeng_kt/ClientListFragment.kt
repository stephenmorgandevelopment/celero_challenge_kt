package com.stephenmorgandevelopment.celero_challeng_kt

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.stephenmorgandevelopment.celero_challeng_kt.adapters.AllClientsAdapter
import com.stephenmorgandevelopment.celero_challeng_kt.databinding.ClientListViewBinding
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.AllClientsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListFragment : Fragment() {
    val TAG = ClientListFragment::class.java.simpleName
    private val clientFragmentTag = ClientFragment::class.java.simpleName

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
            if (it != null) {
                adapter.setClientList(it)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refreshMenuBtn -> {
                viewModel.viewModelScope.launch {
                    viewModel.refreshList()
                    // Runs test hosted on my domain to check for list update.
//                    viewModel.refreshTestList()
                }

                viewModel.clients.observe(viewLifecycleOwner) {
                    if (it != null) {
                        val adp = binding.clientListview.adapter as AllClientsAdapter
                        adp.setClientList(it)
                    }
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

    private fun itemClickListener() =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val fragment = ClientFragment(id, viewModel.clientRepo)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.container, fragment, clientFragmentTag)
            transaction.commit()
        }
}