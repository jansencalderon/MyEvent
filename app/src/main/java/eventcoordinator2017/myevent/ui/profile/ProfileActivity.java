package eventcoordinator2017.myevent.ui.profile;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityProfileBinding;
import eventcoordinator2017.myevent.databinding.NoResultBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.EventsListAdapter;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public class ProfileActivity extends MvpActivity<ProfileView, ProfilePresenter> implements ProfileView {

    private ProgressDialog progressDialog;
    private ActivityProfileBinding binding;
    private Realm realm;
    private EventsListAdapter eventListAdapter;
    private NoResultBinding noResultBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setView(getMvpView());

        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.setUser(realm.where(User.class).findFirst());

        presenter.onStart();

        binding.recyclerView.setAdapter(eventListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }


    /***
     * End of MvpViewStateActivity
     ***/


    /***
     * Start of ProfileView
     ***/
    @Override
    public void onEdit() {
        Toast.makeText(this, "Edit Me", Toast.LENGTH_SHORT).show();
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
        if(eventList.isEmpty()){
            binding.recyclerView.setVisibility(View.GONE);
        }else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            eventListAdapter.setEvents(eventList);
        }
    }

    /***
     * End of ProfileView
     ***/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                onEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
