package eventcoordinator2017.myevent.ui.main.tabs;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Sen on 2/28/2017.
 */

public interface MainPageView extends MvpView {
    void setEvents(List<Event> events);

    void internet(Boolean status);

    void checkResult(int count);

    void onEventClicked(Event event);

    void showAlert(String s);
}
