package eventcoordinator2017.myevent.ui.events.add.packages;


import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.add.EventAddView;
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

public class EventAddPackagePresenter extends MvpNullObjectBasePresenter<EventAddPackageView> {

    private Realm realm;
    private User user;
    private RealmResults<Package> packageRealmResults;
    private static final String TAG = EventAddPackagePresenter.class.getSimpleName();
    private String query;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        packageRealmResults = realm.where(Package.class).
                findAllSortedAsync("packageId", Sort.ASCENDING);
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
            packageRealmResults.removeAllChangeListeners();
        }
        realm.close();
    }

    private void packages(Call<List<Package>> packageListCall) {
        getView().startLoading();
        packageListCall.enqueue(new Callback<List<Package>>() {
            @Override
            public void onResponse(Call<List<Package>> call, final Response<List<Package>> response) {
                if (!response.body().isEmpty()) {
                    getView().stopLoading();
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Package.class);
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
                } else {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Package.class);
                        }
                    });
                }
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
/*

    public void updateEvent(final String eventName, final String eventDescription, final String[] tags,
                            final String fromDate, final String fromTime, final String toDate, final String toTime, final String eventBudget) {
        final String joined = TextUtils.join("", tags).trim();
        if (eventName.equals("") || eventDescription.equals("") || fromDate.equals("") || fromTime.equals("") ||
                toDate.equals("") || toTime.equals("") || joined.equals("") || eventBudget.equals("")) {
            getView().showAlert("Fill up all fields");
        } else {
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(TempEvent.class);
                    TempEvent tempEvent = new TempEvent();
                    tempEvent.setEventId(1);
                    tempEvent.setEventName(eventName);
                    tempEvent.setEventDescription(eventDescription);
                    tempEvent.setEventTags(joined);
                    tempEvent.setEventDateFrom(fromDate);
                    tempEvent.setEventDateTo(toDate);
                    tempEvent.setEventTimeFrom(fromTime);
                    tempEvent.setEventTimeTo(toTime);
                    tempEvent.setBudget(eventBudget);
                    realm.copyToRealmOrUpdate(tempEvent);

                }
            });
            realm.close();
            getView().onNext();
        }

    }
*/

    void setQuery(String query) {
        this.query = query;
        filterList();
    }

    private void filterList() {
        if (packageRealmResults.isLoaded() && packageRealmResults.isValid()) {
            List<Package> packageList;
            if (query != null && !query.isEmpty()) {
                RealmResults<Package> packages = packageRealmResults.where()
                        .lessThanOrEqualTo("packagePrice", Integer.parseInt(query))
                        .findAll();

                packageList = realm.copyFromRealm(packages);

            } else {
                packageList = realm.copyFromRealm(packageRealmResults);
            }

            getView().setPackages(packageList);

        }
    }

    public void setApplyFilter(String filterType, String filterSort, String budget) {
        if (packageRealmResults.isLoaded() && packageRealmResults.isValid()) {
            List<Package> list;
            RealmResults<Package> packages = packageRealmResults.where()
                    .lessThanOrEqualTo("packagePrice", Integer.parseInt(budget))
                    .contains("packageType", filterType)
                    .findAllSorted("packageName", Sort.DESCENDING);

            if (filterSort.equals("Price (High to Low)")) {
                packages = packageRealmResults.where()
                        .lessThanOrEqualTo("packagePrice", Integer.parseInt(budget))
                        .contains("packageType", filterType)
                        .findAllSorted("packagePrice", Sort.DESCENDING);
            }

            if (filterSort.equals("Price (Low To High)")) {
                packages = packageRealmResults.where()
                        .lessThanOrEqualTo("packagePrice", Integer.parseInt(budget))
                        .contains("packageType", filterType)
                        .findAllSorted("packagePrice", Sort.ASCENDING);
            }


            list = realm.copyFromRealm(packages);
            getView().setPackages(list);

        }
    }
}
