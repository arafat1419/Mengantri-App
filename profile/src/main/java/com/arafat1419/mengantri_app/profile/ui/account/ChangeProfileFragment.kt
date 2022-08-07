package com.arafat1419.mengantri_app.profile.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.Helper
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.profile.databinding.FragmentChangeProfileBinding
import com.arafat1419.mengantri_app.profile.di.profileModule
import com.arafat1419.mengantri_app.profile.ui.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class ChangeProfileFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentChangeProfileBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: ProfileViewModel by viewModel()

    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@ChangeProfileFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangeProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(profileModule)

        setData()
        onItemClicked()
    }

    private fun onItemClicked() {
        binding.apply {
            btnBack.setOnClickListener {
                NavHostFragment.findNavController(this@ChangeProfileFragment).navigateUp()
            }
            btnSubmit.setOnClickListener {
                if (checkEditText()) {
                    updateProfile(edtName.text.toString(), edtPhone.text.toString())
                }
            }
        }
    }

    private fun updateProfile(name: String, phone: String) {
        viewModel.updateProfile(
            sessionManager.fetchCustomerId(),
            name,
            phone
        ).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    isLoading(false)
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> isLoading(true)
                is Resource.Success -> {
                    isLoading(false)
                    val customer = result.data

                    if (customer != null) {
                        sessionManager.clearCustomer()
                        sessionManager.saveCustomer(customer)

                        Toast.makeText(context, getString(R.string.update_profile_success), Toast.LENGTH_SHORT).show()
                        NavHostFragment.findNavController(this@ChangeProfileFragment).navigateUp()
                    }
                }
            }
        }
    }

    private fun setData() {
        binding.apply {
            val avatar = sessionManager.fetchCustomerName()?.let {
                Helper.setAvatarGenerator(
                    requireContext(),
                    it
                )
            }

            Glide.with(this@ChangeProfileFragment)
                .load(avatar)
                .into(imgProfile)


            edtName.setText(sessionManager.fetchCustomerName())
            edtPhone.setText(sessionManager.fetchCustomerPhone())
        }
    }

    private fun isLoading(state: Boolean) {
        binding.loading.root.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun checkEditText(): Boolean {
        var check: Boolean
        binding.apply {
            check = when {
                edtName.text?.isEmpty() == true -> {
                    Toast.makeText(
                        context,
                        R.string.name_cannot_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    false
                }
                edtPhone.text?.isEmpty() == true -> {
                    Toast.makeText(
                        context,
                        R.string.phone_cannot_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    false
                }
                else -> {
                    true
                }
            }
        }
        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}