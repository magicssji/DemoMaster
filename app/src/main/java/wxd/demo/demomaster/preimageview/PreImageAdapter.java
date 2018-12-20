package wxd.demo.demomaster.preimageview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

//图片预览适配器
public class PreImageAdapter extends PagerAdapter {

    private List<PreImageHolder> imagePath;
    private OnImageClick onImageClick;
    private PreImageLoader preImageLoader;   //图片加载器

    public PreImageAdapter(List<PreImageHolder> imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int getCount() {
        return imagePath.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //定义可以缩放的图片对象
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setScaleType(ImageView.ScaleType.FIT_START);
        photoView.setAdjustViewBounds(true);
        if (preImageLoader != null) {
            preImageLoader.showView(container.getContext(), photoView, imagePath.get(position));
        }
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onImageClick != null) {
                    onImageClick.click(position);
                }
            }
        });
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //设置当图片被点击时回调
    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    //当图片被点击时调用
    public interface OnImageClick {
        void click(int position);
    }

    //设置图片加载器
    public void setImageLoader(PreImageLoader preImageLoader){
        this.preImageLoader = preImageLoader;

    }

}
