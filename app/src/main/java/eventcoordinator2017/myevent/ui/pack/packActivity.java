package eventcoordinator2017.myevent.ui.pack;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityPackBinding;
import eventcoordinator2017.myevent.model.data.Category;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.events.add.packages.EventAddPackageActivity;
import eventcoordinator2017.myevent.utils.PaletteBitmap;
import eventcoordinator2017.myevent.utils.PaletteBitmapTranscoder;
import io.realm.Realm;

/**
 * Created by Sen on 2/6/2017.
 */

public class PackActivity extends MvpActivity<PackView, PackPresenter> implements PackView {
    ActivityPackBinding binding;
    private Realm realm;
    private Package aPackage;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pack);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int id = i.getIntExtra(Constants.ID, -1);
        if (id != -1) {
            aPackage = realm.copyFromRealm(realm.where(Package.class).equalTo(Constants.PACKAGE_ID, id).findFirst());
            binding.setAPackage(aPackage);
        }

        categoryList = aPackage.getCategory();

        binding.viewpager.setAdapter(new PackPageFragmentAdapter(getSupportFragmentManager(), this, categoryList));
        binding.slidingTabs.setupWithViewPager(binding.viewpager);


        binding.collapsingToolBar.setTitleEnabled(false);
        binding.collapsingToolBar.setTitle(aPackage.getPackageName());
        binding.collapsingToolBar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        binding.collapsingToolBar.setScrimVisibleHeightTrigger(104);

        Glide.with(this).load(Constants.URL_IMAGE + aPackage.getImageDirectory()).asBitmap()
                .transcode(new PaletteBitmapTranscoder(this), PaletteBitmap.class)
                .into(new ImageViewTarget<PaletteBitmap>(binding.packageImage) {

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        binding.packageImage.setImageBitmap(resource.bitmap);
                        int colorP = resource.palette.getMutedColor(ContextCompat.getColor(PackActivity.this, R.color.colorPrimary));
                        int colorD = resource.palette.getDarkMutedColor(ContextCompat.getColor(PackActivity.this, R.color.colorPrimaryDark));

                        binding.collapsingToolBar.setStatusBarScrimColor(colorP);
                        binding.collapsingToolBar.setContentScrimColor(colorD);
                    }
                });
        ;

    }


    @Override
    public void onAvail(final Package aPackage){
        final Realm realm = Realm.getDefaultInstance();
        final TempEvent tempEvent = realm.where(TempEvent.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                tempEvent.setPackageId(aPackage.getPackageId());
                tempEvent.setaPackage(aPackage);
                realm.close();
            }
        });
        startActivity(new Intent(this,EventAddActivity.class));
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, EventAddPackageActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, EventAddPackageActivity.class));
        super.onBackPressed();
    }

    @NonNull
    @Override
    public PackPresenter createPresenter() {
        return new PackPresenter();
    }
}
