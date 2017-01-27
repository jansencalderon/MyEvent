package eventcoordinator2017.myevent.ui.events;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ItemEventBinding;
import eventcoordinator2017.myevent.model.data.Event;

/**
 * Created by Sen on 1/26/2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private EventsView eventsView;
    private List<Event> eventList;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public EventsListAdapter(EventsView eventsView) {
        this.eventsView = eventsView;
        eventList = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEventBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_event, parent, false);
        return new EventViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventViewHolder eventViewHolder = (EventViewHolder) holder;
        eventViewHolder.itemEventBinding.setEvent(eventList.get(position));
        eventViewHolder.itemEventBinding.setView(eventsView);

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
        private ItemEventBinding itemEventBinding;

        public EventViewHolder(ItemEventBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
