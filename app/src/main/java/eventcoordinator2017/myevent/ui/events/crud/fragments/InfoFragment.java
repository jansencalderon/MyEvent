package eventcoordinator2017.myevent.ui.events.crud.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import eventcoordinator2017.myevent.R;

/**
 * Created by Mark Jansen Calderon on 2/1/2017.
 */

public class InfoFragment extends Fragment{
    Context context;

    public InfoFragment newInstance(Context context) {
        InfoFragment info = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("TYPE", "ADD");
        info.setArguments(args);
        this.context = context;
        return info;
    }

    public InfoFragment() {
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_events_crud_add, container, false);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Cancel Event Creation");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                       getActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.next:
/*                presenter.toBudget(binding.eventName.getText().toString(),
                        binding.eventDescription.getText().toString(),
                        binding.tagGroup.getTags(),
                        binding.eventLocation.getText().toString(),
                        binding.eventFromDate.getText().toString(),
                        binding.eventFromTime.getText().toString(),
                        binding.eventToDate.getText().toString(),
                        binding.eventToTime.getText().toString(),
                        eventLat, eventLng);*/

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.next,menu);
    }
}
