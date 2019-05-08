package eventcoordinator2017.myevent.ui.main.tabs;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.FragmentMainPageBinding;
import eventcoordinator2017.myevent.model.data.Event;
import eventcoordinator2017.myevent.ui.events.details.EventDetailActivity;
import eventcoordinator2017.myevent.utils.StringUtils;

/**
 * Created by Sen on 2/28/2017.
 */

public class MainPageFragment extends MvpFragment<MainPageView, MainPagePresenter> implements MainPageView {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_TYPE = "ARG_TYPE";
    private static final String TAG = MainPageFragment.class.getSimpleName();
    private List<Event> eventList;
    private int mPage;
    private String type;
    private FragmentMainPageBinding binding;
    private MainListAdapter adapter;

    public static MainPageFragment newInstance(int page, String s) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TYPE, s);
        MainPageFragment mainPageFragment = new MainPageFragment();
        mainPageFragment.setArguments(args);
        return mainPageFragment;
    }

    @NonNull
    @Override
    public MainPagePresenter createPresenter() {
        return new MainPagePresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        type = getArguments().getString(ARG_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_page, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MainListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart(type);
    }

    @Override
    public void setEvents(List<Event> events) {
        adapter.setEvents(events);
        checkResult(events.size());
    }


    @Override
    public void internet(Boolean status) {
        if (status) {
            binding.noInternet.noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            binding.noInternet.noInternetLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkResult(int count) {
        if (type.equals("Today")) {
            binding.noResult.resultText.setText("No Events for Today\nSee Upcoming");
        } else {
            binding.noResult.resultText.setText("No Upcoming Events");
        }
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(Constants.ID, event.getEventId());
        intent.putExtra("fromMain",true);
        startActivity(intent);
        Log.d(TAG,event.getEventTags());
        Log.d(TAG, StringUtils.putHashtags(event.getEventTags()));
        List<String> myList = new ArrayList<>(Arrays.asList(event.getEventTags().split(",")));
        Log.d(TAG, myList.toString());
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}
