package eventcoordinator2017.myevent.ui.events.add.venue;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ItemLocationBinding;
import eventcoordinator2017.myevent.model.data.Location;

/**
 * Created by Sen on 1/26/2017.
 */

public class EventLocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private EventAddLocationView view;
    private List<Location> list;
    private boolean loading;

    public EventLocationListAdapter(EventAddLocationView view) {
        this.view = view;
        list = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLocationBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_location, parent, false);
        return new LocationViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
        locationViewHolder.itemLocationBinding.setLocation(list.get(position));
        locationViewHolder.itemLocationBinding.setView(view);
        Glide.with(holder.itemView.getContext())
                .load(Constants.URL_IMAGE + list.get(position).getLocImage())
                .into(locationViewHolder.itemLocationBinding.locationImage);
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private ItemLocationBinding itemLocationBinding;

        public LocationViewHolder(ItemLocationBinding itemLocationBinding) {
            super(itemLocationBinding.getRoot());
            this.itemLocationBinding = itemLocationBinding;
        }
    }


    public void setLocations(List<Location> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
