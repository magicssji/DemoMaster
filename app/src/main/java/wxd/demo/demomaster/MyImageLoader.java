package wxd.demo.demomaster;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import wxd.demo.demomaster.preimageview.PreImageHolder;
import wxd.demo.demomaster.preimageview.PreImageLoader;

public class MyImageLoader implements PreImageLoader {
    @Override
    public void showView(Context context, ImageView img, PreImageHolder imgUrl) {
        Glide.with(context).load(imgUrl.getPath()).into(img);
    }
}
