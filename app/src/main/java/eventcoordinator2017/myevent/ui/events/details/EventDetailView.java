package eventcoordinator2017.myevent.ui.events.details;

import com.hannesdorfmann.mosby.mvp.MvpView;

import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventDetailView extends MvpView{

    void setEvent(Event event);
}
