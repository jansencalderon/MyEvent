package eventcoordinator2017.myevent.ui.events.details;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import eventcoordinator2017.myevent.ui.events.EventsView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventDetailPresenter extends MvpNullObjectBasePresenter<EventDetailView> {

    private RealmResults<Event> eventRealmResults;
    private Realm realm;
    private User user;
    private Event event;
    private String TAG = EventDetailPresenter.class.getSimpleName();

    public void onStart(){
        realm = Realm.getDefaultInstance();

    }

    public void sendResponse(String userId, String eventId, final String userResponse){
        App.getInstance().getApiInterface().eventResponse(userId,eventId,userResponse).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResult().equals(Constants.SUCCESS)){
                        getView().onResponseSuccessful(userResponse);
                    }else getView().showAlert("Failed");
                }else
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    public void onStop(){
        realm.close();

    }


    public void getEventData(String event_id) {
        getView().startLoading();
        App.getInstance().getApiInterface().getSingleEvent(event_id).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                getView().stopLoading();
                if(response.isSuccessful()){
                    if(response.body().getEventId()!=null){
                        getView().setEvent(response.body());
                    }else getView().showAlert("Failed");
                }else
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                getView().stopLoading();
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().showAlert("Error Connecting to Server");
            }
        });
    }
}
