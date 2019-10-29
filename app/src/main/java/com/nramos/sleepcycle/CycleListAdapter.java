package com.nramos.sleepcycle;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CycleListAdapter extends ArrayAdapter<Cycle> {

    private static final String TAG = "CycleListAdapter";
    private Context mContext;
    int mResource;

    public CycleListAdapter(Context context, int resource, ArrayList<Cycle> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //get the Cycle's information
        Date cycleTime = getItem(position).getTime();
        String cycleNumber = getItem(position).getNumberOfCycles();
        int cycleHour = getItem(position).getHour();
        int cycleMinute = getItem(position).getMinute();
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");


        //LinearLayout oneRow = convertView.findViewById(R.id.adapterLinearLayout);

        //create the cycle object with the information
        //Cycle cycle = new Cycle(cycleTime, cycleNumber, cycleHour, cycleMinute);
        //Log.d(TAG, cycle.toString());
        if(Integer.parseInt(cycleNumber) == 1)
        {
            cycleHour = 1;
            cycleMinute = 30;
        }
        else if(Integer.parseInt(cycleNumber) % 2 != 0)
        {
            //odds
            if(Integer.parseInt(cycleNumber) == 3)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 1;
                cycleMinute = 30;
            }
            if(Integer.parseInt(cycleNumber) == 5)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 2;
                cycleMinute = 30;
            }
            if(Integer.parseInt(cycleNumber) == 7)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 3;
                cycleMinute = 30;
            }
            if(Integer.parseInt(cycleNumber) == 9)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 4;
                cycleMinute = 30;
            }
        }
        else if(Integer.parseInt(cycleNumber) % 2 == 0)
        {
            //evens
            if(Integer.parseInt(cycleNumber) == 2)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 1;
                cycleMinute = 0;
            }
            if(Integer.parseInt(cycleNumber) == 4)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 2;
                cycleMinute = 0;
            }
            if(Integer.parseInt(cycleNumber) == 6)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 3;
                cycleMinute = 0;
            }
            if(Integer.parseInt(cycleNumber) == 8)
            {
                cycleHour = Integer.parseInt(cycleNumber) + 4;
                cycleMinute = 0;
            }
        }


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView cycleTimeTextview = convertView.findViewById(R.id.cycleTimeTextView);
        TextView numOfCyclesTextView = convertView.findViewById(R.id.numOfCyclesTextView);

        cycleTimeTextview.setText(formatTime.format(cycleTime) + "");
        numOfCyclesTextView.setText(cycleNumber + " cycles ~ " + cycleHour + "h " + cycleMinute + "m");

        //ideal sleep cycles are to be emphasized
        if(position == 4 || position == 5 || position == 6)
        {
            cycleTimeTextview.setTextColor(Color.parseColor("#3df7c5"));
            numOfCyclesTextView.setTextColor(Color.parseColor("#3df7c5"));
        }

        return convertView;
    }
}
