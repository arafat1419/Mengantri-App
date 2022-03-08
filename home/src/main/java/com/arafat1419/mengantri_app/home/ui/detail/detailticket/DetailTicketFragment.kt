package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailTicketBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.detail.detailservice.DetailServiceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class DetailTicketFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailTicketBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: DetailServiceViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@DetailTicketFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTicketBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_TICKET_ID = "extra_ticket_id"
    }
}