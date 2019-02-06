package eventcoordinator2017.myevent.ui.events.add.packages;


import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Case;
import io.realm.Realm;
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
    private static final String TAG = EventAddPackagePresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        loadPackageList();
    }

    public void onStop() {
        realm.removeAllChangeListeners();
        realm.close();
    }

    private void loadPackageList() {
        getView().startLoading();
        App.getInstance().getApiInterface().getPackages("").enqueue(new Callback<List<Package>>() {
            @Override
            public void onResponse(@NonNull Call<List<Package>> call, @NonNull final Response<List<Package>> response) {
                if (!response.body().isEmpty()) {
                    getView().stopLoading();
                    getView().setPackages(response.body());
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(realm1 -> {
                        realm1.delete(Package.class);
                        realm1.copyToRealmOrUpdate(response.body());
                    }, realm::close, error -> {
                        realm.close();
                        Log.e(TAG, "onError: Unable to save USER", error);
                    });
                } else {
                    realm.executeTransactionAsync(realm -> realm.delete(Package.class));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Package>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    /*

        public void updateEvent(final String eventName, final String eventDescription, final String[] tags,
                                final String fromDate, final String fromTime, final String toDate, final String toTime, final String eventBudget) {
            final String joined = TextUtils.join("", tags).trim();
            if (eventName.equals("") || eventDescription.equals("") || fromDate.equals("") || fromTime.equals("") ||
                    toDate.equals("") || toTime.equals("") || joined.equals("") || eventBudget.equals("")) {
                getView().showAlert("E all fields");
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
    public void setApplyFilter(String filterType, String filterSort, String query) {

        int budget = 0;
        boolean isString = false;
        try {
            budget = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isString = true;
        }
        RealmResults<Package> packages = realm.where(Package.class).findAll().sort("packageId", Sort.ASCENDING);

        if (isString) {
            Log.d(TAG, "String: " + query);
            if (query.equals("")) {
                packages = packages.sort("packageName", Sort.DESCENDING);
            } else {
                packages = packages.where()
                        .contains("packageName", query.trim(), Case.INSENSITIVE)
                        .or().contains("packageType", query.trim(), Case.INSENSITIVE)
                        .findAll().sort("packageName", Sort.DESCENDING);
            }
        } else {
            Log.d(TAG, "Integer");
            packages = packages.where().lessThanOrEqualTo("packagePrice", budget)
                    .findAll().sort("packagePrice", Sort.DESCENDING);
        }
/*
            if(!filterType.equals("")){
                packages = packages.where().contains("packageType", filterType).findAll();
            }

            if (filterSort.equals("Price (High to Low)")) {
                packages = packages.where()
                        .findAll().sort("packagePrice", Sort.DESCENDING);
            }
            if (filterSort.equals("Price (Low To High)")) {
                packages = packages.where()
                        .findAll().sort("packagePrice", Sort.ASCENDING);
            }*/


        getView().setPackages(packages);
    }

    public TempEvent getTempEvent() {
        return realm.where(TempEvent.class).findFirst();
    }

}
