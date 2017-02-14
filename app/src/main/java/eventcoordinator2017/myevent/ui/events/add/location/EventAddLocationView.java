package eventcoordinator2017.myevent.ui.events.add.location;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Package;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventAddLocationView extends MvpView{

    void showAlert(String message);

    void onNext();

    void startLoading();

    void stopLoading();
}
