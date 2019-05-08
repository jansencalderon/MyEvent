package eventcoordinator2017.myevent.ui.pack.tabs;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ItemItemBinding;
import eventcoordinator2017.myevent.model.data.Item;
import io.realm.RealmList;

/**
 * Created by Sen on 2/6/2017.
 */


public class PackListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> itemList;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public PackListAdapter(RealmList<Item> itemsList) {
        this.itemList = itemsList;
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemItemBinding itemItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_item, parent, false);
        return new ItemViewHolder(itemItemBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.itemItemBinding.setItem(itemList.get(position));

    }

    public void setEvents(List<Item> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }


    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemItemBinding itemItemBinding;

        public ItemViewHolder(ItemItemBinding itemItemBinding) {
            super(itemItemBinding.getRoot());
            this.itemItemBinding = itemItemBinding;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
