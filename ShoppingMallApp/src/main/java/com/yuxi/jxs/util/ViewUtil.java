package com.yuxi.jxs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.R;


import java.io.File;
import java.io.UnsupportedEncodingException;


public class ViewUtil {
    public static void bindView(TextView textView, String txt) {
        if (!TextUtils.isEmpty(txt)) {
            textView.setText(txt);
        } else {
            textView.setText("");
        }
    }

    /**
     * 添加imageView
     */
    public static void bindImageView(@NonNull ImageView view, String url) {
        Context context = MyApplaction.getInstance();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);

    }

    /**
     * 添加imageView
     */
    public static void bindImageView(@NonNull ImageView view, int drawleId) {
        Context context = MyApplaction.getInstance();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(drawleId)
                .apply(requestOptions)
                .into(view);

    }

    private static final int MAX_SIZE = 4096;
    private static final int MAX_SCALE = 8;

    /**
     * 添加imageView
     */
    public static void bindLognImageView(@NonNull ImageView view, String url, int width) {
        Context context = MyApplaction.getInstance();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head_picture)
                .error(R.drawable.head_picture)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = BitmapUtils.drawableToBitmap(resource);
                        if (bitmap != null) {
                            int bwidth = bitmap.getWidth();
                            int bheight = bitmap.getHeight();
                            if (bwidth > width) {
                                float ratio = (float) width / (float) bwidth;
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = (int) (bheight * ratio);
                                view.setLayoutParams(layoutParams);
                            } else if (bwidth < width) {
                                float ratio = (float) bwidth / (float) width;
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = (int) (bheight / ratio);
                                view.setLayoutParams(layoutParams);
                            } else {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = bheight;
                            }
                        }
                        view.setImageDrawable(resource);
                    }
                });

    }

    /**
     * 添加圆形imageView
     */
    public static void bindCircleImageView(@NonNull ImageView view, String url) {
        Context context = MyApplaction.getInstance();
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.head_picture)
                .priority(Priority.HIGH)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);

    }

    /**
     * 添加圆形imageView
     */
    public static void bindCircleImageView(@NonNull ImageView view, int drawleId) {
        Context context = MyApplaction.getInstance();
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.head_picture)
                .centerCrop()
                .priority(Priority.HIGH)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(drawleId)
                .apply(requestOptions)
                .into(view);
    }

    /**
     * 添加四边圆角imageView
     */
    public static void bindRoundImageView(@NonNull ImageView view, int drawleId) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.ALL));
        //Glide 加载图片简单用法
        Glide.with(context).load(drawleId)
                .apply(myOptions).into(view);
    }

    /**
     * 添加四边圆角imageView
     */
    public static void bindRoundImageView(@NonNull ImageView view, String url) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.ALL));
        //Glide 加载图片简单用法
        Glide.with(context).load(url)
                .apply(myOptions).into(view);
    }

    /**
     * 添加上边圆角imageView
     */
    public static void bindTopRoundImageView(@NonNull ImageView view, int drawleId) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.TOP_LEFT_TOP_RIGHT));
        //Glide 加载图片简单用法
        Glide.with(context).load(drawleId)
                .apply(myOptions).into(view);
    }

    /**
     * 添加上边圆角imageView
     */
    public static void bindTopRoundImageView(@NonNull ImageView view, String url) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.TOP_LEFT_TOP_RIGHT));
        //Glide 加载图片简单用法
        Glide.with(context).load(url)
                .apply(myOptions).into(view);
    }


    /**
     * 添加左边圆角imageView
     */
    public static void bindLeftRoundImageView(@NonNull ImageView view, int drawleId) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.LEFT));
        //Glide 加载图片简单用法
        Glide.with(context).load(drawleId)
                .apply(myOptions).into(view);
    }

    /**
     * 添加左边圆角imageView
     */
    public static void bindLeftRoundImageView(@NonNull ImageView view, String url) {
        Context context = MyApplaction.getInstance();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .centerCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalTransform
                        (new GlideRoundedCornersTransform(UIUtils.dp2px(8)
                                , GlideRoundedCornersTransform.CornerType.LEFT));
        //Glide 加载图片简单用法
        Glide.with(context).load(url)
                .apply(myOptions).into(view);
    }

    /**
     * 解密
     * encodeWord：加密后的文字/比如密码
     */
    public static String setDecrypt(String encodeWord) {

        try {
            String decodeWord = new String(Base64.decode(encodeWord, Base64.NO_WRAP), "utf-8");
            LogUtils.i("Tag", "decode wrods = " + decodeWord);
            return decodeWord;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < 16) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

}
