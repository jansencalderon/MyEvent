package eventcoordinator2017.myevent.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.ui.main.tabs.MainPageFragment;

/**
 * Created by Mark Jansen Calderon on 2/8/2017.
 */

public class MainPageFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<String> strings;

    public MainPageFragmentAdapter(FragmentManager fm, Context context, List<String> strings) {
        super(fm);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Fragment getItem(int position) {
        return MainPageFragment.newInstance(position + 1, strings.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return strings.get(position);
    }
}