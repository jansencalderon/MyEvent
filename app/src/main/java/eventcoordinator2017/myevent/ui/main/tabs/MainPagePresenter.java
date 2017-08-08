package eventcoordinator2017.myevent.ui.main.tabs;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Date;
import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.utils.DateTimeUtils;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Sen on 2/28/2017.
 */

public class MainPagePresenter extends MvpNullObjectBasePresenter<MainPageView> {
    private Realm realm;
    private RealmResults<Event> eventRealmResults;
    private String type;

    public void onStart(String type) {
        realm = Realm.getDefaultInstance();
        this.type = type;
        eventRealmResults = realm.where(Event.class).findAll();
        eventRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                filterList();
            }
        });
    }

    private void filterList() {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            List<Event> eventList;
            if (type.equals("Today")) {
                eventList = realm.copyFromRealm(eventRealmResults.where()
                        .between("eventDateFrom", DateTimeUtils.getDateToday(),DateTimeUtils.getDateTodayEnd())
                        .findAll());
               // getView().showAlert(DateTimeUtils.dateTodayToast());
            } else {
                eventList = realm.copyFromRealm(eventRealmResults.where()
                        .greaterThan("eventDateFrom",DateTimeUtils.getDateTodayEnd()).findAll());
            }
            getView().setEvents(eventList);
        }

    }

    public void onStop() {
        if (eventRealmResults.isValid()) {
            eventRealmResults.removeChangeListeners();
        }
        realm.close();
    }

}
