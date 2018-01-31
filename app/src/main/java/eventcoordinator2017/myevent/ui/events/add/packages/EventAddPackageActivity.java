package eventcoordinator2017.myevent.ui.events.add.packages;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityEventAddPackageBinding;
import eventcoordinator2017.myevent.databinding.DialogFilterPackagesBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.pack.PackActivity;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPackageActivity extends MvpActivity<EventAddPackageView, EventAddPackagePresenter> implements EventAddPackageView {

    ActivityEventAddPackageBinding binding;
    PackagesListAdapter packagesListAdapter;
    private Realm realm;
    private TempEvent tempEvent;
    private String filterType ="", filterSort ="";

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
           // binding.eventBudget.setText(tempEvent.getBudget());
        }
    }


    @NonNull
    @Override
    public EventAddPackagePresenter createPresenter() {
        return new EventAddPackagePresenter();
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
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void filter() {
        final Dialog dialogFilter = new Dialog(this);
        final DialogFilterPackagesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_filter_packages, null, false);
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setCancelable(false);
        dialogFilter.setContentView(binding.getRoot());
        if (!filterType.equals(""))
            binding.filterType.setText(filterType);


        binding.filterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EventAddPackageActivity.this)
                        .title("Package Types")
                        .items("Birthday", "Debut", "Wedding", "Party")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                binding.filterType.setText(text);
                                filterType = binding.filterType.getText().toString();
                            }
                        })
                        .show();
            }
        });

        binding.filterSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EventAddPackageActivity.this)
                        .title("Package Types")
                        .items("Name", "Price (High to Low)", "Price (Low to High)")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                binding.filterSort.setText(text);
                                filterSort = binding.filterSort.getText().toString();
                            }
                        })
                        .show();
            }
        });

        binding.filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setApplyFilter(filterType, filterSort, tempEvent.getBudget());
                dialogFilter.dismiss();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilter.dismiss();
            }
        });

        dialogFilter.show();


    }
    @Override
    public void clearFilter() {
        filterSort = "";
        filterType = "";
        presenter.setApplyFilter(filterType, filterSort,tempEvent.getBudget());

    }

    @Override
    public void onPackageAvail(Package aPackage) {
        showAlert(aPackage.getPackageName());
    }


    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }
    @Override
    public void checkResult(int count) {
        binding.noResult.resultText.setText("No Result for Filters\nTry others");
        binding.noResult.resultImage.setImageResource(R.drawable.ic_no);
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPackages(List<Package> packageList) {
        packagesListAdapter.setPackages(packageList);
        checkResult(packageList.size());
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
