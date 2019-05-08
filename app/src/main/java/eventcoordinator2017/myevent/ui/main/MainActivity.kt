package eventcoordinator2017.myevent.ui.main

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.hannesdorfmann.mosby.mvp.MvpActivity

import java.util.ArrayList

import de.hdodenhof.circleimageview.CircleImageView
import eventcoordinator2017.myevent.R
import eventcoordinator2017.myevent.app.App
import eventcoordinator2017.myevent.app.Constants
import eventcoordinator2017.myevent.databinding.ActivityMainBinding
import eventcoordinator2017.myevent.model.data.Event
import eventcoordinator2017.myevent.model.data.User
import eventcoordinator2017.myevent.model.response.ResultResponse
import eventcoordinator2017.myevent.ui.events.EventsActivity
import eventcoordinator2017.myevent.ui.login.LoginActivity
import eventcoordinator2017.myevent.ui.notifs.NotificationsActivity
import eventcoordinator2017.myevent.ui.profile.ProfileActivity
import eventcoordinator2017.myevent.utils.SharedPreferencesUtil
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var client: GoogleApiClient
    private lateinit var progressDialog: ProgressDialog
    private var strings = ArrayList<String>()
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = mvpView
        presenter.onStart()
        setSupportActionBar(binding.toolbar)


        val drawer = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)


        //display data
        binding.navigationView.getHeaderView(0).findViewById<View>(R.id.viewProfile).setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        binding.navigationView.menu.getItem(0).isChecked = true

        strings.add("Today")
        strings.add("Upcoming")
        binding.viewpager.apply {
            adapter = MainPageFragmentAdapter(supportFragmentManager, this.context, strings)
            offscreenPageLimit = strings.size
        }
        binding.slidingTabs.setupWithViewPager(binding.viewpager)
        binding.swipeRefreshLayout.setOnRefreshListener { presenter.getEvents() }


        //sendTokenToServer();
    }

    override fun onResume() {
        super.onResume()
        val realm = Realm.getDefaultInstance()
        val user = realm.where(User::class.java).findFirst()
        if (user != null)
            displayUserData(user)
        realm.close()
    }

    /*private fun sendTokenToServer() {
        val user = App.getUser()
        val sharedPreferencesUtil = SharedPreferencesUtil(this, Constants.FIREBASE)
        val sent = sharedPreferencesUtil.getBooleanValue(Constants.FIREBASE + "_sent", false)
        val token = sharedPreferencesUtil.getStringValue(Constants.FIREBASE + "_token", "")
        if (!sent) {
            if (token != "") {
                App.getInstance().apiInterface.saveUserToken(user.userId.toString() + "", token).enqueue(object : Callback<ResultResponse> {
                    override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                        if (response.isSuccessful) {
                            if (response.body() == Constants.SUCCESS) {
                                sharedPreferencesUtil.putBooleanValue(Constants.FIREBASE + "_sent", true)
                            } else {
                                Log.e(TAG, "Token Not Updated")
                            }
                        } else {
                            showAlert("Website is sleeping")
                        }
                    }

                    override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                        Log.d(TAG, t.toString())
                    }
                })
            } else {
                Log.e(TAG, "No Token Yet")
            }
        } else {
            Log.e(TAG, "Token is already sent")
        }
    }*/


    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }


    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun displayUserData(user: User) {
        // TextView email = (TextView) binding.navigationView.getHeaderView(0).findViewById(R.id.email);
        val name = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.name)
        val circleImageView = binding.navigationView.getHeaderView(0).findViewById<CircleImageView>(R.id.userImage)
        // email.setText(user.getEmail());
        name.text = user.fullName
        Glide.with(this)
                .load(Constants.URL_IMAGE + user.image)
                .error(R.drawable.ic_gallery)
                .into(circleImageView)
    }

    override fun showAlert(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun setEvents(eventToday: List<Event>, eventUpcoming: List<Event>) {
        binding.viewpager.adapter = MainPageFragmentAdapter(supportFragmentManager, this, strings)
    }

    override fun refreshList() {

    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (!searchView.isIconified) {
                searchView.isIconified = true
                return
            }
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.home -> {

            }
            R.id.events -> {
                startActivity(Intent(this, EventsActivity::class.java))
                binding.navigationView.menu.getItem(0).isChecked = true
            }
            R.id.notification -> {
                startActivity(Intent(this, NotificationsActivity::class.java))
                binding.navigationView.menu.getItem(0).isChecked = true
            }
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Log Out")
                builder.setMessage("Are you sure?")
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do nothing but close the dialog
                    val realm = Realm.getDefaultInstance()
                    realm.executeTransactionAsync({ realm1 -> realm1.deleteAll() }, {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        this@MainActivity.finish()
                    }, { error ->
                        realm.close()
                        Log.e(TAG, "onError: Error Logging out (deleting all data)", error)
                    })
                    finish()
                }
                builder.setNegativeButton("NO") { dialog, which ->
                    // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()

            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    public override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


}
