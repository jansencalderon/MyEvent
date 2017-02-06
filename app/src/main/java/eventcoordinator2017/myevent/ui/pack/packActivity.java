package eventcoordinator2017.myevent.ui.pack;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ActivityPackBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.model.data.User;
import io.realm.Realm;

/**
 * Created by Sen on 2/6/2017.
 */

public class PackActivity extends MvpActivity<PackView,PackPresenter> implements PackView{
    ActivityPackBinding binding;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pack);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int id = i.getIntExtra(Constants.ID, -1);
        if(id != -1){
            Package aPackage = realm.where(Package.class).equalTo(Constants.PACKAGE_ID,id).findFirst();
            binding.setAPackage(aPackage);
        }

    }


    @NonNull
    @Override
    public PackPresenter createPresenter() {
        return null;
    }
}
