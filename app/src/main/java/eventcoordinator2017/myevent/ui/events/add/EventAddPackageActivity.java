package eventcoordinator2017.myevent.ui.events.add;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityEventAddBudgetBinding;
import eventcoordinator2017.myevent.databinding.DialogBudgetBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.ui.eventsPackage.PackagesListAdapter;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPackageActivity extends MvpActivity<EventAddView, EventAddPresenter> implements EventAddView {

    ActivityEventAddBudgetBinding binding;
    PackagesListAdapter packagesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_add_package);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        packagesListAdapter = new PackagesListAdapter(getMvpView());
        presenter.onStart();

        binding.recyclerView.setAdapter(packagesListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.eventBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setQuery(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setQuery(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // presenter.setQuery(editable.toString());

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
    public void onPackageClicked(Package aPackage) {
        showAlert(aPackage.getPackageName());
    }

    @Override
    public void askForBudget(String budget) {
        final DialogBudgetBinding budgetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_budget, null, false);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(budgetBinding.getRoot());
        dialog.setCancelable(false);
        if(!budget.equals("")){
            budgetBinding.budget.setText(budget);
            budgetBinding.budget.setSelection(budget.length());
        }else{
            budgetBinding.budget.setText("");
        }
        budgetBinding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.eventBudget.setText(budgetBinding.budget.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void clearBudget(){
        askForBudget(binding.eventBudget.getText().toString());
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
