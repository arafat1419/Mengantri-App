package com.arafat1419.mengantri_app.home.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.home.databinding.FragmentSearchBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SearchFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@SearchFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabsConfig()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        sendKeyword()
        onItemClicked()
    }

    private fun sendKeyword() {
        binding.edtSearch.apply {
            requestFocus()
            doOnTextChanged { text, _, _, _ ->
                setFragmentResult(
                    EXTRA_SEARCH_KEY_COMPANY, bundleOf(
                        EXTRA_SEARCH_KEYWORD to text.toString()
                    )
                )
                setFragmentResult(
                    EXTRA_SEARCH_KEY_SERVICE, bundleOf(
                        EXTRA_SEARCH_KEYWORD to text.toString()
                    )
                )
            }
        }
    }

    private fun onItemClicked() {
        binding.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this@SearchFragment).navigateUp()
        }
    }

    private fun tabsConfig() {
        val searchPagerAdapter = SearchPagerAdapter(childFragmentManager, lifecycle)

        binding.apply {
            viewPager.adapter = searchPagerAdapter
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                tab.text = resources.getString(TAB_TITLES[pos])
                tab.icon = ContextCompat.getDrawable(requireContext(), TAB_ICON[pos])
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SEARCH_KEYWORD = "extra_search_keyword"
        const val EXTRA_SEARCH_KEY_COMPANY = "extra_search_key_company"
        const val EXTRA_SEARCH_KEY_SERVICE = "extra_search_key_service"

        private val TAB_TITLES = arrayOf(
            R.string.company,
            R.string.service
        )

        private val TAB_ICON = arrayOf(
            R.drawable.ic_outline_business_24,
            R.drawable.ic_outline_miscellaneous_services_24
        )
    }
}