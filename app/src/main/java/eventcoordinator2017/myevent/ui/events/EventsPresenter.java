package eventcoordinator2017.myevent.ui.events;

import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.LoginResponse;
import eventcoordinator2017.myevent.utils.DateTimeUtils;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventsPresenter extends MvpNullObjectBasePresenter<EventsView> {
    private static final String TAG = EventsPresenter.class.getSimpleName();
    private RealmResults<Event> eventRealmResults;
    private String query;
    private Realm realm;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.copyFromRealm(App.getUser());

        eventRealmResults = realm.where(Event.class).equalTo("userId", user.getUserId()).findAllSorted("eventId", Sort.DESCENDING);
        eventRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                getView().setEvents(eventRealmResults);
            }
        });

        getView().setEvents(eventRealmResults);

        loadEventList();
    }

    public void onStop() {
        if (eventRealmResults.isValid()) {
            eventRealmResults.removeChangeListeners();
        }
        realm.close();
    }

    private void events(Call<List<Event>> eventListCall) {
        getView().startLoading();
        eventListCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, final Response<List<Event>> response) {
                getView().stopLoading();
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Event.class)
                                .equalTo("userId", user.getUserId())
                                .findAll().deleteAllFromRealm();
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
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    void loadEventList() {
        events(App.getInstance().getApiInterface().getUserEvents(user.getUserId()));
    }

    void setQuery(String query) {
        this.query = query;
    }


    void filterList(String item) {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            List<Event> eventList = eventRealmResults;
            if (item.equals("All")) {
                eventList = eventRealmResults;
            } else if (item.equals("Past")) {
                eventList = eventRealmResults.where().lessThan("eventDateFrom",DateTimeUtils.getDateToday()).findAll();
            }else if (item.equals("Future")){
                eventList = realm.copyFromRealm(eventRealmResults.where()
                        .greaterThan("eventDateFrom", DateTimeUtils.getDateTodayEnd()).findAll());
            }
            getView().setEvents(eventList);
        }
    }
}
