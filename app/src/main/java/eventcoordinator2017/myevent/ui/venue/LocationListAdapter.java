package eventcoordinator2017.myevent.ui.venue;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ItemTextBinding;

/**
 * Created by Sen on 1/26/2017.
 */

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private boolean loading;

    public LocationListAdapter(List<String> list) {
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTextBinding itemTextBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_text, parent, false);
        return new ViewHolder(itemTextBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemTextBinding.textContent.setText(list.get(position).trim());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTextBinding  itemTextBinding;

        public ViewHolder(ItemTextBinding itemTextBinding) {
            super(itemTextBinding.getRoot());
            this.itemTextBinding = itemTextBinding;
        }
    }


    public void setList(List<String> list) {
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
