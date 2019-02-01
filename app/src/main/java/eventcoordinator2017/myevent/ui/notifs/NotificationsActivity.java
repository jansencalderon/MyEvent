package eventcoordinator2017.myevent.ui.notifs;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityNotificationBinding;
import eventcoordinator2017.myevent.model.data.Event;

public class NotificationsActivity extends MvpActivity<NotificationsView, NotificationsPresenter> implements NotificationsView {

    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void setEvents(List<Event> eventList) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onEventClicked(Event event) {

    }
}
