package eventcoordinator2017.myevent.ui.events.add.guests;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.ItemGuestBinding;
import eventcoordinator2017.myevent.model.data.Guest;

/**
 * Created by Sen on 1/26/2017.
 */

public class GuestsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Guest> list = new ArrayList<>();
    private GuestsView guestsView;
/*
    public GuestsListAdapter(List<Guest> list) {
        this.list = list;
    }*/

    public GuestsListAdapter(GuestsView guestsView) {
        this.guestsView = guestsView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGuestBinding itemGuestBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_guest, parent, false);
        return new ViewHolder(itemGuestBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Guest guest = list.get(position);
        viewHolder.itemGuestBinding.setGuest(guest);
        viewHolder.itemGuestBinding.setView(guestsView);
        Glide.with(viewHolder.itemView.getContext())
                .load(Constants.URL_IMAGE + guest.getImage())
                .error(R.drawable.ic_mood)
                .into(viewHolder.itemGuestBinding.userImage);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGuestBinding itemGuestBinding;

        public ViewHolder(ItemGuestBinding itemGuestBinding) {
            super(itemGuestBinding.getRoot());
            this.itemGuestBinding = itemGuestBinding;
        }
    }


    public void setList(List<Guest> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
