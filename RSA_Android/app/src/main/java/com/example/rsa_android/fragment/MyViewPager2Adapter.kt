package com.example.rsa_android.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GenerateKeyFragment()
            1 -> EncryptFragment()
            else -> DecryptFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}