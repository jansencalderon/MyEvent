package eventcoordinator2017.myevent.ui.events.add;


import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPresenter extends MvpNullObjectBasePresenter<EventAddView> {

    private Realm realm;
    private User user;
    private static final String TAG = EventAddPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
    }

    public void onStop() {
        realm.close();
    }

    public void updateEvent(final String eventName,
                            final String eventDescription,
                            final String[] tags,
                            final String fromDate,
                            final String fromTime,
                            final String toDate,
                            final String toTime,
                            final String eventBudget,
                            final String type) {
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
            if (type.equals("loc")) {
                getView().onAddLocation();
            } else if (type.equals("pack")) {
                getView().onAddPackage();
            }
        }

    }


    void createEvent(final File eventImage, String eventName) {
        final TempEvent tempEvent = realm.where(TempEvent.class).findFirst();
        if (tempEvent == null) {
            getView().showAlert("Fill up fields");
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    tempEvent.setImageUri(eventImage.getPath());
                }
            });
            if (tempEvent.getLocationId() == 0) {
                getView().showAlert("You must choose location for the event");
            } else if (tempEvent.getPackageId() == 0) {
                getView().showAlert("You must choose package for the event");
            } else {
                // create RequestBody instance from file
                getView().startLoading();
                final String eventImageName = eventName.replaceAll("\\s+", "");
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), eventImage);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", eventImage.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
                App.getInstance().uploadImage().uploadImage(body, name).enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResult().equals(Constants.SUCCESS)) {
                                createEventStep2(eventImageName);
                            } else {
                                getView().showAlert("Uploading Image Failed");
                            }
                        } else {
                            getView().startLoading();
                            getView().showAlert(response.message() != null ? response.message()
                                    : "Unknown Error");

                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: Error calling login api", t);
                        getView().stopLoading();
                        getView().showAlert("Error Connecting to Server");
                    }
                });
            }
        }

    }

    private void createEventStep2(String eventImageName) {
        TempEvent tempEvent = realm.where(TempEvent.class).findFirst();
        App.getInstance().getApiInterface().createEvent(tempEvent.getUserId() + ""
                , tempEvent.getaPackage().getPackageId() + ""
                , tempEvent.getEventName()
                , tempEvent.getEventDateFrom() + " " + tempEvent.getEventTimeFrom()
                , tempEvent.getEventDateTo() + " " + tempEvent.getEventTimeTo()
                , tempEvent.getEventDescription()
                , tempEvent.getEventTags()
                , tempEvent.getLocation().getLocId() + ""
                , eventImageName)
                .enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            if (response.body().getResult().equals(Constants.SUCCESS)) {
                                getView().onInviteGuests();
                            } else {
                                getView().showAlert("Uploading Image Failed");
                            }
                        } else {
                            getView().showAlert(response.message() != null ? response.message()
                                    : "Unknown Error");

                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: Error calling login api", t);
                        getView().stopLoading();
                        getView().showAlert("Error Connecting to Server");
                    }
                });
    }
}
