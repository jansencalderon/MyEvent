package eventcoordinator2017.myevent.ui.venue;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityLocationBinding;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.events.add.packages.EventAddPackageActivity;
import eventcoordinator2017.myevent.ui.events.add.venue.EventAddLocationActivity;
import io.realm.Realm;

public class LocationActivity extends MvpActivity<LocationView, LocationPresenter> implements LocationView {

    ActivityLocationBinding binding;
    private Location location;
    private Realm realm;
    private TempEvent tempEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        int id = i.getIntExtra(Constants.ID, -1);
        if (id != -1) {
            location = realm.where(Location.class).equalTo(Constants.LOCATION_ID, id).findFirst();
            binding.setLocation(location);
            //set lists
            List<String> list = Arrays.asList(location.getLocCateredEvents().split("-"));
            binding.cateredEvents.setLayoutManager(new LinearLayoutManager(this));
            binding.cateredEvents.setAdapter(new LocationListAdapter(list));

            List<String> list2 = Arrays.asList(location.getLocVenueType().split("-"));
            binding.venueTypes.setLayoutManager(new LinearLayoutManager(this));
            binding.venueTypes.setAdapter(new LocationListAdapter(list2));

            List<String> list3 = Arrays.asList(location.getLocFeatures().split("-"));
            binding.features.setLayoutManager(new LinearLayoutManager(this));
            binding.features.setAdapter(new LocationListAdapter(list3));

            Glide.with(this).load(Constants.URL_IMAGE + location.getLocImage()).into(binding.locationImage);
        }

        tempEvent = realm.where(TempEvent.class).findFirst();
        if (tempEvent == null) {
            binding.selectVenue.setVisibility(View.GONE);
        }


    }

    @Override
    public void onAvail() {
        final Realm realm = Realm.getDefaultInstance();
        final TempEvent tempEvent = realm.where(TempEvent.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                tempEvent.setLocation(location);
                tempEvent.setLocationId(location.getLocId());
                realm.close();
            }
        });
        startActivity(new Intent(this, EventAddActivity.class));
        finish();
    }

    @Override
    public void openOnGoogleMaps() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", location.getLocLat(), location.getLocLong());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }

    @Override
    public void openOnWaze() {
        final String url = String.format(Locale.ENGLISH, "waze://?ll=%f,%f&navigate=yes", location.getLocLat(), location.getLocLong());
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (tempEvent == null) {
            finish();
        } else {
            startActivity(new Intent(this, EventAddLocationActivity.class));
            finish();
        }
    }

    @NonNull
    @Override
    public LocationPresenter createPresenter() {
        return new LocationPresenter();
    }
}
