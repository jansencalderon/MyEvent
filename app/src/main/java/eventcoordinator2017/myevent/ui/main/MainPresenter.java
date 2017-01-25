package eventcoordinator2017.myevent.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public class MainPresenter extends MvpNullObjectBasePresenter<MainView> {
    public void displayUserInfo() {
        final Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null)
            getView().displayUserData(user);
        realm.close();
    }
}
