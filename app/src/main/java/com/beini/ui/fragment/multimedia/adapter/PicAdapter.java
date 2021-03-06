package com.beini.ui.fragment.multimedia.adapter;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.bean.BaseBean;
import com.beini.ui.fragment.multimedia.bean.ImageBean;

import java.util.List;


/**
 * Created by beini on 2016/7/13.
 */
public class PicAdapter extends BaseAdapter {
    private List<ImageBean> imageBeens;
    private LruCache<String, Bitmap> cache;

    public PicAdapter(BaseBean baseBean) {
        super(baseBean);
        this.imageBeens = baseBean.getBaseList();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 10;
        this.cache = new LruCache<>(cacheSize);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Bitmap bitmap = cache.get(imageBeens.get(position).getUrl());
        if (bitmap == null) {
            getSimpleDraweeView((ViewHolder) holder, R.id.facebook_image).setImageBitmap(imageBeens.get(position).getBitmaps());
            cache.put(imageBeens.get(position).getUrl(), imageBeens.get(position).getBitmaps());
        } else {
            getSimpleDraweeView((ViewHolder) holder,R.id.facebook_image).setImageBitmap(bitmap);
        }
//		getSimpleDraweeView(R.id.facebook_image).setImageURI("file://" + imageBeens.get(position).getUrl());
    }


}
