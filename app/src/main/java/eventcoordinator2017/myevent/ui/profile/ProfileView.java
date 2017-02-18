package eventcoordinator2017.myevent.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public interface ProfileView extends MvpView{


    void showAlert(String message);

    void onEdit();

    void startLoading();

    void stopLoading();

    void setEvents(List<Event> eventList);
}
