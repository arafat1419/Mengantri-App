package com.arafat1419.mengantri_app.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.profile.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getView()?.findViewById<FrameLayout>(R.id.frame_profile) != null) {
            if (savedInstanceState != null) {
                return
            }
            // below line is to inflate our fragment.
            parentFragmentManager.beginTransaction()
                .add(R.id.frame_profile, ProfilePrefFragment())
                .commit()
        }
    }
}