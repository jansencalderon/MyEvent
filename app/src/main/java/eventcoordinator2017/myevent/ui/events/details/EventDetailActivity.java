package eventcoordinator2017.myevent.ui.events.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventDetailBinding;
import eventcoordinator2017.myevent.databinding.ActivityEventsBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventDetailActivity extends MvpActivity<EventDetailView, EventDetailPresenter> implements EventDetailView {

    ActivityEventDetailBinding binding;
    private Event event;
    private User user;
    private Realm realm;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.onStart();
        eventId = getIntent().getIntExtra(Constants.EVENT_ID, -1);

        if (eventId != -1) {
            showAlert("No event data");
            finish();
        }

        user = App.getUser();
        event = realm.copyFromRealm(realm.where(Event.class).equalTo(Constants.EVENT_ID, eventId).findFirst());
        binding.setEvent(event);
        binding.setUser(user);

        if (user.getUserId() == event.getUserId()) {
            binding.eventResponsePanel.setVisibility(View.GONE);

        }
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
        binding.going.setBackgroundColor(ContextCompat.getColor(this, R.color.lightestGray));
        binding.maybe.setBackgroundColor(ContextCompat.getColor(this, R.color.lightestGray));
        binding.ignore.setBackgroundColor(ContextCompat.getColor(this, R.color.lightestGray));

        switch (userResponse) {
            case Constants.RESPONSE_GOING:
                binding.going.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDark));
                break;
            case Constants.RESPONSE_MAYBE:
                binding.maybe.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDark));
                break;
            case Constants.RESPONSE_IGNORE:
                binding.ignore.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDark));
                break;
        }

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


    @NonNull
    @Override
    public EventDetailPresenter createPresenter() {
        return new EventDetailPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
