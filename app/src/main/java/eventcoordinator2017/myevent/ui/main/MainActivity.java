package eventcoordinator2017.myevent.ui.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityMainBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import eventcoordinator2017.myevent.ui.events.EventsActivity;
import eventcoordinator2017.myevent.ui.login.LoginActivity;
import eventcoordinator2017.myevent.ui.notifs.NotificationsActivity;
import eventcoordinator2017.myevent.ui.profile.ProfileActivity;
import eventcoordinator2017.myevent.utils.SharedPreferencesUtil;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ProgressDialog progressDialog;
    private List<String> strings = new ArrayList<>();
    private SearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(getMvpView());
        presenter.onStart();
        setSupportActionBar(binding.toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);


        //display data
        binding.navigationView.getHeaderView(0).findViewById(R.id.viewProfile).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        binding.navigationView.getMenu().getItem(0).setChecked(true);

        strings.add("Today");
        strings.add("Upcoming");
        binding.viewpager.setAdapter(new MainPageFragmentAdapter(getSupportFragmentManager(), this, strings));
        binding.viewpager.setOffscreenPageLimit(strings.size());
        binding.slidingTabs.setupWithViewPager(binding.viewpager);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getEvents();
            }
        });


        sendTokenToServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null)
            displayUserData(user);
        realm.close();
    }

    private void sendTokenToServer() {
        User user = App.getUser();
        final SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this, Constants.FIREBASE);
        Boolean sent = sharedPreferencesUtil.getBooleanValue(Constants.FIREBASE + "_sent", false);
        String token = sharedPreferencesUtil.getStringValue(Constants.FIREBASE + "_token", "");
        if (!sent) {
            if (!token.equals("")) {
                App.getInstance().getApiInterface().saveUserToken(user.getUserId() + "", token).enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().equals(Constants.SUCCESS)) {
                                sharedPreferencesUtil.putBooleanValue(Constants.FIREBASE + "_sent", true);
                            } else {
                                Log.e(TAG, "Token Not Updated");
                            }
                        } else {
                            showAlert("Website is sleeping");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        Log.d(TAG, t.toString());
                    }
                });
            } else {
                Log.e(TAG, "No Token Yet");
            }
        } else {
            Log.e(TAG, "Token is already sent");
        }
    }


    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }


    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void displayUserData(User user) {
        // TextView email = (TextView) binding.navigationView.getHeaderView(0).findViewById(R.id.email);
        TextView name = binding.navigationView.getHeaderView(0).findViewById(R.id.name);
        CircleImageView circleImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.userImage);
        // email.setText(user.getEmail());
        name.setText(user.getFullName());
        Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .error(R.drawable.ic_gallery)
                .into(circleImageView);
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEvents(List<Event> eventToday, List<Event> eventUpcoming) {
        binding.viewpager.setAdapter(new MainPageFragmentAdapter(getSupportFragmentManager(), this, strings));
    }

    @Override
    public void refreshList() {

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
                return;
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Budget, Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // presenter.setApplyFilter(filterType, filterSort, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //presenter.setApplyFilter(filterType, filterSort, query);
                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.events) {
            startActivity(new Intent(this, EventsActivity.class));
            binding.navigationView.getMenu().getItem(0).setChecked(true);
        } else if (id == R.id.notification) {
            startActivity(new Intent(this, NotificationsActivity.class));
            binding.navigationView.getMenu().getItem(0).setChecked(true);
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", (dialog, which) -> {
                // Do nothing but close the dialog
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(realm1 -> realm1.deleteAll(), () -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    MainActivity.this.finish();
                }, error -> {
                    realm.close();
                    Log.e(TAG, "onError: Error Logging out (deleting all data)", error);
                });
                finish();
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }


}
