package eventcoordinator2017.myevent.ui.register;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    public void register(String email,
                         String password,
                         String confirmPassword,
                         String firstName,
                         String lastName,
                         String birthday,
                         String contact,
                         String address) {

        if (email.equals("") || password.equals("") || confirmPassword.equals("") || firstName.equals("") || lastName.equals("") || birthday.equals("") ||
                contact.equals("") || address.equals("")) {
            getView().showAlert("Fill-up all fields");
        } else if (!password.contentEquals(confirmPassword)) {
            getView().showAlert("Password does not match");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().register(email,password, firstName, lastName, contact,birthday,address)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().onRegistrationSuccess();
                                        break;
                                    case Constants.EMAIL_EXIST:
                                        getView().showAlert("Email already exists");
                                        break;
                                    default:
                                        getView().showAlert(String.valueOf(R.string.oops));
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showAlert(errorBody);
                                } catch (IOException e) {
                                    Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showAlert(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }

    }

    public void onChecked(boolean isChecked){
        if (isChecked) {
           getView().onCheckedIsTrue();
        } else {
           getView().onCheckedIsFalse();
        }
        ;
    }

    ;


}
