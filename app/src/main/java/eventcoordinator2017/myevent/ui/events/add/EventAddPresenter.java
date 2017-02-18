package eventcoordinator2017.myevent.ui.events.add;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.app.App;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public class EventAddPresenter extends MvpNullObjectBasePresenter<EventAddView> {

    private Realm realm;
    private User user;
    private static final String TAG = EventAddPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();

        getView().askForBudget("");
    }

    public void onStop() {
        realm.close();
    }

    public void updateEvent(final String eventName,
                            final String eventDescription,
                            final String[] tags,
                            final String fromDate,
                            final String fromTime,
                            final String toDate,
                            final String toTime,
                            final String eventBudget,
                            final String type) {
        final String joined = TextUtils.join("", tags).trim();
        if (eventName.equals("") || eventDescription.equals("") || fromDate.equals("") || fromTime.equals("") ||
                toDate.equals("") || toTime.equals("") || joined.equals("") || eventBudget.equals("")) {
            getView().showAlert("Fill up all fields");
        } else {
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(TempEvent.class);
                    TempEvent tempEvent = new TempEvent();
                    tempEvent.setEventId(1);
                    tempEvent.setEventName(eventName);
                    tempEvent.setEventDescription(eventDescription);
                    tempEvent.setEventTags(joined);
                    tempEvent.setEventDateFrom(fromDate);
                    tempEvent.setEventDateTo(toDate);
                    tempEvent.setEventTimeFrom(fromTime);
                    tempEvent.setEventTimeTo(toTime);
                    tempEvent.setBudget(eventBudget);
                    realm.copyToRealmOrUpdate(tempEvent);

                }
            });
            realm.close();
            if (type.equals("loc")) {
                getView().onAddLocation();
            } else if (type.equals("pack")) {
                getView().onAddPackage();
            }
        }

    }


}
