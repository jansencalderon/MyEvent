package eventcoordinator2017.myevent.ui.events.add.guests;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import eventcoordinator2017.myevent.model.data.TempEvent;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsPresenter extends MvpNullObjectBasePresenter<GuestsView> {
    private Realm realm;
    private User user;
    private TempEvent tempEvent;
    public void onStart(){
        realm = Realm.getDefaultInstance();

    }
    public void onStop() {
        realm.close();
    }
}
