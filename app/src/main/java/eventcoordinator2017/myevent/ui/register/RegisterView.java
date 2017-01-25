package eventcoordinator2017.myevent.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface RegisterView extends MvpView {

    void onSubmit();

    void showAlert(String message);

    void setEditTextValue(String email, String password, String confirmPassword, String firstName, String lastName, String birthday, String contact, String address, String city, String zipCode, String country);

    void startLoading();

    void stopLoading();

    void onRegistrationSuccess();

    void onChecked();

    void onCheckedIsTrue();

    void onCheckedIsFalse();

    void onBirthdayClicked();
}
