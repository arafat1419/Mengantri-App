package com.arafat1419.mengantri_app.company.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyScanBinding
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.budiyev.android.codescanner.*

class CompanyScanFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner

    private var _binding: FragmentCompanyScanBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@CompanyScanFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyScanBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

        binding?.apply {
            codeScanner = CodeScanner(requireContext(), codeScannerView)
            codeScanner.apply {
                camera = CodeScanner.CAMERA_BACK
                formats = CodeScanner.ALL_FORMATS
                autoFocusMode = AutoFocusMode.SAFE
                scanMode = ScanMode.SINGLE
                isAutoFocusEnabled = true
                isFlashEnabled = false
                decodeCallback = DecodeCallback {
                    Log.d("TST", it.text)
                    activity?.runOnUiThread {
                        txtQueue.text = it.text
                        btnProgress.isEnabled = true
                    }
                }
                errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                    activity?.runOnUiThread {
                        Log.e("Camera Error", "Camera initialization error: ${it.message}")
                    }
                }
            }

            codeScannerView.setOnClickListener {
                codeScanner.startPreview()
            }

            btnProgress.setOnClickListener {
                navigateToHome(txtQueue.text.toString().toInt())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                codeScanner.startPreview()
            } else {
                Toast.makeText(context, R.string.permission_rejected, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun navigateToHome(ticketId: Int) {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")
            ).also {
                it.putExtra(StatusHelper.EXTRA_FRAGMENT_STATUS, true)
                it.putExtra(StatusHelper.EXTRA_TICKET_ID, ticketId)
                startActivity(it)
            }
        } catch (e: Exception) {
            Toast.makeText(context, com.arafat1419.mengantri_app.R.string.module_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}