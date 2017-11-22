package eventcoordinator2017.myevent.ui.events.add.venue;


import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
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

public class EventAddLocationPresenter extends MvpNullObjectBasePresenter<EventAddLocationView> {

    private Realm realm;
    private User user;
    private RealmResults<Location> locationRealmResults;
    private static final String TAG = EventAddLocationPresenter.class.getSimpleName();
    private String query;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        locationRealmResults = realm.where(Location.class).
                findAllSortedAsync("locId", Sort.ASCENDING);
        locationRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Location>>() {
            @Override
            public void onChange(RealmResults<Location> element) {
                filterList();
            }
        });

        loadList();
    }

    public void onStop() {
        if (locationRealmResults.isValid()) {
            locationRealmResults.removeAllChangeListeners();
        }
        realm.close();
    }

    private void locations(Call<List<Location>> listCall) {
        getView().startLoading();
        listCall.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, final Response<List<Location>> response) {
                if (!response.body().isEmpty()) {
                    getView().stopLoading();
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Location.class);
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
                            realm.delete(Location.class);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    void loadList() {
        locations(App.getInstance().getApiInterface().getLocations(""));
    }

  /*  public void updateEvent(final String eventName, final String eventDescription, final String[] tags,
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

    }*/

    void setQuery(String query) {
        this.query = query;
        filterList();
    }

    void setApplyFilter(String capacity, String venueType, String setup) {
        if (locationRealmResults.isLoaded() && locationRealmResults.isValid()) {
            List<Location> list;
            if(capacity.equals("")){
                capacity = "0";
            }
            RealmResults<Location> locations = locationRealmResults.where()
                    .greaterThanOrEqualTo("locCapacity", Integer.parseInt(capacity))
                    .contains("locVenueType", venueType)
                    .contains("locSetup", setup)
                    .findAll();
            list = realm.copyFromRealm(locations);
            getView().setList(list);

        }
    }

    private void filterList() {
        if (locationRealmResults.isLoaded() && locationRealmResults.isValid()) {
            List<Location> list;
            if (query != null && !query.isEmpty()) {
                RealmResults<Location> locations = locationRealmResults.where().findAll();
                list = realm.copyFromRealm(locations);
            } else {
                list = realm.copyFromRealm(locationRealmResults);
            }

            getView().setList(list);
            getView().checkResult(list.size());

        }
    }
}
