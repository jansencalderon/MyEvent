package eventcoordinator2017.myevent.ui.events.add;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.model.data.Package;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventAddView extends MvpView{

    void showAlert(String message);

    void askForBudget(String budget);

    void clearBudget();

    void onPhotoClicked();

    void onDateClicked(int id);

    void onAddLocation();

    void onAddPackage();

    void startLoading();

    void stopLoading();

    void setPackages(List<Package> packageList);

    void onInviteGuests();
}
