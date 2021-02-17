package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.stephenmorgandevelopment.celero_challeng_kt.databinding.ClientDetailViewBinding
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.viewmodels.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientFragment() : Fragment() {
    private var _binding: ClientDetailViewBinding? = null
    private val binding get() = _binding!!

    val clientViewModel: ClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (clientViewModel.identifier == -1L) {
            val identifier = arguments?.getLong(IDENTIFIER_TAG)
                ?: throw IllegalArgumentException("Missing identifier.")

            clientViewModel.setClient(identifier)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ClientDetailViewBinding.inflate(inflater, container, false)

        clientViewModel.liveClient.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        clientViewModel.liveClient.value?.let { updateUi(it) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateUi(curClient: Client) {
        updateText(curClient)
        loadPic(curClient.profilePictureAsUri, binding.profilePicture)
        curClient.getProblemPicturesAsUri().forEach {
            addProblemPic(it)
        }

        binding.openInMaps.setOnClickListener { _ ->
            val uriString = curClient.getMapsUriString()
            launchDirectionsWithUri(uriString)
        }
    }

    private fun updateText(curClient: Client) {
        binding.clientsName.text = curClient.name
        binding.phoneNumber.text = curClient.phoneNumber
        binding.addressLineOne.text = curClient.getStreetAddress()
        binding.addressLineTwo.text = curClient.getParsedCityStatePostalCode()
        binding.serviceReason.text = curClient.serviceReason
    }

    private fun loadPic(imgUri: Uri, imageView: ImageView) {
        Glide.with(requireContext()).load(imgUri).into(imageView)
    }

    private fun addProblemPic(imgUri: Uri) {
        val imgView = makeImageViewForProblemPics()
        loadPic(imgUri, imgView)
        binding.problemsScroller.addView(imgView)
    }

    private fun launchDirectionsWithUri(uriString: Uri) {
        val directionsIntent = Intent(Intent.ACTION_VIEW, uriString)
        directionsIntent.setPackage("com.google.android.apps.maps")

        directionsIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(directionsIntent)
        }
    }

    private val problemPicParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private fun makeImageViewForProblemPics(): ImageView {
        val imgView = ImageView(context)
        imgView.layoutParams = problemPicParams
        imgView.adjustViewBounds = true
        return imgView
    }

    companion object {
        val IDENTIFIER_TAG = "identifier"
        val TAG = ClientFragment::class.java.simpleName
    }
}
