package cn.com.datateller.util;

import java.util.List;

import cn.com.datateller.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{

	private List<String> list;
	private Context context;
	
	public SpinnerAdapter(Context context,List<String> list) {
		// TODO Auto-generated constructor stub
	    this.context=context;
	    this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater=LayoutInflater.from(context);
		convertView=layoutInflater.inflate(R.layout.spinner_item, null);
		if(convertView!=null){
			TextView textView=(TextView)convertView.findViewById(R.id.spinner_item_textView1);
			textView.setText(list.get(position));
		}
		return convertView;
	}

	
	
}
