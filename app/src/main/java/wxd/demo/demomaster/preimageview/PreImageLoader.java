package wxd.demo.demomaster.preimageview;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/*
 *图片加载器    使用时直接继承并设置给PreImageConfige即可
 * 继承Serializable防止被混淆
 */
public interface PreImageLoader extends Serializable {

    void showView(Context context, ImageView img, PreImageHolder imgUrl);

}
