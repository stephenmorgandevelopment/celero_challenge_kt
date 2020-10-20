package com.stephenmorgandevelopment.celero_challeng_kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.stephenmorgandevelopment.celero_challeng_kt.adapters.AllClientsAdapter
import com.stephenmorgandevelopment.celero_challeng_kt.databinding.ClientListViewBinding
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.AllClientsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientListFragment : Fragment() {
    val TAG = ClientListFragment::class.java.simpleName

    private var _binding: ClientListViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllClientsViewModel by viewModels(
        factoryProducer = { defaultViewModelProviderFactory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ClientListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val adapter = AllClientsAdapter(context ?: binding.root.context)
        binding.clientListview.adapter = adapter

        binding.clientListview.onItemClickListener = itemClickListener()

        viewModel.clients.observe(viewLifecycleOwner) {
            adapter.setClientList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun itemClickListener() =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val fragment = ClientFragment(id)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.container, fragment, fragment.TAG)
            transaction.commit()
        }
}