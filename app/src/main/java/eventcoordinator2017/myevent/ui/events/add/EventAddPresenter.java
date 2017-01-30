package eventcoordinator2017.myevent.ui.events.add;

import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.EventsPresenter;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPresenter extends MvpNullObjectBasePresenter<EventAddView> {

    private Realm realm;
    private User user;
    private RealmResults<Package> packageRealmResults;
    private static final String TAG = EventAddPresenter.class.getSimpleName();
    private String query;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        packageRealmResults = realm.where(Package.class).
                findAllSorted("packageId", Sort.ASCENDING);
        packageRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Package>>() {
            @Override
            public void onChange(RealmResults<Package> element) {
                filterList();
            }
        });

        loadPackageList();
    }

    public void onStop() {
        if (packageRealmResults.isValid()) {
            packageRealmResults.removeChangeListeners();
        }
        realm.close();
    }

    private void packages(Call<List<Package>> packageListCall) {
        getView().startLoading();
        packageListCall.enqueue(new Callback<List<Package>>() {
            @Override
            public void onResponse(Call<List<Package>> call, final Response<List<Package>> response) {
                getView().stopLoading();
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(response.body());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        realm.close();
                        Log.e(TAG, "onError: Unable to save USER", error);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Package>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    void loadPackageList() {
        packages(App.getInstance().getApiInterface().getPackages(""));
    }

    public void toBudget(final String eventName, final String eventDescription, String[] tags, final String eventLocation,
                         final String fromDate, final String fromTime, final String toDate, final String toTime, final String eventLat, final String eventLng) {
        final String joined = TextUtils.join("", tags).trim();
        if (eventName.equals("") || eventDescription.equals("") || eventLocation.equals("") || fromDate.equals("") || fromTime.equals("") ||
                toDate.equals("") || toTime.equals("") || joined.equals("")) {
            getView().showAlert("Fill up all fields");
        } else if (eventLat.equals("") || eventLng.equals("")) {
            getView().showAlert("Please select valid location");
        } else {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Event event = realm.createObject(Event.class);
            event.setEventName(eventName);
            event.setEventDescription(eventDescription);
            event.setEventTags(joined);
            event.setLocName(eventLocation);
            event.setLocLat(eventLat);
            event.setLocLong(eventLng);
            event.setEventDateFrom(fromDate + " " + fromTime);
            event.setEventDateTo(toDate + " " + toTime);
            realm.commitTransaction();
            realm.close();
            getView().onNext();
        }

    }

    void setQuery(String query) {
        this.query = query;
        filterList();
    }

    private void filterList() {
        if (packageRealmResults.isLoaded() && packageRealmResults.isValid()) {
            List<Package> packageList;
            if (query != null && !query.isEmpty()) {
                packageList = realm.copyFromRealm(packageRealmResults.where()
                        .lessThan("packagePriceInt",Integer.parseInt(query))
                        .findAll());
            } else {
                packageList = realm.copyFromRealm(packageRealmResults);
            }
            getView().setPackages(packageList);
        }
    }
}
