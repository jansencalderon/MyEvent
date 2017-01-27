package eventcoordinator2017.myevent.ui.events.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventDetailBinding;
import eventcoordinator2017.myevent.databinding.ActivityEventsBinding;
import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventDetailActivity extends MvpActivity<EventDetailView, EventDetailPresenter> implements EventDetailView{

    ActivityEventDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        presenter.onStart(i.getStringExtra(Constants.EVENT_ID));
    }

    @NonNull
    @Override
    public EventDetailPresenter createPresenter() {
        return new EventDetailPresenter();
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
    public void setEvent(Event event){
        binding.setEvent(event);
    }


}
