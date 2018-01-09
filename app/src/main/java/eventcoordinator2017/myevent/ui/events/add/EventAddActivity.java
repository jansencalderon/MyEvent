package eventcoordinator2017.myevent.ui.events.add;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventAddBinding;
import eventcoordinator2017.myevent.databinding.DialogBudgetBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import eventcoordinator2017.myevent.ui.events.add.packages.EventAddPackageActivity;
import eventcoordinator2017.myevent.ui.events.add.venue.EventAddLocationActivity;
import eventcoordinator2017.myevent.ui.events.details.EventDetailActivity;
import eventcoordinator2017.myevent.utils.PermissionsActivity;
import eventcoordinator2017.myevent.utils.PermissionsChecker;
import eventcoordinator2017.myevent.utils.StringUtils;
import io.realm.Realm;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView, View.OnClickListener {

    ActivityEventAddBinding binding;

    public static final int PICK_IMAGE = 100;
    private int PICK_IMAGE_REQUEST = 1;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;
    //manipulated strings
    private String eventLat, eventLng, eventFromDate, eventFromTime;

    private Realm realm;
    private TempEvent tempEvent;
    private String TAG = EventAddActivity.class.getSimpleName();
    private File eventImage;
    private ProgressDialog progressDialog;
    private String realBudget = "";
    private String eventUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();
        presenter.onStart();



        /**
         * Permission Checker Initialized
         */
        checker = new PermissionsChecker(this);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.eventToTime.setOnClickListener(this);
        binding.eventToDate.setOnClickListener(this);
        binding.eventFromDate.setOnClickListener(this);
        binding.eventFromTime.setOnClickListener(this);

        int eventId = getIntent().getIntExtra((Constants.EVENT_ID), -1);
        if (eventId != -1) {
            Event event = realm.where(Event.class).equalTo(Constants.EVENT_ID, eventId).findFirst();
            binding.eventName.setText(event.getEventName());
        } else {
            tempEvent = realm.where(TempEvent.class).findFirst();
        }
        if (tempEvent != null) {
            Glide.with(this)
                    .load(new File(tempEvent.getImageUri()))
                    .error(R.drawable.ic_gallery)
                    .centerCrop()
                    .into(binding.eventImage);
            eventImage = new File(tempEvent.getImageUri());

            binding.eventDescription.setText(tempEvent.getEventDescription());
            binding.eventName.setText(tempEvent.getEventName());
            binding.eventFromDate.setText(tempEvent.getEventDateFrom());
            binding.eventFromTime.setText(tempEvent.getEventTimeFrom());
            binding.eventToDate.setText(tempEvent.getEventDateTo());
            binding.eventToTime.setText(tempEvent.getEventTimeTo());
            binding.eventBudget.setText(StringUtils.moneyFormat(Integer.parseInt(tempEvent.getBudget())));
            realBudget = tempEvent.getBudget();
            binding.tagGroup.setTags(tempEvent.getEventTags().split(","));
            if (tempEvent.getaPackage() != null) {
                binding.packageCard.setVisibility(View.VISIBLE);
                binding.addPackage.setVisibility(View.GONE);
                Package aPackage = tempEvent.getaPackage();
                binding.setAPackage(aPackage);
                Glide.with(this)
                        .load(Constants.URL_IMAGE + tempEvent.getaPackage().getImageDirectory())
                        .into(binding.packageImage);
                binding.removePackage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                tempEvent.setPackageId(0);
                                tempEvent.setaPackage(null);
                            }
                        });
                        binding.addPackage.setVisibility(View.VISIBLE);
                        binding.packageCard.setVisibility(View.GONE);
                    }
                });
            } else {
                binding.addPackage.setVisibility(View.VISIBLE);
                binding.packageCard.setVisibility(View.GONE);
            }

            if (tempEvent.getLocation() != null) {
                Location location = tempEvent.getLocation();
                binding.setLocation(location);
                binding.addLocation.setVisibility(View.GONE);
                Glide.with(this)
                        .load(Constants.URL_IMAGE + tempEvent.getLocation().getLocImage())
                        .into(binding.locImage);
                binding.removeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                tempEvent.setLocationId(0);
                                tempEvent.setLocation(null);
                            }
                        });
                        binding.addLocation.setVisibility(View.VISIBLE);
                        binding.locationCard.setVisibility(View.GONE);
                    }
                });
            } else {
                binding.addLocation.setVisibility(View.VISIBLE);
                binding.locationCard.setVisibility(View.GONE);
            }


        } else {
            binding.locationCard.setVisibility(View.GONE);
            binding.packageCard.setVisibility(View.GONE);
        }

        binding.addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempEvent != null && (tempEvent.getaPackage() != null || tempEvent.getLocation() != null)) {
                    onAddPackage();
                } else {
                    presenter.updateEvent(
                            binding.eventName.getText().toString(),
                            binding.eventDescription.getText().toString(),
                            binding.tagGroup.getTags(),
                            binding.eventFromDate.getText().toString(),
                            binding.eventFromTime.getText().toString(),
                            binding.eventToDate.getText().toString(),
                            binding.eventToTime.getText().toString(),
                            realBudget,
                            eventUri,
                            "pack");
                    Log.d(TAG, "Create new tempEvent and pick Pack");
                }
            }

        });

        binding.addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempEvent != null && (tempEvent.getLocation() != null || tempEvent.getaPackage() != null)) {
                    onAddLocation();
                } else {
                    presenter.updateEvent(
                            binding.eventName.getText().toString(),
                            binding.eventDescription.getText().toString(),
                            binding.tagGroup.getTags(),
                            binding.eventFromDate.getText().toString(),
                            binding.eventFromTime.getText().toString(),
                            binding.eventToDate.getText().toString(),
                            binding.eventToTime.getText().toString(),
                            realBudget,
                            eventUri,
                            "loc");
                    Log.d(TAG, "Create new tempEvent and pick Loc");
                }
            }
        });


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
    public void askForBudget(final String budget) {
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
                if(budgetBinding.budget.getText().toString().equals("")){
                    showAlert("Please input budget");
                }else {
                    realBudget = budgetBinding.budget.getText().toString();
                    binding.eventBudget.setText(StringUtils.moneyFormat(Integer.parseInt(budgetBinding.budget.getText().toString())));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(budgetBinding.budget.getWindowToken(), 0);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void clearBudget() {
        askForBudget(realBudget);
    }

    @Override
    public void onPhotoClicked() {
        EasyImage.openGallery(this, 0);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                eventImage = imageFile;
                eventUri = eventImage.getPath();
                Glide.with(EventAddActivity.this)
                        .load(imageFile)
                        .centerCrop()
                        .error(R.drawable.ic_gallery)
                        .into(binding.eventImage);
            }


        });
    }

    @Override
    public void onAddLocation() {
        startActivity(new Intent(this, EventAddLocationActivity.class));
        finish();
    }


    @Override
    public void onAddPackage() {
        startActivity(new Intent(this, EventAddPackageActivity.class));
        finish();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Creating event");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setPackages(List<Package> packageList) {

    }

    @Override
    public void onInviteGuests(int i) {
        startActivity(new Intent(this, EventDetailActivity.class).putExtra(Constants.ID, i));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.create:
                if (tempEvent != null) {
                    presenter.createEvent(eventImage, binding.eventName.getText().toString());
                } else {
                    showAlert("Fill up fields");
                }
                Log.d(TAG, "Create new event");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_add, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        presenter.onStop();
    }


}
