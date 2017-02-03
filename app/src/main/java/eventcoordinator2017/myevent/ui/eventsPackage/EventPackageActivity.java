package eventcoordinator2017.myevent.ui.eventsPackage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventPackageDetailBinding;
import eventcoordinator2017.myevent.model.data.Category;
import eventcoordinator2017.myevent.model.data.Package;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Mark Jansen Calderon on 2/2/2017.
 */

public class EventPackageActivity extends MvpActivity<EventPackageView,EventPackagePresenter> implements EventPackageView,ViewPager.OnPageChangeListener {

    private Realm realm;
    private ActivityEventPackageDetailBinding binding;
    private Package aPackage;
    private EventPackagePageAdapter eventPackagePageAdapter;

    @SuppressWarnings("ConstantsConditions") // assumes that toolbar is setup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance(); // init realm
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_package_detail);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // checking for intent pass
        int packageId = getIntent().getIntExtra(Constants.ID, -1);
        if (packageId == -1) {
            Toast.makeText(this, "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        //check if has data
        aPackage = realm.where(Package.class).equalTo(Constants.ID, packageId).findFirst();
        if (aPackage == null) {
            Toast.makeText(this, "No Topic Object Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(aPackage.getPackageName());

        eventPackagePageAdapter = new EventPackagePageAdapter(getSupportFragmentManager(), packageId);
        binding.container.addOnPageChangeListener(this);
        binding.container.setAdapter(eventPackagePageAdapter);

        setUiPageViewController();
    }

    @NonNull
    @Override
    public EventPackagePresenter createPresenter() {
        return new EventPackagePresenter();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setUiPageViewController() {
        // setup view page controller specially the counter indicator
        eventPackagePageAdapter.setCategoryList(realm.copyFromRealm(aPackage.getCategory().sort("categoryId",Sort.DESCENDING)));
        dotsCount = lessonPageAdapter.getCount();
        if (dotsCount <= 0) return;
        dots = new ImageView[dotsCount];
        binding.viewPagerCountDots.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            binding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_item_dot));
    }
}
