package eventcoordinator2017.myevent.ui.events;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventsBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.events.details.EventDetailActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventsActivity extends MvpActivity<EventsView, EventsPresenter> implements EventsView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener{

    ActivityEventsBinding binding;
    private User user;
    private EventsListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_events);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        eventListAdapter = new EventsListAdapter(getMvpView());
        presenter.onStart();

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        binding.recyclerView.setAdapter(eventListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner dropdown = binding.spinner;
        dropdown.setOnItemSelectedListener(this);
        String[] items = new String[]{
                "All",
                "Past",
                "Future"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    @Override
    public void onNewViewStateInstance() {
        binding.swipeRefreshLayout.post(() -> {
            binding.swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        });
    }

    @NonNull
    @Override
    public EventsPresenter createPresenter() {
        return new EventsPresenter();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_event:
                final Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.delete(TempEvent.class);
                realm.commitTransaction();
                realm.close();

                Intent i = new Intent(EventsActivity.this, EventAddActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setEvents(List<Event> eventList) {
        eventListAdapter.setEvents(eventList);
        checkResult(eventList.size());
    }


    public void checkResult(int count) {
        binding.noResult.resultText.setText("You have no hosted events yet");
        binding.noResult.resultImage.setImageResource(R.drawable.ic_calendar);
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadEventList();
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(Constants.ID, event.getEventId());
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);


        return true;
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.filterList(item);

        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
