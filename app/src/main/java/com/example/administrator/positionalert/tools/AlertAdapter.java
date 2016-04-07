package com.example.administrator.positionalert.tools;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.R;
import com.example.administrator.positionalert.model.AlertItem;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class AlertAdapter extends ArrayAdapter<AlertItem> {

    private int resourceId;

    public AlertAdapter(Context context, int resource, List<AlertItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }



    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final AlertItem alertItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView itemName=(TextView)view.findViewById(R.id.alert_item_name);
        TextView itemRange=(TextView)view.findViewById(R.id.alert_item_range);
        Switch on=(Switch)view.findViewById(R.id.alert_item_switch);
        Button delete = (Button)view.findViewById(R.id.alert_item_delete);

        itemName.setText(alertItem.getName());
        itemRange.setText(""+alertItem.getRange());
        on.setChecked(alertItem.isOn());
        on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (alertItem.isOn()) {
                    alertItem.setOn(false);
                } else {
                    alertItem.setOn(true);
                }

                AlertItem.updateItem(alertItem);
                mProximityAlert.refreshRing(getContext());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertItem.deleteItem(alertItem);
            }
        });
        return view;
    }
}
