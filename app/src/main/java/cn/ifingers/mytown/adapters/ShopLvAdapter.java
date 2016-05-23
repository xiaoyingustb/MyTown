package cn.ifingers.mytown.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.entities.GoodsInfo;
import cn.ifingers.mytown.utils.AsyncImgLoader;
import cn.ifingers.mytown.utils.TBaseAdapter;

/**
 * Created by syfing on 2016/5/15.
 */
public class ShopLvAdapter extends TBaseAdapter<GoodsInfo> {

    public ShopLvAdapter(List<GoodsInfo> datas, Context context) {
        super(datas, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list_item, null);
            holder = new GoodsHolder();
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else{
            holder = (GoodsHolder) convertView.getTag();
        }

        //初始化
        GoodsInfo goods = getItem(position);
        holder.imageView.setImageResource(R.drawable.ic_empty_dish);
        AsyncImgLoader.getInstance(context).loadImage(goods.getImgUrl(), holder.imageView);
        String value = goods.getValue();
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        holder.value.setText(spannableString);

        holder.title.setText(goods.getTitle());
        holder.sortTitle.setText(goods.getSortTitle());
        holder.price.setText(goods.getPrice());
        holder.number.setText(goods.getBought());
        return convertView;
    }

}

class GoodsHolder{
    @ViewInject(R.id.index_gl_item_image)
    public ImageView imageView;
    @ViewInject(R.id.index_gl_item_title)
    public TextView title;
    @ViewInject(R.id.index_gl_item_titlecontent)
    public TextView sortTitle;
    @ViewInject(R.id.index_gl_item_value)
    public TextView value;
    @ViewInject(R.id.index_gl_item_price)
    public TextView price;
    @ViewInject(R.id.index_gl_item_number)
    public TextView number;
}
