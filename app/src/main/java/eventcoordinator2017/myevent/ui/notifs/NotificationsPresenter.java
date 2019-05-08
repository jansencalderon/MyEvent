package eventcoordinator2017.myevent.ui.notifs;

import androidx.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class NotificationsPresenter extends MvpNullObjectBasePresenter<NotificationsView> {
    private static final String TAG = NotificationsPresenter.class.getSimpleName();
    //private Realm realm;
    private User user;

    public void onStart() {
       // realm = Realm.getDefaultInstance();
        //user = realm.copyFromRealm(App.getUser());

        //eventRealmResults = realm.where(Event.class).equalTo("userId", user.getUserId()).findAll().sort("eventId", Sort.DESCENDING);

       // getView().setEvents(eventRealmResults);

        loadEventList();
    }

    public void onStop() {
       // realm.close();
    }

    private void events(Call<List<Event>> eventListCall) {
        getView().startLoading();
        eventListCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull final Response<List<Event>> response) {
                getView().stopLoading();
                getView().setEvents(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    void loadEventList() {
        events(App.getInstance().getApiInterface().getAllEventsInvited(App.getUser().getUserId()));
    }

}
