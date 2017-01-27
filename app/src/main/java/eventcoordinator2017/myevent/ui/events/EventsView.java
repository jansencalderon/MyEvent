package eventcoordinator2017.myevent.ui.events;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventsView extends MvpView{

    void onNewViewStateInstance();

    void showAlert(String s);

    void stopLoading();

    void startLoading();

    void setEvents(List<Event> eventList);

    void onRefresh();

    void onEventClicked(Event event);
}
