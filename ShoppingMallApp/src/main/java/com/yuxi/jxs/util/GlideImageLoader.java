package com.yuxi.jxs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.youth.banner.loader.ImageLoader;
import com.yuxi.jxs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author 陈开.
 * Date: 2019/5/27
 * Time: 11:22
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         order_cancle.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        //Glide 加载图片简单用法
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(path)
                .apply(requestOptions)
                .into(imageView);
        //用fresco加载图片简单用法，记得要写下面的createImageView方法

    }

}
