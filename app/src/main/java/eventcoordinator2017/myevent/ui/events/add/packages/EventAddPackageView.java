package eventcoordinator2017.myevent.ui.events.add.packages;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import eventcoordinator2017.myevent.model.data.Package;

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

public interface EventAddPackageView extends MvpView {

    void showAlert(String message);

    void onPackageClicked(Package aPackage);

    void filter();

    void clearFilter();

    void onPackageAvail(Package aPackage);

    void startLoading();

    void stopLoading();

    void checkResult(int count);

    void setPackages(List<Package> packageList);
}
