package eventcoordinator2017.myevent.ui.events.add.packages;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import eventcoordinator2017.myevent.ui.events.add.EventAddActivity;
import eventcoordinator2017.myevent.ui.pack.PackActivity;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPackageActivity extends MvpActivity<EventAddPackageView, EventAddPackagePresenter> implements EventAddPackageView {

    ActivityEventAddPackageBinding binding;
    PackagesListAdapter packagesListAdapter;
    //  private TempEvent tempEvent;
    private String filterType = "", filterSort = "";
    private SearchView searchView;
    String query;

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


       /* tempEvent = presenter.getTempEvent();
        if (tempEvent != null) {
            presenter.setQuery(tempEvent.getBudget());
            binding.eventBudget.setText(tempEvent.getBudget());
        }*/
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Budget, Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.setApplyFilter(filterType, filterSort, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                presenter.setApplyFilter(filterType, filterSort, query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
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
                        .itemsCallback((dialog, view, which, text) -> {
                            binding.filterSort.setText(text);
                            filterSort = binding.filterSort.getText().toString();
                        })
                        .show();
            }
        });

        binding.filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setApplyFilter(filterType, filterSort, query);
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
        presenter.setApplyFilter(filterType, filterSort, query);

    }

    @Override
    public void onPackageAvail(Package aPackage) {
        showAlert(aPackage.getPackageName());
    }


    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void checkResult(int count) {
        Log.d("Item Count", count+"");
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
    protected void onDestroy() {
        super.onDestroy();
        if(binding.swipeRefreshLayout.isRefreshing()){
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        presenter.onStop();
    }
}
