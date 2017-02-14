package eventcoordinator2017.myevent.ui.pack.tabs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.app.Constants;
import eventcoordinator2017.myevent.databinding.FragmentPackPageBinding;
import eventcoordinator2017.myevent.model.data.Category;
import eventcoordinator2017.myevent.model.data.Package;
import io.realm.Realm;

/**
 * Created by Mark Jansen Calderon on 2/8/2017.
 */

public class PackPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_PACKAGE_ID = "packages-id";
    public static final String ARG_CATEGORY_ID = "category-id";

    private int mPage;
    private int packageId;
    private int categoryId;
    private FragmentPackPageBinding binding;
    private Realm realm;

    public static PackPageFragment newInstance(int page, int packageId, int categoryId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_PACKAGE_ID,packageId);
        args.putInt(ARG_CATEGORY_ID,categoryId);
        PackPageFragment fragment = new PackPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        packageId = getArguments().getInt(ARG_PACKAGE_ID);
        categoryId = getArguments().getInt(ARG_CATEGORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pack_page, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        Log.e("TAG","Pack ID: "+ packageId+"\nCat ID: "+categoryId);
        Package aPackage = realm.where(Package.class).equalTo(Constants.PACKAGE_ID, packageId).findFirst();
        Category category = realm.where(Category.class).equalTo("packageId",aPackage.getPackageId()).equalTo("categoryId",categoryId).findFirst();
        binding.categoryDesc.setText(category.getCategoryDescription());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new PackListAdapter(category.getItems()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
