package wxd.demo.demomaster.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import wxd.demo.demomaster.R;


//图片缓存工具，不支持加载大图
public class ImgCache {

    private LruCache<String, Bitmap> mMemoryCache;    //利用LruCache缓存图片
    private int maxMemory;    // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
    private int proportion = 4;// 使用最大可用内存值的1/4作为缓存的大小。
    private int reqHeight = 100, reqWidth = 100; //期望缩放后的图片宽高
    private Context context;

    public ImgCache(Context context) {
        this.context = context;
        // LruCache通过构造函数传入缓存值，以KB为单位。
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / proportion;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    //这里加载图片默认已经经过缩放处理，不合适加载大图
    public void loadBitmap(int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //如果缓存里面没有就使用默认的图片
            imageView.setImageResource(R.mipmap.ic_launcher);
            LoadWorkerTask task = new LoadWorkerTask();
            task.execute(resId);
        }
    }

    //添加图片
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    //获取图片
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    class LoadWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        // 在后台加载图片。
        @Override
        protected Bitmap doInBackground(Integer... params) {
            //这里将经过处理的图片加载到了内存中
            final Bitmap bitmap = decodeImgFromResource(params[0], reqHeight, reqWidth);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }
    }

    //获取压缩图片，下面先采用了采样率压缩(又称尺寸压缩)，RGB_565压缩（适用对透明度无要求的图片）
    //没有采用质量压缩是因为质量压缩的特点是:  File形式的图片被压缩了,
    // 但是当你重新读取压缩后的file为Bitmap是,它占用的内存并没有改变
    public Bitmap decodeImgFromResource(int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;      //测量宽高时，不需要加载到内存中
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        // 调用calculateInSampleSize计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //将inPreferredConfig设置为RGB_565， 将会进一步降低图片大小，但不推荐使用有透明度要求的图片
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        //decodeStream，是因为相对来说加载图片时会占据更小内存
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    //按照预期宽高计算图片适当的缩放比例，缩放比例总为2的倍数
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //计算inSampleSize直到缩放后的宽高都小于指定的宽高
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
