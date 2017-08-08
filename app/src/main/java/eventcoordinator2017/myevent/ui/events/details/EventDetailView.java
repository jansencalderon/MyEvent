package eventcoordinator2017.myevent.ui.events.details;

import com.hannesdorfmann.mosby.mvp.MvpView;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Location;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.model.response.ResultResponse;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventDetailView extends MvpView{

    void onViewGuests(Event event);

    void eventBy(User user);

    void editEvent(Event event);

    void onLocationClicked(Location location);

    void onAdd();

    void showAlert(String s);

    void onResponseGoing(User user);

    void onResponseMaybe(User user);

    void onResponseIgnore(User user);

    void onResponseSuccessful(String userResponse);

    void setEvent(Event event);

    void startLoading();

    void stopLoading();
}
