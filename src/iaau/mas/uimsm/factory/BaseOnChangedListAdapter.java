//package iaau.mas.uimsm.factory;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//public class BaseOnChangedListAdapter extends BaseAdapter 
//{
//
//	private ArrayList<CurrentOrderClass> listData;
//	private LayoutInflater layoutInflater;
//
//	public BaseOnChangedListAdapter(Context context, ArrayList<CurrentOrderClass> listData) 
//	{
//		this.listData = listData;
//		layoutInflater = LayoutInflater.from(context);
//	}
//
//	public void setListData(ArrayList<CurrentOrderClass> data){
//	    listData = data;
//	  }
//
//	  @Override
//	  public int getCount() {
//	    return listData.size();
//	  }
//
//
//	  @Override
//	  public Object getItem(int position) {
//	    return listData.get(position);
//	  }
//
//	  @Override
//	  public long getItemId(int position) {
//	    return position;
//	  }
//
//	  public View getView(int position, View convertView, ViewGroup parent) {
//	    ViewHolder holder;
//	    if (convertView == null) {
//	        convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
//	        holder = new ViewHolder();
//	        holder.variantView = (TextView) convertView.findViewById(R.id.variant);
//	        holder.unitView = (TextView) convertView.findViewById(R.id.unit);
//	        holder.quantityView = (TextView) convertView.findViewById(R.id.quantity);
//	        convertView.setTag(holder);
//	    } else {
//	        holder = (ViewHolder) convertView.getTag();
//	    }
//
//	    holder.variantView.setText(listData.get(position).getVariantArray().get(position).toString());
//	    holder.unitView.setText(listData.get(position).getUnitArray().get(position).toString());
//	    holder.quantityView.setText(String.valueOf(listData.get(position).getQuantityRow()));
//
//	    return convertView;
//	}
//
//	  static class ViewHolder {
//	    TextView variantView;
//	    TextView unitView;
//	    TextView quantityView;
//	  }
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//}
