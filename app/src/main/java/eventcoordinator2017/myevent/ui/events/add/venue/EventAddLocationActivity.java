package eventcoordinator2017.myevent.ui.events.add.venue;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventAddLocationBinding;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.pack.PackActivity;
import eventcoordinator2017.myevent.ui.venue.LocationActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 2/14/2017.
 */

public class EventAddLocationActivity extends MvpActivity<EventAddLocationView,EventAddLocationPresenter> implements EventAddLocationView{

    private ActivityEventAddLocationBinding binding;
    private Realm realm;
    private EventLocationListAdapter locationListAdapter;
    private TempEvent tempEvent;

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
        binding.eventBudget.setText(tempEvent.getBudget());
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
    public void onNext() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

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
        super.onStop();
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
