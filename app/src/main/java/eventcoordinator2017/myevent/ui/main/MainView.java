package eventcoordinator2017.myevent.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface MainView extends MvpView {

    void stopLoading();

    void startLoading();

    void displayUserData(User user);

    void showAlert(String s);

    void setEvents(List<Event> eventToday, List<Event> eventUpcoming);

    void refreshList();
}
