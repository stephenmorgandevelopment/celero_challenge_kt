package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.stephenmorgandevelopment.celero_challeng_kt.databinding.ClientDetailViewBinding
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientFragment(private val identifier: Long) : Fragment() {
    val TAG = ClientFragment::class.java.simpleName

    private var _binding: ClientDetailViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by viewModels(
        factoryProducer = { defaultViewModelProviderFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setClient(identifier)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ClientDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        updateUi(viewModel.liveClient.value)
        viewModel.liveClient.observe(viewLifecycleOwner) {
            updateUi(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(curClient: Client?) {
        if (curClient != null) {
            val addressTwoString =
                "${curClient.location.address.city}, ${curClient.location.address.state}, ${curClient.location.address.postalCode}"

            binding.clientsName.text = curClient.name
            binding.phoneNumber.text = curClient.phoneNumber
            binding.addressLineOne.text = curClient.location.address.street
            binding.addressLineTwo.text = addressTwoString
            binding.serviceReason.text = curClient.serviceReason

            val imgUri = curClient.profilePicture.large.toUri().buildUpon().scheme("https").build()
            Glide.with(binding.profilePicture.context).load(imgUri).into(binding.profilePicture)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            curClient.problemPictures.forEach {
                val imgView = ImageView(context)
                imgView.layoutParams = params
                imgView.adjustViewBounds = true

                val uri = it.toUri().buildUpon().scheme("https").build()
                Glide.with(binding.problemsScroller.context).load(uri).into(imgView)

                binding.problemsScroller.addView(imgView)
            }

            binding.openInMaps.setOnClickListener { view ->
                val latitude = curClient.location.coordinate.latitude
                val longitude = curClient.location.coordinate.longitude
                val uriString = "google.navigation:q=${latitude},${longitude}".toUri()

                val directionsIntent = Intent(Intent.ACTION_VIEW, uriString)
                directionsIntent.setPackage("com.google.android.apps.maps")

                directionsIntent.resolveActivity(requireContext().packageManager)?.let {
                    startActivity(directionsIntent)
                }
            }
        }
    }
}
