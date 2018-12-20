package wxd.demo.demomaster.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 *图片压缩工具
 *使用方法：传入int资源，压缩后为bitmap，传入图片文件资源，压缩后仍为图片文件
 *
 *  2018/12/19      QQ:2381144912       WANXUEDONG
 */
public class ImgCompress implements Handler.Callback {

    private static final int MSG_IMGCOMPRESS_BITMAP_SUCCESS = 1000;
    private static final int MSG_IMGCOMPRESS_FILE_SUCCESS = 1001;
    private static final int MSG_IMGCOMPRESS_ERROR = 1002;

    private static final String LOGS = "imgcompress";    //错误查看log
    private static final String FIELPOSITION = "xdw_cache_imgcompress";   //产生图片所在的父级目录名称
    private static List<File> files;
    private static List<Integer> bitmaps;
    private Handler handler;
    private OnCompress onCompress;

    private ImgCompress(QueueCache cache) {
        this.onCompress = cache.onCompress;
        files = cache.files;
        bitmaps = cache.bitmaps;
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public static QueueCache with(Context context) {
        return new QueueCache(context);
    }

    //将每个压缩当成一个事件放到队列中
    public static class QueueCache {

        private Context context;
        private List<File> files;
        private List<Integer> bitmaps;
        private OnCompress onCompress;

        public QueueCache(Context context) {
            this.context = context;
            files = new ArrayList<>();
            bitmaps = new ArrayList<>();
        }

        public QueueCache load(int resId) {
            bitmaps.add(resId);
            return this;
        }

        public QueueCache load(File file) {
            files.add(file);
            return this;
        }

        public void launch() {
            new ImgCompress(this).launch(context);
        }

        public QueueCache setOnComPress(OnCompress onComPress) {
            this.onCompress = onComPress;
            return this;
        }

    }

    private void launch(final Context context) {

        Iterator<File> fileIterator = files.iterator();
        Iterator<Integer> integerIterator = bitmaps.iterator();

        while (fileIterator.hasNext()) {
            final File file = fileIterator.next();
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    File result;
                    try {
                        result = decodeImgFromResource(context, file);
                        handler.sendMessage(handler.obtainMessage(MSG_IMGCOMPRESS_FILE_SUCCESS, result));
                    } catch (IOException e) {
                        handler.sendMessage(handler.obtainMessage(MSG_IMGCOMPRESS_ERROR, e));
                        Log.d(LOGS, "error " + e.getMessage());
                    }
                }
            });
            fileIterator.remove();
        }
        while (integerIterator.hasNext()) {
            final Integer resId = integerIterator.next();
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap result;
                        result = decodeImgFromResource(context, resId);
                        handler.sendMessage(handler.obtainMessage(MSG_IMGCOMPRESS_BITMAP_SUCCESS, result));
                    } catch (Exception e) {
                        handler.sendMessage(handler.obtainMessage(MSG_IMGCOMPRESS_ERROR, e));
                        Log.d(LOGS, "error " + e.getMessage());
                    }
                }
            });
            integerIterator.remove();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (onCompress == null) return false;
        switch (msg.what) {
            case MSG_IMGCOMPRESS_FILE_SUCCESS:
                onCompress.compressFileSuccess((File) msg.obj);
                break;
            case MSG_IMGCOMPRESS_BITMAP_SUCCESS:
                onCompress.compressBitmapSuccess((Bitmap) msg.obj);
                break;
            case MSG_IMGCOMPRESS_ERROR:
                onCompress.compressFail((Throwable) msg.obj);
                break;
        }
        return false;
    }

    private Bitmap decodeImgFromResource(Context context, int resId) {
        return decodeImgFromResource(context, resId, 100, 100);
    }

    //传入将要压缩的图片位置,将会返还一个压缩后的新的图片文件
    private File decodeImgFromResource(Context context, File file) throws IOException {
        return decodeImgFromResource(context, file, 100, 100);
    }

    //获取压缩图片，下面先采用了采样率压缩(又称尺寸压缩)，RGB_565压缩（适用对透明度无要求的图片）
    //没有采用质量压缩是因为质量压缩的特点是:  File形式的图片被压缩了,
    // 但是当你重新读取压缩后的file为Bitmap是,它占用的内存并没有改变
    private Bitmap decodeImgFromResource(Context context, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;      //测量宽高时，不需要加载到内存中
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        // 调用calculateInSampleSize计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //将inPreferredConfig设置为RGB_565， 将会进一步降低图片大小，但不推荐使用有透明度要求的图片
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        //推荐使用decodeStream，是因为相对来说加载图片时会占据更小内存
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    //传入将要压缩的图片位置,将会返还一个压缩后的新的图片文件
    private File decodeImgFromResource(Context context, File file, int reqWidth, int reqHeight) throws IOException {
        File tagImgFile = new File(makeName(context));
        if (tagImgFile != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.decodeStream(new FileInputStream(tagImgFile), null, options);
            options.inSampleSize = calculateInSampleSize(options);
            if (file != null) {
                Bitmap tagBitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                tagBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                tagBitmap.recycle();
                FileOutputStream fos = new FileOutputStream(tagImgFile);
                fos.write(stream.toByteArray());
                fos.flush();
                fos.close();
                stream.close();
                return tagImgFile;
            } else {
                Log.e(LOGS, "the incoming picture does not exist!");
                return null;
            }
        } else {
            Log.e(LOGS, "can't make a file position!");
            return null;
        }
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

    //自己计算图片适当的缩放比例,和鲁班压缩一致
    private int calculateInSampleSize(BitmapFactory.Options options) {
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);
        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    //给新产生的图片生成一个路径
    private String makeName(Context context) {
        File cacheDir = null;
        try {
            cacheDir = context.getExternalCacheDir();
        } catch (Exception e) {
            Log.e(LOGS, "error : " + e.getMessage());
        }
        if (cacheDir != null) {
            File result = new File(cacheDir, FIELPOSITION);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                return null;
            }
            String cacheBuilder = "/" + System.currentTimeMillis() + "/" + (int) (Math.random() * 100) + ".jpg";
            return result.getAbsolutePath() + cacheBuilder;
        }
        Log.e(LOGS, "can't make a file position!");
        return null;
    }

    public interface OnCompress {

        void compressBitmapSuccess(Bitmap bitmap);

        void compressFileSuccess(File file);

        void compressFail(Throwable message);

    }

}
