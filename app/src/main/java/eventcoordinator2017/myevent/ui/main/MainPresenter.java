package eventcoordinator2017.myevent.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class MainPresenter extends MvpNullObjectBasePresenter<MainView> {

    public void onStart(){

    }

    void displayUserInfo() {
        final Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null)
            getView().displayUserData(user);
        realm.close();
    }

    public void onStop(){

    }


}
