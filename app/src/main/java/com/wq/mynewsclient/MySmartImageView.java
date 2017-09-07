package com.wq.mynewsclient;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/9/6.
 */

public class MySmartImageView extends ImageView {
    public MySmartImageView(Context context) {
        super(context);
    }

    public MySmartImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MySmartImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MySmartImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     *
     * @param URL
     */
    public void setURL(String URL){
        
    }
}
