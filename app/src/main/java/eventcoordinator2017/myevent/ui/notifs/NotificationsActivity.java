package eventcoordinator2017.myevent.ui.notifs;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityNotificationBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.ui.events.details.EventDetailActivity;

public class NotificationsActivity extends MvpActivity<NotificationsView, NotificationsPresenter> implements NotificationsView {

    ActivityNotificationBinding binding;
    NotificationListAdapter notificationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        notificationListAdapter = new NotificationListAdapter(getMvpView());
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadEventList();
        });

        binding.recyclerView.setAdapter(notificationListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @NonNull
    @Override
    public NotificationsPresenter createPresenter() {
        return new NotificationsPresenter();
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
        notificationListAdapter.setEvents(eventList);
        notificationListAdapter.notifyDataSetChanged();
        checkResult(eventList.size());
    }

    public void checkResult(int count) {
        binding.noResult.resultText.setText("You have notifs yet");
        binding.noResult.resultImage.setImageResource(R.drawable.ic_calendar);
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(Constants.ID, event.getEventId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
