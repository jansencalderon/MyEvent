package eventcoordinator2017.myevent.ui.events.details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventDetailBinding;
import eventcoordinator2017.myevent.databinding.DialogProfileHostBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Guest;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.events.add.guests.GuestsActivity;
import eventcoordinator2017.myevent.ui.venue.LocationActivity;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventDetailActivity extends MvpActivity<EventDetailView, EventDetailPresenter> implements EventDetailView, OnMapReadyCallback {

    ActivityEventDetailBinding binding;
    private User user;
    private int eventId;
    private List<Guest> guestList;
    private Boolean fromMain;
    private ProgressDialog progressDialog;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        presenter.onStart();
        eventId = getIntent().getIntExtra(Constants.ID, -1);
        fromMain = getIntent().getBooleanExtra("fromMain", false);

        if (getIntent().getBooleanExtra("from_notif", false)) {
            presenter.getEventData(getIntent().getStringExtra(Constants.EVENT_ID));
        } else {
            if (eventId == -1) {
                showAlert("No event data");
                finish();
            } else {
                event = presenter.getEvent(eventId);
                if (event != null) {
                    setEventData(event);
                }
            }
        }


    }

    private void setEventData(Event event) {
        user = App.getUser();
        binding.setEvent(event);
        binding.setUser(user);

        if (user.getUserId() == event.getUserId()) {
            binding.eventResponsePanel.setVisibility(View.GONE);
        } else {
            binding.inviteGuests.setVisibility(View.GONE);
            if (event.getGuests().where().equalTo("email", user.getEmail()).findFirst() == null) {
                binding.eventResponsePanel.setVisibility(View.GONE);
                binding.notInvited.setVisibility(View.VISIBLE);
            } else {
                Guest guest = event.getGuests().where().equalTo("email", user.getEmail()).findFirst();
                onResponseSuccessful(guest.getResponse());
                //showAlert(guest.getResponse());
            }
        }

        Glide.with(this)
                .load(Constants.URL_IMAGE + event.getImageDirectory())
                .centerCrop()
                .dontAnimate()
                .into(binding.eventImage);

        guestList = event.getGuests();
        if (guestList.size() > 0) {
            binding.goingCount.setText(guestList.size() + " people invited");
        } else {
            binding.goingCount.setText("No guests invited yet");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onViewGuests(Event event) {

    }

    @Override
    public void eventBy(User user) {
        DialogProfileHostBinding dialogProfileViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_profile_host, null, false);
        dialogProfileViewBinding.setGuest(user);
        Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .error(R.drawable.ic_mood)
                .dontAnimate()
                .into(dialogProfileViewBinding.guestImage);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogProfileViewBinding.getRoot());
        dialog.show();
    }


    @Override
    public void editEvent(Event event) {
        showAlert(event.getEventName());
    }

    @Override
    public void onLocationClicked(Location location) {
        startActivity(new Intent(this, LocationActivity.class).putExtra(Constants.ID, location.getLocId()));
    }

    @Override
    public void onAdd() {
        startActivity(new Intent(this, GuestsActivity.class).putExtra(Constants.ID, event.getEventId()));
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseGoing(User user) {
        presenter.sendResponse(user.getUserId() + "", eventId + "", Constants.RESPONSE_GOING);
    }

    @Override
    public void onResponseMaybe(User user) {
        presenter.sendResponse(user.getUserId() + "", eventId + "", Constants.RESPONSE_MAYBE);
    }

    @Override
    public void onResponseIgnore(User user) {
        presenter.sendResponse(user.getUserId() + "", eventId + "", Constants.RESPONSE_IGNORE);
    }

    @Override
    public void onResponseSuccessful(String userResponse) {
        binding.going.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.maybe.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ignore.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        switch (userResponse) {
            case Constants.RESPONSE_GOING:
                binding.going.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                break;
            case Constants.RESPONSE_MAYBE:
                binding.maybe.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                break;
            case Constants.RESPONSE_IGNORE:
                binding.ignore.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                break;
            default:
                binding.ignore.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
        }

    }

    @Override
    public void setEvent(Event event) {
        setEventData(event);
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading event...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_event:
                Intent i = new Intent(EventDetailActivity.this, EventAddActivity.class);
                i.putExtra(Constants.EVENT_ID, i);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fromMain) {
            NavUtils.navigateUpFromSameTask(this);
        } else {
            NavUtils.navigateUpTo(this, new Intent(EventDetailActivity.this, EventsActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

    }

    @NonNull
    @Override
    public EventDetailPresenter createPresenter() {
        return new EventDetailPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(event.getLocation().getLocLat(), event.getLocation().getLocLong());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(event.getLocation().getLocName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
}
