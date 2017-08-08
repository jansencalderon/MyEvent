package eventcoordinator2017.myevent.ui.events.add.venue;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Location;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventAddLocationView extends MvpView{

    void clearFilter();

    void showAlert(String message);

    void filter();

    void onNext();

    void startLoading();

    void checkResult(int count);

    void stopLoading();

    void onCardClicked(Location location);

    void setList(List<Location> list);
}
