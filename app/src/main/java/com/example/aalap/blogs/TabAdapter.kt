package com.example.aalap.blogs

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private var list: MutableList<Fragment> = mutableListOf()

    fun addFragment(fragment: Fragment) {
        list.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Search"
            1 -> "Watch List"
            2 -> "Post"
            3 -> "Account"
            else -> {
                "Title"
            }
        }
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

}