package eventcoordinator2017.myevent.ui.events.add;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityEventAddBinding;
import eventcoordinator2017.myevent.databinding.DialogBudgetBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import eventcoordinator2017.myevent.ui.main.MainActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    ActivityEventAddBinding binding;

    //manipulated strings
    private String eventLat, eventLng, eventFromDate, eventFromTime;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static String TAG = "PLACES API";
    private static final LatLngBounds BOUNDS_MANILA = new LatLngBounds(
            new LatLng(13.570972, 120.022153), new LatLng(15.138226, 121.857090));

    private Realm realm;
    private TempEvent tempEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.eventToTime.setOnClickListener(this);
        binding.eventToDate.setOnClickListener(this);
        binding.eventFromDate.setOnClickListener(this);
        binding.eventFromTime.setOnClickListener(this);

        //get API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, EventAddActivity.this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_MANILA,
                null);

        binding.eventLocation.setAdapter(mAdapter);
        binding.eventLocation.setOnItemClickListener(AutocompleteClickListener);

        tempEvent = realm.where(TempEvent.class).findFirst();
        if (tempEvent != null) {
            binding.eventDescription.setText(tempEvent.getEventDescription());
            binding.eventName.setText(tempEvent.getEventName());
            binding.eventFromDate.setText(tempEvent.getEventDateFrom());
            binding.eventFromTime.setText(tempEvent.getEventTimeFrom());
            binding.eventToDate.setText(tempEvent.getEventDateTo());
            binding.eventToTime.setText(tempEvent.getEventTimeTo());

        }


    }

    @NonNull
    @Override
    public EventAddPresenter createPresenter() {
        return new EventAddPresenter();
    }


    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPackageClicked(Package aPackage) {

    }

    @Override
    public void onPackageAvail(Package aPackage) {

    }

    @Override
    public void askForBudget(String budget) {
        final DialogBudgetBinding budgetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_budget, null, false);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(budgetBinding.getRoot());
        dialog.setCancelable(false);
        if (!budget.equals("")) {
            budgetBinding.budget.setText(budget);
            budgetBinding.budget.setSelection(budget.length());
        } else {
            budgetBinding.budget.setText("");
        }
        budgetBinding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.eventBudget.setText(budgetBinding.budget.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(budgetBinding.budget.getWindowToken(), 0);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void clearBudget() {
        askForBudget(binding.eventBudget.getText().toString());
    }

    @Override
    public void onPhotoClicked() {
        showAlert("Photo");
    }

    @Override
    public void onDateClicked(final int id) {
        if (id == binding.eventToDate.getId() || id == binding.eventFromDate.getId()) {
            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String date = dateFormatter.format(newDate.getTime());
                    if (id == binding.eventFromDate.getId()) {
                        binding.eventFromDate.setText(date);
                    } else {
                        binding.eventToDate.setText(date);
                    }

                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int milliseconds = mcurrentTime.get(Calendar.MILLISECOND);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if (id == binding.eventFromTime.getId()) {
                        binding.eventFromTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    } else {
                        binding.eventToTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

    @Override
    public void onNext() {
        startActivity(new Intent(this, EventAddPackageActivity.class));
        finish();
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void setPackages(List<Package> packageList) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.next:
                presenter.toBudget(binding.eventName.getText().toString(),
                        binding.eventDescription.getText().toString(),
                        binding.tagGroup.getTags(),
                        binding.eventLocation.getText().toString(),
                        binding.eventFromDate.getText().toString(),
                        binding.eventFromTime.getText().toString(),
                        binding.eventToDate.getText().toString(),
                        binding.eventToTime.getText().toString(),
                        eventLat, eventLng
                        ,binding.eventBudget.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next, menu);


        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        onDateClicked(id);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Event Creation");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(TempEvent.class);
                    }
                });
                realm.close();
                navigateUpTo(new Intent(EventAddActivity.this, EventsActivity.class));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener AutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);

        }
    };

    /*
    * Callback for results from a Places Geo Data API query that shows the first place result in
    * the details view on screen.
    */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            eventLat = place.getLatLng().latitude + "";
            eventLng = place.getLatLng().longitude + "";


            Log.i(TAG, "Place details received: " + place.getName());
            Log.i(TAG, " LatLng: " + place.getLatLng().latitude + " , " + place.getLatLng().longitude);

            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
