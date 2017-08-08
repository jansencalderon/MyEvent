package eventcoordinator2017.myevent.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import eventcoordinator2017.myevent.utils.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sen on 2/27/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        User user = App.getUser();
        final SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this, Constants.FIREBASE);
        sharedPreferencesUtil.putStringValue(Constants.FIREBASE + "_token", token);
        Log.e(TAG, "Saved token to shared prefs: " + sharedPreferencesUtil.getStringValue(Constants.FIREBASE + "_token", null));
        if (user != null) {
            App.getInstance().getApiInterface().saveUserToken(user.getUserId() + "", token).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                    if (response.body().equals(Constants.SUCCESS)) {
                        sharedPreferencesUtil.putBooleanValue(Constants.FIREBASE + "_sent", true);
                    } else {
                        Log.e(TAG, "Token Not Updated");
                    }
                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });
        }

    }
}

