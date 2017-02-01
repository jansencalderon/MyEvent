package eventcoordinator2017.myevent.ui.events.crud;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityEventsCrudBinding;
import eventcoordinator2017.myevent.ui.events.crud.fragments.BudgetFragment;
import eventcoordinator2017.myevent.ui.events.crud.fragments.InfoFragment;

/**
 * Created by Mark Jansen Calderon on 2/1/2017.
 */

public class EventsCrudActivity extends MvpActivity<EventsCrudView, EventsCrudPresenter> implements EventsCrudView{

    ActivityEventsCrudBinding binding;
    private FragmentTransaction transaction;
    private Fragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_events_crud);
        binding.setView(getMvpView());


        fr = new InfoFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fr);
        transaction.commit();

        binding.basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Fragment fr = new InfoFragment();
                if(fr.isAdded()){
                    transaction.replace(R.id.container,fr);
                    transaction.commit();
                }else transaction.show(fr);
            }
        });

        binding.budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment fr = new BudgetFragment();
                if(fr.isAdded()){
                    transaction.replace(R.id.container,fr);
                    transaction.commit();
                }else transaction.show(fr);
            }
        });

    }


    @Override
    public void onNext(){

    }

    @NonNull
    @Override
    public EventsCrudPresenter createPresenter() {
        return new EventsCrudPresenter();
    }
}
