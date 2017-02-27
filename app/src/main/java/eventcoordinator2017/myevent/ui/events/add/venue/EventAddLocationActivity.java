package eventcoordinator2017.myevent.ui.events.add.venue;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventAddLocationBinding;
import eventcoordinator2017.myevent.databinding.DialogFilterLocationBinding;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.venue.LocationActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 2/14/2017.
 */

public class EventAddLocationActivity extends MvpActivity<EventAddLocationView, EventAddLocationPresenter> implements EventAddLocationView {

    private ActivityEventAddLocationBinding binding;
    private Realm realm;
    private EventLocationListAdapter locationListAdapter;
    private TempEvent tempEvent;
    private String filterCapacity ="", filterSetup ="", filterType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add_location);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();
        presenter.onStart();


        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationListAdapter = new EventLocationListAdapter(getMvpView());

        binding.recyclerView.setAdapter(locationListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tempEvent = realm.where(TempEvent.class).findFirst();
        if (tempEvent != null) {
            presenter.setQuery(tempEvent.getBudget());
        }
    }

    @NonNull
    @Override
    public EventAddLocationPresenter createPresenter() {
        return new EventAddLocationPresenter();
    }

    @Override
    public void filter() {
        final Dialog dialogFilter = new Dialog(this);
        final DialogFilterLocationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_filter_location, null, false);
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setCancelable(false);
        dialogFilter.setContentView(binding.getRoot());

        if ((!filterSetup.equals("")))
            binding.filterSetup.setText(filterSetup);
        if (!filterType.equals(""))
            binding.filterVenueType.setText(filterType);
        if (!filterCapacity.equals(""))
            binding.filterCapacity.setText(filterCapacity);

        binding.filterSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EventAddLocationActivity.this)
                        .title("Setup")
                        .items("Indoor",
                                "Outdoor")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                binding.filterSetup.setText(text);
                                filterSetup = binding.filterSetup.getText().toString();
                            }
                        })
                        .show();
            }
        });

        binding.filterVenueType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EventAddLocationActivity.this)
                        .title("Venue Types")
                        .items("Open Ground",
                                "Private Estate",
                                "Conference Room",
                                "Co-Working Space",
                                "Event Space",
                                "Function Room",
                                "Multipurpose Hall",
                                "Conference Room")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                binding.filterVenueType.setText(text);
                                filterType = binding.filterVenueType.getText().toString();
                            }
                        })
                        .show();
            }
        });

        binding.filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCapacity = binding.filterCapacity.getText().toString();
                presenter.setApplyFilter(filterCapacity, filterType, filterSetup);
                dialogFilter.dismiss();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilter.dismiss();
            }
        });

        dialogFilter.show();


    }

    @Override
    public void onNext() {

    }


    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void clearFilter() {
        /*if(!filterSetup.equals("")||!filterCapacity.equals("")||!filterType.equals("")){
            binding.clearFilter.setEnabled(false);
        }*/
        filterCapacity = "";
        filterSetup = "";
        filterType = "";
        presenter.setApplyFilter(filterCapacity, filterType, filterSetup);

    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardClicked(Location location) {
        Intent i = new Intent(this, LocationActivity.class);
        i.putExtra(Constants.ID, location.getLocId());
        startActivity(i);
        finish();
    }

    @Override
    public void setList(List<Location> list) {
        locationListAdapter.setLocations(list);
    }


    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        presenter.onStop();
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
        startActivity(new Intent(this, EventAddActivity.class));
        finish();
    }


}
