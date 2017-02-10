package eventcoordinator2017.myevent.ui.events.add;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventAddPackageBinding;
import eventcoordinator2017.myevent.databinding.DialogBudgetBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.pack.PackActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPackageActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView {

    ActivityEventAddPackageBinding binding;
    PackagesListAdapter packagesListAdapter;
    private Realm realm;
    private TempEvent tempEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add_package);
        binding.setView(getMvpView());
        realm = Realm.getDefaultInstance();


        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        packagesListAdapter = new PackagesListAdapter(getMvpView());
        presenter.onStart();

        binding.recyclerView.setAdapter(packagesListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tempEvent = realm.where(TempEvent.class).findFirst();
        if (tempEvent != null) {
            presenter.setQuery(tempEvent.getBudget());
        }
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
    public void onBackPressed() {
        startActivity(new Intent(this, EventAddActivity.class));
        finish();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPackageClicked(Package aPackage) {
        Intent i = new Intent(this, PackActivity.class);
        i.putExtra(Constants.ID, aPackage.getPackageId());
        startActivity(i);
        finish();
    }

    @Override
    public void onPackageAvail(Package aPackage) {
        showAlert(aPackage.getPackageName());
    }

    @Override
    public void askForBudget(String budget) {

    }

    @Override
    public void clearBudget() {

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
