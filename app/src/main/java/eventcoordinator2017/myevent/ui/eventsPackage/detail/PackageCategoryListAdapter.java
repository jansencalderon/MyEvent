package eventcoordinator2017.myevent.ui.eventsPackage.detail;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailImageBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailTextBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonHeaderBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonQuizButtonBinding;
import com.tip.capstone.mlearning.helper.ImageHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.model.data.Package;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Adapter for Lesson Details
 */

class PackageCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_TEXT = 1;
    private static final int VIEW_IMAGE = 2;
    private static final int VIEW_QUIZ = 3;
    private static final String TAG = PackageCategoryListAdapter.class.getSimpleName();

    private final Package aPackage;
    private final List<Package> lessonDetails;
    private final PackageCategoryListView view;
    private boolean isLastPage;
    private String query;


    PackageCategoryListAdapter(Package aPackage, PackageCategoryListView view, boolean isLastPage) {
        this.aPackage = aPackage;
        this.view = view;
        this.isLastPage = isLastPage;
        lessonDetails = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && lesson != null) {
            return VIEW_HEADER;
        } else if (isLastPage && position == (getItemCount() - 1)) {
            return VIEW_QUIZ;
        } else {
            // decrement index for list if has lesson
            int index = lesson != null ? position - 1 : position;
            switch (lessonDetails.get(index).getBody_type()) {
                case Constant.DETAIL_TYPE_TEXT:
                    return VIEW_TEXT;
                case Constant.DETAIL_TYPE_IMAGE:
                    return VIEW_IMAGE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                ItemLessonHeaderBinding itemLessonHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_header,
                        parent,
                        false);
                return new LessonHeaderViewHolder(itemLessonHeaderBinding);
            case VIEW_TEXT:
                ItemLessonDetailTextBinding itemLessonDetailTextBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_text,
                        parent,
                        false);
                return new LessonDetailTextViewHolder(itemLessonDetailTextBinding);
            case VIEW_IMAGE:
                ItemLessonDetailImageBinding itemLessonDetailImageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_image,
                        parent,
                        false);
                return new LessonDetailImageViewHolder(itemLessonDetailImageBinding);
            case VIEW_QUIZ:
                ItemLessonQuizButtonBinding itemLessonQuizButtonBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_quiz_button,
                        parent,
                        false);
                return new LessonQuizButtonHolder(itemLessonQuizButtonBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HEADER:
                LessonHeaderViewHolder lessonHeaderViewHolder = (LessonHeaderViewHolder) holder;
                lessonHeaderViewHolder.itemLessonHeaderBinding.setLesson(lesson);
                break;
            case VIEW_TEXT:
                LessonDetailTextViewHolder lessonDetailTextViewHolder = (LessonDetailTextViewHolder) holder;
                lessonDetailTextViewHolder.itemLessonDetailTextBinding
                        .setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.setView(view);

                //use a loop to change text color
                String text = lessonDetailTextViewHolder.itemLessonDetailTextBinding.getLessonDetail().getBody();
                if (query != null && !query.isEmpty() &&
                        text.toUpperCase().contains(query.toUpperCase())) {
                    Log.d(TAG, "onBindViewHolder: id:" + lessonDetailTextViewHolder.itemLessonDetailTextBinding.getLessonDetail().getId());
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                    Spannable WordtoSpan = new SpannableString(text);
                    int startIndex = text.toUpperCase().indexOf(query.toUpperCase());
                    int stopIndex = startIndex + query.length();
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    WordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setText(WordtoSpan);
                } else {
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                }
                break;
        }
    }

    /**
     * @param lessonDetails list of lesson details to display
     */
    void setLessonDetails(List<LessonDetail> lessonDetails) {
        this.lessonDetails.clear();
        this.lessonDetails.addAll(lessonDetails);
        notifyDataSetChanged();
    }

    private class LessonHeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonHeaderBinding itemLessonHeaderBinding;

        LessonHeaderViewHolder(ItemLessonHeaderBinding itemLessonHeaderBinding) {
            super(itemLessonHeaderBinding.getRoot());
            this.itemLessonHeaderBinding = itemLessonHeaderBinding;
        }
    }



}