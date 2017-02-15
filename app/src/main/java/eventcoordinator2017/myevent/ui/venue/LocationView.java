package eventcoordinator2017.myevent.ui.venue;

import com.hannesdorfmann.mosby.mvp.MvpView;

import eventcoordinator2017.myevent.model.data.Location;

/**
 * Created by Mark Jansen Calderon on 2/15/2017.
 */

public interface LocationView extends MvpView {
    void onAvail(Location location);
}
