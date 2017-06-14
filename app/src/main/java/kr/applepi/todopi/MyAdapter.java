package kr.applepi.todopi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ilhoon on 8/30/16.
 */
public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Data> items;

    public MyAdapter(Context context, ArrayList<Data> items){
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list, null);

        TextView contentText = (TextView) view.findViewById(R.id.main_text);
        TextView dateText = (TextView) view.findViewById(R.id.side_text);

        Data data = items.get(position);
        contentText.setText((data.content));
        dateText.setText((data.date));
        return view;
    }
}
