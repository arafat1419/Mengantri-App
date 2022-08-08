package com.arafat1419.mengantri_app.login.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.databinding.BottomConfirmationBinding
import com.arafat1419.mengantri_app.login.databinding.FragmentForgotPasswordBinding
import com.arafat1419.mengantri_app.login.di.loginModule
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class ForgotPasswordFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: ForgotPasswordViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.login.R.id.login_container)
    }

    private val getCustomerId: Int? by lazy { arguments?.getInt(EXTRA_CUSTOMER_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@ForgotPasswordFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        onItemClicked()
    }

    private fun onItemClicked() {
        binding.apply {
            btnBack.setOnClickListener {
                NavHostFragment.findNavController(this@ForgotPasswordFragment).navigateUp()
            }
            btnSubmit.setOnClickListener {
                if (checkEditText() && getCustomerId != null) {
                    showDialogAlert(edtNewPassword.text.toString().trim())
                }
            }
        }
    }

    private fun updatePassword(newPassword: String) {
        viewModel.updatePassword(getCustomerId!!, newPassword)
            .observe(viewLifecycleOwner) { result ->
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
                            Toast.makeText(
                                context,
                                R.string.update_password_success,
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToLogin()
                        }
                    }
                }
            }
    }

    private fun showDialogAlert(newPassword: String) {
        val sheetBinding = BottomConfirmationBinding.inflate(LayoutInflater.from(context))
        val builder = BottomSheetDialog(requireContext())

        sheetBinding.apply {
            txtMessageTitle.text = getString(R.string.change_password_title)
            txtMessage.text = getString(R.string.change_password_message)

            btnYes.setOnClickListener {
                updatePassword(newPassword)
                builder.dismiss()
            }

            btnNo.setOnClickListener {
                builder.dismiss()
            }
        }

        builder.apply {
            setCancelable(true)
            setContentView(sheetBinding.root)
            show()
        }
    }

    private fun isLoading(state: Boolean) {
        binding.loading.root.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun checkEditText(): Boolean {
        var check: Boolean
        binding.apply {
            check = when {
                edtNewPassword.text?.toString()?.length!! < 8 ||
                        edtConfirmPassword.text?.toString()?.length!! < 8 -> {
                    Toast.makeText(
                        context,
                        R.string.minimum_password,
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                edtNewPassword.text?.toString()?.trim() != edtConfirmPassword.text?.toString()
                    ?.trim() -> {
                    Toast.makeText(
                        context,
                        R.string.password_different,
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> {
                    true
                }
            }
        }
        return check
    }

    private fun navigateToLogin() {
        // Navigate to Login
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.login.R.id.action_forgotPasswordFragment_to_loginFragment
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_CUSTOMER_ID = "extra_customer_id"
    }
}