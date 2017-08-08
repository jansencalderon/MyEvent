package eventcoordinator2017.myevent.ui.main;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.utils.DateTimeUtils;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class MainPresenter extends MvpNullObjectBasePresenter<MainView> {
    private Realm realm;
    private String TAG = MainPresenter.class.getSimpleName();
    private RealmResults<Event> eventRealmResults;
    public void onStart(){
        realm = Realm.getDefaultInstance();

        eventRealmResults = realm.where(Event.class).findAllAsync();
        eventRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                getView().refreshList();
            }
        });

        getEvents();

    }

    /*private void manageList() {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            List<Event> eventUpcoming;
            List<Event> eventToday;
            eventUpcoming = realm.copyFromRealm(eventRealmResults.where().equalTo("eventDateFrom", DateTimeUtils.dateToday()).findAll());
            eventToday = realm.copyFromRealm(eventRealmResults.where().notEqualTo("eventDateFrom", DateTimeUtils.dateToday()).findAll());
            getView().setEvents(eventToday,eventUpcoming);
        }
    }*/

    void getEvents() {
        getView().startLoading();
        App.getInstance().getApiInterface().getAllEvents("").enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, final Response<List<Event>> response) {
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
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }




    public void onStop(){
        if (eventRealmResults.isValid()) {
            eventRealmResults.removeChangeListeners();
        }
        realm.close();
    }


}
