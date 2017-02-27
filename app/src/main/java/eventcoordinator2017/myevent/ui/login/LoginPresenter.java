package eventcoordinator2017.myevent.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.LoginResponse;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    public void login(String email, final String password) {
        if (email.isEmpty() || email.equals("")) {
            getView().showAlert("Please enter email");
        } else if (password.isEmpty() || password.equals("")) {
            getView().showAlert("Please enter Password");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().login(email, password)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                try {
                                    switch (response.body().getResult()) {
                                        case Constants.SUCCESS:
                                            final Realm realm = Realm.getDefaultInstance();
                                            realm.executeTransactionAsync(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    User user = response.body().getUser();
                                                    realm.copyToRealmOrUpdate(user);
                                                }
                                            }, new Realm.Transaction.OnSuccess() {
                                                @Override
                                                public void onSuccess() {
                                                    realm.close();
                                                    getView().onLoginSuccess();
                                                }
                                            }, new Realm.Transaction.OnError() {
                                                @Override
                                                public void onError(Throwable error) {
                                                    realm.close();
                                                    Log.e(TAG, "onError: Unable to save USER", error);
                                                    getView().showAlert("Error Saving API Response");
                                                }
                                            });
                                            break;
                                        case Constants.NOT_EXIST:
                                            getView().showAlert("Email does not exist");
                                        case Constants.WRONG_PASSWORD:
                                            getView().showAlert("Wrong Password");
                                            break;
                                        default:
                                            getView().showAlert(String.valueOf(R.string.oops));
                                            break;

                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    getView().showAlert("Oops");
                                }
                            } else {
                                getView().showAlert(response.message() != null ? response.message()
                                        : "Unknown Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling login api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }
}
