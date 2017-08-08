package eventcoordinator2017.myevent.ui.events.add.guests;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityAddGuestsBinding;
import eventcoordinator2017.myevent.databinding.DialogProfileViewBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Guest;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import io.realm.Realm;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsActivity extends MvpActivity<GuestsView, GuestsPresenter> implements GuestsView {

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
}
