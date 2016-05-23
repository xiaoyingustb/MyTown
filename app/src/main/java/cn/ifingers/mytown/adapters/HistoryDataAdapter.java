package cn.ifingers.mytown.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.dbutils.HistorySearchDao;
import cn.ifingers.mytown.entities.SearchHistoryBean;
import cn.ifingers.mytown.utils.TBaseAdapter;

/**
 * Created by syfing on 2016/5/20.
 */
public class HistoryDataAdapter extends TBaseAdapter<SearchHistoryBean> {
    private HistorySearchDao dao;

    public HistoryDataAdapter(List<SearchHistoryBean> mDatas, Context context) {
        super(mDatas, context);
        dao = new HistorySearchDao(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.history_list_item, null);
            holder.deleteImg = (ImageView) convertView.findViewById(R.id.history_item_delete);
            holder.timeText = (TextView) convertView.findViewById(R.id.history_item_time);
            holder.titleText = (TextView) convertView.findViewById(R.id.history_item_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final SearchHistoryBean bean = getItem(position);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteByTime(bean.getTime());
                setData(dao.queryAll());
                HistoryDataAdapter.this.notifyDataSetChanged();
            }
        });
        holder.timeText.setText(bean.getTime());
        holder.titleText.setText(bean.getTitle());
        return convertView;
    }

    class ViewHolder{
        TextView titleText;
        TextView timeText;
        ImageView deleteImg;
    }
}
