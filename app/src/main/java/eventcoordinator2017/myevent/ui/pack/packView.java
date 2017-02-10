package eventcoordinator2017.myevent.ui.pack;

import com.hannesdorfmann.mosby.mvp.MvpView;

import eventcoordinator2017.myevent.model.data.Package;

/**
 * Created by Sen on 2/6/2017.
 */

public interface PackView extends MvpView {

    void onAvail(Package aPackage);
}
