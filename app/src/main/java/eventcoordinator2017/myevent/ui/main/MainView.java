package eventcoordinator2017.myevent.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

import eventcoordinator2017.myevent.model.data.User;


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface MainView extends MvpView {

    void displayUserData(User user);
}
