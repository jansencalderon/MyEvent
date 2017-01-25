package eventcoordinator2017.myevent.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import eventcoordinator2017.myevent.R;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class MainListAdapter extends RealmRecyclerViewAdapter {

    private Context context;


    public MainListAdapter(Context context, @Nullable OrderedRealmCollection data) {
        super(context, data, true);
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_main_card, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);

        }

    }

}
