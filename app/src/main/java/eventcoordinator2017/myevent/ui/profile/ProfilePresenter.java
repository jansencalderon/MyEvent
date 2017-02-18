package eventcoordinator2017.myevent.ui.profile;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.utils.DateTimeUtils;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class ProfilePresenter extends MvpNullObjectBasePresenter<ProfileView> {

    private static final String TAG = ProfilePresenter.class.getSimpleName();
    private Realm realm;
    private User user;
    private RealmResults<Event> eventRealmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
        loadEventList();

        eventRealmResults = realm.where(Event.class).findAllSorted("eventId", Sort.ASCENDING);
        eventRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
                    List<Event> eventList;
                    eventList = realm.copyFromRealm(eventRealmResults.where()
                            .equalTo("userId", user.getUserId())
                            .equalTo("eventDateFrom", DateTimeUtils.dateToday())
                            .findAll());
                    getView().setEvents(eventList);
                }
            }
        });
    }

    void loadEventList() {
        events(App.getInstance().getApiInterface().getUserEvents(user.getUserId()));
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

    public void getEvents() {

    }

    public void onStop() {
        realm.close();
    }
}
