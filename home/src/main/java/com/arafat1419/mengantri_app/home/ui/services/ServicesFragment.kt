package com.arafat1419.mengantri_app.home.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.ServicesAdapter
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.arafat1419.mengantri_app.home.databinding.FragmentServicesBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.detail.detailservice.DetailServiceFragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


@ExperimentalCoroutinesApi
@FlowPreview
class ServicesFragment : Fragment(), AdapterCallback<ServiceCountDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: ServicesViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    private var companyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@ServicesFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentServicesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getCompanyDomain = arguments?.getParcelable<CompanyDomain>(EXTRA_COMPANY_DOMAIN)

        if (getCompanyDomain != null) {
            showDataCompany(getCompanyDomain)
            companyName = getCompanyDomain.companyName
        }

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        // get service and served from view model and set the data to services adapter
        viewModel.getServiceAndServed(getCompanyDomain?.companyId!!).observe(viewLifecycleOwner) {
            binding?.rvServices?.adapter.let { adapter ->
                when (adapter) {
                    is ServicesAdapter -> {
                        adapter.setData(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // move to companies fragment with category domain
    override fun onItemClicked(data: ServiceCountDomain) {
        val bundle = bundleOf(
            DetailServiceFragment.EXTRA_SERVICE_COUNT_DOMAIN to data,
            DetailServiceFragment.EXTRA_COMPANY_NAME to companyName
        )
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.R.id.action_servicesFragment_to_detailServiceFragment,
            bundle
        )
    }

    private fun showDataCompany(data: CompanyDomain) {
        binding?.apply {
            txtServicesAppTitle.text = data.companyName
            Glide.with(this@ServicesFragment)
                .load(DataMapper.imageDirectus + data.companyBanner)
                .into(imgServicesBanner)

            Glide.with(this@ServicesFragment)
                .load(DataMapper.imageDirectus + data.companyImage)
                .into(imgServicesProfile)
            txtServicesId.text = resources.getString(R.string.id_format, data.companyId.toString())
            txtServicesTitle.text = data.companyName
            txtServicesAddress.text = data.companyAddress
            val timeFormat = resources.getString(R.string.time_format)
            txtServicesTime.text = String.format(
                timeFormat, data.companyOpenTime?.substring(0..4),
                data.companyCloseTime?.substring(0..4)
            )
            txtServicesPhone.text = data.companyPhone
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvServices?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ServicesAdapter(this@ServicesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_COMPANY_DOMAIN = "extra_company_domain"
    }
}