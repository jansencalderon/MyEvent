package eventcoordinator2017.myevent.ui.events.add.guests;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityAddGuestsBinding;
import eventcoordinator2017.myevent.databinding.DialogInviteSmsBinding;
import eventcoordinator2017.myevent.databinding.DialogProfileViewBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Guest;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import io.realm.Realm;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsActivity extends MvpActivity<GuestsView, GuestsPresenter> implements GuestsView,AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    // Initialize variables

    AutoCompleteTextView textView=null;
    private ArrayAdapter<String> adapter;

    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();

    EditText toNumber=null;
    String toNumberValue="";


    ActivityAddGuestsBinding binding;
    private Realm realm;
    private GuestsListAdapter guestsListAdapter;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_guests);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();

        int eventId = getIntent().getIntExtra(Constants.ID, -1);
        event = realm.where(Event.class).equalTo(Constants.EVENT_ID, eventId).findFirst();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        guestsListAdapter = new GuestsListAdapter(getMvpView());
        binding.recyclerView.setAdapter(guestsListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));



        presenter.onStart(event.getEventId());

        binding.inviteSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSmsDialog();
            }
        });
    }

    @Override
    public void onClick(Guest guest) {
        DialogProfileViewBinding dialogProfileViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_profile_view, null, false);
        dialogProfileViewBinding.setGuest(guest);
        Glide.with(this)
                .load(Constants.URL_IMAGE + guest.getImage())
                .error(R.drawable.ic_mood)
                .into(dialogProfileViewBinding.guestImage);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogProfileViewBinding.getRoot());
        dialog.show();
    }

    public void showSmsDialog() {

        final DialogInviteSmsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_invite_sms, null, false);

        textView = binding.query;
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(this);
        textView.setOnItemClickListener(this);

        readContactData();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(binding.getRoot());
        dialog.show();
    }

    private View.OnClickListener BtnAction(final AutoCompleteTextView toNumber) {
        return new View.OnClickListener() {

            public void onClick(View v) {

                String NameSel = "";
                NameSel = toNumber.getText().toString();


                final String ToNumber = toNumberValue;


                if (ToNumber.length() == 0 ) {
                    Toast.makeText(getBaseContext(), "Please fill phone number",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), NameSel+" : "+toNumberValue,
                            Toast.LENGTH_LONG).show();
                }

            }
        };
    }



    @Override
    public void onAdd() {
        presenter.onAddGuest(binding.query.getText().toString(), event.getEventId() + "");
    }

    @Override
    public void clearEmail() {
        binding.query.setText("");
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void refreshList(List<Guest> guests) {
        guestsListAdapter.setList(guests);
        checkResult(guests.size());
    }

    public void checkResult(int count) {
        binding.noResult.resultText.setText("No Invites Yet");
        binding.noResult.resultImage.setImageResource(R.drawable.ic_no_guest);
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(TempEvent.class);
                    }
                });
                realm.close();
                navigateUpTo(new Intent(this, EventsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, EventAddActivity.class).putExtra(Constants.FROM_INVITE_GUESTS, true));
        finish();
    }

    @NonNull
    @Override
    public GuestsPresenter createPresenter() {
        return new GuestsPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest, menu);

        return true;
    }




    // Read phone contact name and phone numbers

    private void readContactData() {

        try {

            /*********** Reading Contacts Name And Number **********/

            String phoneNumber = "";
            ContentResolver cr = getBaseContext()
                    .getContentResolver();

            //Query to get contact name

            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            // If data data found in contacts
            if (cur.getCount() > 0) {

                Log.i("AutocompleteContacts", "Reading   contacts........");

                int k=0;
                String name = "";

                while (cur.moveToNext())
                {

                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {

                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?",
                                        new String[] { id },
                                        null);
                        int j=0;

                        while (pCur
                                .moveToNext())
                        {
                            // Sometimes get multiple data
                            if(j==0)
                            {
                                // Get Phone number
                                phoneNumber =""+pCur.getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                // Add contacts names to adapter
                                adapter.add(name);

                                // Add ArrayList names to adapter
                                phoneValueArr.add(phoneNumber.toString());
                                nameValueArr.add(name.toString());

                                j++;
                                k++;
                            }
                        }  // End while loop
                        pCur.close();
                    } // End if

                }  // End while loop

            } // End Cursor value check
            cur.close();


        } catch (Exception e) {
            Log.i("AutocompleteContacts","Exception : "+ e);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        //Log.d("AutocompleteContacts", "onItemSelected() position " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        // Get Array index value for selected name
        int i = nameValueArr.indexOf(""+arg0.getItemAtPosition(arg2));

        // If name exist in name ArrayList
        if (i >= 0) {

            // Get Phone Number
            toNumberValue = phoneValueArr.get(i);

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            // Show Alert
            Toast.makeText(getBaseContext(),
                    "Position:"+arg2+" Name:"+arg0.getItemAtPosition(arg2)+" Number:"+toNumberValue,
                    Toast.LENGTH_LONG).show();

            Log.d("AutocompleteContacts",
                    "Position:"+arg2+" Name:"+arg0.getItemAtPosition(arg2)+" Number:"+toNumberValue);

        }

    }
}
