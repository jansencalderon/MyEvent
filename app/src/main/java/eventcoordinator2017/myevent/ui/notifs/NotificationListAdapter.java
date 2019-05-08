package eventcoordinator2017.myevent.ui.notifs;

import androidx.databinding.DataBindingUtil;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ItemNotificationBinding;
import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Sen on 1/26/2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NotificationsView mvpView;
    private List<Event> eventList;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public NotificationListAdapter(NotificationsView mvpView) {
        this.mvpView = mvpView;
        eventList = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_notification, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventViewHolder eventViewHolder = (EventViewHolder) holder;
        Event event = eventList.get(position);
        eventViewHolder.binding.setEvent(eventList.get(position));
        eventViewHolder.binding.setView(mvpView);
        String toHtml = "<b>" + event.getUser().getFullName() + "</b> has invited you to <b>"+event.getEventName()+"</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            eventViewHolder.binding.content.setText(Html.fromHtml(toHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            eventViewHolder.binding.content.setText(Html.fromHtml(toHtml));
        }
        Glide.with(eventViewHolder.itemView.getContext())
                .load(Constants.URL_IMAGE + eventList.get(position).getImageDirectory())
                .centerCrop()
                .error(R.drawable.ic_gallery)
                .dontAnimate()
                .into(eventViewHolder.binding.userImage);

    }

    public void setEvents(List<Event> eventList) {
        this.eventList.clear();
        this.eventList.addAll(eventList);
        notifyDataSetChanged();
    }

    public void clear() {
        eventList.clear();
        notifyDataSetChanged();
    }


    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding binding;

        public EventViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
