package eventcoordinator2017.myevent.ui.events.add.guests;

import android.util.Log;
import android.util.MalformedJsonException;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Guest;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsPresenter extends MvpNullObjectBasePresenter<GuestsView> {
    private Realm realm;
    private List<Guest> guest = new ArrayList<>();
    private String TAG = GuestsPresenter.class.getSimpleName();
    private RealmResults<Event> eventRealmResults;
    private Event event;

    public void onStart(int id) {
        realm = Realm.getDefaultInstance();

        event = realm.where(Event.class).equalTo(Constants.EVENT_ID, id).findFirst();
        getView().refreshList(event.getGuests());
        event.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                getView().refreshList(event.getGuests());
            }
        });


    }

    public void getGuests(String event_id) {
        getView().startLoading();
        App.getInstance().getApiInterface().getGuests(event_id).enqueue(new Callback<List<Guest>>() {
            @Override
            public void onResponse(Call<List<Guest>> call, Response<List<Guest>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        guest = response.body();
                        getView().refreshList(guest);
                    } else {
                        getView().showAlert("No Guest Yet");
                    }
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }

            @Override
            public void onFailure(Call<List<Guest>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    public void onAddGuest(String query, String event_id) {
        query = query.trim();
        Guest guest = event.getGuests().where().equalTo("email", query).findFirst();
        //check if user is exising
        if (guest == null && (query.equals(App.getUser().getEmail()))) {
            getView().showAlert(App.getUser().getFullName() + " is already invited");
        } else if (query.equals("")) {
            getView().showAlert("Please input email");
        } else if (guest != null) {
            getView().showAlert("You can't invite yourself");
        } else {
            getView().startLoading();
            final String finalQuery = query;
            App.getInstance().getApiInterface().inviteGuest(query, event_id).enqueue(new Callback<Guest>() {
                @Override
                public void onResponse(Call<Guest> call, final Response<Guest> response) {
                    getView().stopLoading();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (event != null) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        update(event, realm, response.body());
                                        getView().showAlert(response.body().getFullName() + " added");
                                        getView().clearEmail();
                                    }
                                });
                            } else {
                                getView().showAlert("Event is null");
                            }
                        } else {
                            getView().showAlert("Failed Adding");
                        }
                    } else {
                        getView().showAlert("Failed Response");
                    }
                }

                @Override
                public void onFailure(Call<Guest> call, Throwable t) {
                    Log.e(TAG, "onFailure: Error calling login api", t);
                    getView().stopLoading();
                    if (t instanceof MalformedJsonException) {
                        getView().showAlert(finalQuery + " does not exists on our database");
                    } else {
                        getView().showAlert("Error Connecting to Server");
                    }
                }
            });
        }

    }

    public static void update(Event event, Realm realm, Guest guest) {
        //do update stuff
        Guest update = realm.copyToRealmOrUpdate(guest);
        event.getGuests().add(update);

    }


    public void onStop() {
        realm.close();
    }
}
