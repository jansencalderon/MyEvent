package eventcoordinator2017.myevent.ui.events.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import eventcoordinator2017.myevent.ui.events.EventsView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventDetailPresenter extends MvpNullObjectBasePresenter<EventDetailView> {

    private RealmResults<Event> eventRealmResults;
    private Realm realm;
    private User user;
    private Event event;

    public void onStart(String eventId){
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        event = realm.where(Event.class).equalTo("eventId",eventId).findFirst();
        getView().setEvent(event);
    }

    public void onStop(){
        realm.close();

    }


}
