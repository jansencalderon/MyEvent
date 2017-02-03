package eventcoordinator2017.myevent.ui.eventsPackage.detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.model.data.Package;
import io.realm.Realm;
public class PackageCategoryListFragment
        extends MvpFragment<PackageCategoryListView, PackageCategoryListPresenter>
        implements PackageCategoryListView {

    private static final String ARG_TOPIC_ID = "arg-topic-id";
    private static final String ARG_LESSON_ID = "arg-lesson-id";
    private static final String ARG_IS_LAST = "arg-is-last";
    private static final String ARG_QUERY = "arg-query";
    private static final String ARG_FIRST_INST = "arg-first-inst";
    private static final String TAG = PackageCategoryListFragment.class.getSimpleName();

    private int topicId;
    private int packageId;
    private boolean isLastPage;
    private String query;

    private Realm realm;
    private FragmentLessonBinding binding;
    private TextToSpeech textToSpeech;
    private int firstLessonId;
    private LinearLayoutManager layoutManager;


    public static PackageCategoryListFragment newInstance(int packageId, int categoryId) {
        PackageCategoryListFragment fragment = new PackageCategoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOPIC_ID, topicId);
        args.putInt(ARG_LESSON_ID, lessonId);
        args.putBoolean(ARG_IS_LAST, isLastPage);
        args.putString(ARG_QUERY, query);
        args.putInt(ARG_FIRST_INST, firstLessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public PackageCategoryListPresenter createPresenter() {
        return new PackageCategoryListPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retrieving parameters/bundle/arguments
        if (getArguments() != null) {
            topicId = getArguments().getInt(ARG_TOPIC_ID, -1);
            categoryId = getArguments().getInt(ARG_LESSON_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // init data
        realm = Realm.getDefaultInstance();
        Package aPackage = realm.where(Package.class).equalTo(Constants.ID, packag).findFirst();
        PackageCategoryListView eventDetailListView = new Ev(lesson, getMvpView(), isLastPage, query);
        binding.recyclerView.setAdapter(lessonDetailListAdapter);
        List<LessonDetail> lessonDetails = realm.copyFromRealm(lesson.getLessondetails().sort(LessonDetail.COL_SEQ));
        lessonDetailListAdapter.setLessonDetails(lessonDetails);
        textToSpeech = new TextToSpeech(getContext(), this);
        Log.d(TAG, "onStart: " + firstLessonId);
        for (int i = 0; i < lessonDetails.size(); i++) {
            if (lessonDetails.get(i).getId() == firstLessonId) {
                //logd
                //layoutManager.scrollToPosition(i + 1);
                //binding.recyclerView.getLayoutManager().scrollToPosition(i + 1);
                return;
            }
        }
    }



}