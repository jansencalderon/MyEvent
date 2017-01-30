package eventcoordinator2017.myevent.ui.events.add;

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
import eventcoordinator2017.myevent.databinding.ActivityEventAddBudgetBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.ui.events.add.packages.PackagesListAdapter;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddBudgetActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView {

    ActivityEventAddBudgetBinding binding;
    private PackagesListAdapter packagesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add_budget);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        packagesListAdapter = new PackagesListAdapter(getMvpView());
        presenter.onStart();

        binding.recyclerView.setAdapter(packagesListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setQuery(binding.eventBudget.getText().toString());
            }
        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);


        return true;
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoClicked() {

    }

    @Override
    public void onDateClicked(int id) {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void setPackages(List<Package> packageList) {
        packagesListAdapter.setPackages(packageList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
