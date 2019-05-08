package eventcoordinator2017.myevent.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import eventcoordinator2017.myevent.ui.main.tabs.MainPageFragment

/**
 * Created by Mark Jansen Calderon on 2/8/2017.
 */

class MainPageFragmentAdapter(fm: FragmentManager, private val context: Context, private val strings: List<String>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return strings.size
    }

    override fun getItem(position: Int): Fragment {
        return MainPageFragment.newInstance(position + 1, strings[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return strings[position]
    }
}