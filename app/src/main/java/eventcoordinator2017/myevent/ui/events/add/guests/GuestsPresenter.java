package eventcoordinator2017.myevent.ui.events.add.guests;

import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsPresenter extends MvpNullObjectBasePresenter<GuestsView> {
    private Realm realm;
    private List<User> users = new ArrayList<>();
    private String TAG = GuestsPresenter.class.getSimpleName();
    private Event event;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        event = realm.where(Event.class).findFirst();
        if (event != null) {
            users = realm.where(TempEvent.class).findFirst().getGuests();
            if (users.size() > 0) {
                getView().refreshList(users);
            }
        }
    }

    public void getGuests(String event_id) {
        getView().startLoading();
        App.getInstance().getApiInterface().getGuests(event_id).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        users = response.body();
                        getView().refreshList(users);
                    } else {
                        getView().showAlert("No Guests Yet");
                    }
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    void onAddGuest(String query, String event_id) {
        App.getInstance().getApiInterface().inviteGuest(query, event_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (event != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                update(event, realm);
                            }
                        });
                    }
                    users.add(response.body());
                    getView().showAlert(response.body().getFullName() + " added");
                    getView().refreshList(users);

                } else {
                    getView().showAlert("Failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    public static void update(Event event, Realm realm) {
        //do update stuff
        User user = realm.copyToRealmOrUpdate(new User());
        event.getGuests().add(user);

    }


    public void onStop() {
        realm.close();
    }
}
