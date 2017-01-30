package eventcoordinator2017.myevent.ui.events.add.packages;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ItemPackageBinding;
import eventcoordinator2017.myevent.model.data.Package;
import eventcoordinator2017.myevent.ui.events.add.EventAddView;

/**
 * Created by Sen on 1/26/2017.
 */

public class PackagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private EventAddView packagesView;
    private List<Package> packageList;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public PackagesListAdapter(EventAddView packagesView) {
        this.packagesView = packagesView;
        packageList = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPackageBinding itemPackageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_package, parent, false);
        return new PackageViewHolder(itemPackageBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PackageViewHolder eventViewHolder = (PackageViewHolder) holder;
        eventViewHolder.itemPackageBinding.setAPackage(packageList.get(position));
        eventViewHolder.itemPackageBinding.setView(packagesView);
    }

    public void setPackages(List<Package> packageList) {
        this.packageList.clear();
        this.packageList.addAll(packageList);
        notifyDataSetChanged();
    }

    public void clear() {
        packageList.clear();
        notifyDataSetChanged();
    }



    public class PackageViewHolder extends RecyclerView.ViewHolder {
        private ItemPackageBinding itemPackageBinding;

        public PackageViewHolder(ItemPackageBinding itemPackageBinding) {
            super(itemPackageBinding.getRoot());
            this.itemPackageBinding = itemPackageBinding;
        }
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }
}
