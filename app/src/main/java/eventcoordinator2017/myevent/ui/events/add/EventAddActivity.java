package eventcoordinator2017.myevent.ui.events.add;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityEventAddBinding;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView{

    ActivityEventAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @NonNull
    @Override
    public EventAddPresenter createPresenter() {
        return new EventAddPresenter();
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
