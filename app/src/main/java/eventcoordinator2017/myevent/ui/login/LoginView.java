package eventcoordinator2017.myevent.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/3/2016.
 */

public interface LoginView extends MvpView {

    void onLoginButtonClicked();

    void onRegisterButtonClicked();

    void showAlert(String message);

    void setEditTextValue(String username, String password);

    void startLoading();

    void stopLoading();

    void onLoginSuccess();


    void onForgotPasswordButtonClicked();
}
