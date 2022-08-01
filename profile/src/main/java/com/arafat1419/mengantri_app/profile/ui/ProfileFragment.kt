package com.arafat1419.mengantri_app.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.Helper.setAvatarGenerator
import com.arafat1419.mengantri_app.profile.R
import com.arafat1419.mengantri_app.profile.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProfileFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, ProfilePrefFragment())
            .commit()

        binding.apply {
            txtName.text = sessionManager.fetchCustomerName()
            txtEmail.text = sessionManager.fetchCustomerEmail()

            val avatar = sessionManager.fetchCustomerName()?.let {
                setAvatarGenerator(
                    requireContext(),
                    it
                )
            }

            Glide.with(this@ProfileFragment)
                .load(avatar)
                .into(imgProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}