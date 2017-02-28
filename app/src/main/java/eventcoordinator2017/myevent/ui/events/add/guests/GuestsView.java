package eventcoordinator2017.myevent.ui.events.add.guests;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.User;

/**
 * Created by Sen on 2/18/2017.
 */

public interface GuestsView extends MvpView {


    void onAdd();

    void showAlert(String s);

    void startLoading();

    void stopLoading();

    void refreshList(List<User> users);
}
