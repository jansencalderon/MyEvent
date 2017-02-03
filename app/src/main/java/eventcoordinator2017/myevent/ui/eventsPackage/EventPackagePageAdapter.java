package eventcoordinator2017.myevent.ui.eventsPackage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.ui.lesson.detail.LessonDetailListFragment;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.model.data.Category;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.ui.eventsPackage.detail.PackageCategoryListFragment;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

class EventPackagePageAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = EventPackagePageAdapter.class.getSimpleName();
    private final List<Category> categoryList;
    private final int packageId;

    EventPackagePageAdapter(FragmentManager fm, int packageId) {
        super(fm);
        this.packageId = packageId;
        categoryList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return PackageCategoryListFragment.newInstance(packa, lessonList.get(position).getId(),
                position == (lessonList.size() - 1), query, firstLessonId);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    void setCategoryList(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}