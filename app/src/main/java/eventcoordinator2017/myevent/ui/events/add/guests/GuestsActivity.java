package eventcoordinator2017.myevent.ui.events.add.guests;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

/**
 * Created by Sen on 2/18/2017.
 */

public class GuestsActivity extends MvpActivity<GuestsView, GuestsPresenter> implements GuestsView {




    @NonNull
    @Override
    public GuestsPresenter createPresenter() {
        return new GuestsPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
