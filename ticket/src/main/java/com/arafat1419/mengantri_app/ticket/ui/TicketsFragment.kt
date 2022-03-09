package com.arafat1419.mengantri_app.ticket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.ticket.databinding.FragmentTicketsBinding
import com.arafat1419.mengantri_app.ticket.di.ticketsModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class TicketsFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: TicketsViewModel by viewModel()

    private lateinit var navHostFragment: Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTicketsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(ticketsModule)

        // Initialize nav host fragment as fragment container
        val navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}