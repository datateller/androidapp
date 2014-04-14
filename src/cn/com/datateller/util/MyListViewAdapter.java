package cn.com.datateller.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter{

	private LayoutInflater mInflater = null;
	private List<Map<String, Object>> data;
	
	static class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView tvabstract;
	}
	
	
	public MyListViewAdapter(Context context,List<Map<String, Object>> data) {
		// TODO Auto-generated constructor stub
		super();
		this.mInflater = LayoutInflater.from(context);
		this.data=data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(cn.com.datateller.R.layout.basic_knowledge_listview_item,
					null);
			// knowledgeImageView.setImageBitmap(BitmapFactory.decodeFile(knowledge.getKnowledgePicLink()));
			holder.img = (ImageView) convertView.findViewById(cn.com.datateller.R.id.img);
			holder.title = (TextView) convertView
					.findViewById(cn.com.datateller.R.id.tvtitle);
			holder.tvabstract=(TextView)convertView.findViewById(cn.com.datateller.R.id.tvabstract);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText((String) data.get(position).get("title"));
//		Log.d(TAG + " online 223", (String) data.get(position).get("img"));
		// ´Ë´¦ÊÇbug
		// if((String)data.get(position).get("img")=="")
		// holder.img.setImageBitmap(BitmapFactory.decodeFile("/mnt/sdcard/test3.png"));
		holder.img.setImageBitmap(BitmapFactory.decodeFile((String) data
				.get(position).get("img")));
		holder.tvabstract.setText((String)data.get(position).get("abstract"));
		return convertView;
	}

}
