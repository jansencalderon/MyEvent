package eventcoordinator2017.myevent.ui.notifs;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;

public interface NotificationsView extends MvpView {

    void showAlert(String s);

    void stopLoading();

    void startLoading();

    void setEvents(List<Event> eventList);

    void onRefresh();

    void onEventClicked(Event event);
}
