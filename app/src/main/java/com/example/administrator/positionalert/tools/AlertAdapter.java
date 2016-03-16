package com.example.administrator.positionalert.tools;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        AlertItem alertItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView itemName=(TextView)view.findViewById(R.id.alert_item_name);
        TextView itemRange=(TextView)view.findViewById(R.id.alert_item_range);
        Switch on=(Switch)view.findViewById(R.id.alert_item_switch);

        itemName.setText(alertItem.getName());
        itemRange.setText(""+alertItem.getRange());
        on.setChecked(alertItem.isOn());
        return view;
    }
}
