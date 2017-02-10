package eventcoordinator2017.myevent.ui.pack;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Category;
import eventcoordinator2017.myevent.ui.pack.tabs.PackPageFragment;

/**
 * Created by Mark Jansen Calderon on 2/8/2017.
 */

public class PackPageFragmentAdapter extends FragmentPagerAdapter {
    private List<Category> categoryList;
    private Context context;

    public PackPageFragmentAdapter(FragmentManager fm, Context context, List<Category> categoryList) {
        super(fm);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PackPageFragment.newInstance(position + 1,categoryList.get(position).getPackageId(),categoryList.get(position).getCategoryId());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return categoryList.get(position).getCategoryName();
    }
}