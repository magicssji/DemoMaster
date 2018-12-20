package wxd.demo.demomaster;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import wxd.demo.demomaster.utils.ImgCompress;


//图片压缩代码demo
public class TwoActivity extends AppCompatActivity {

    private ImageView pic;
    private ImageView pic1;
    private int[] bitmap = new int[]{R.mipmap.add_theme, R.mipmap.iwowke_logo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        pic = (ImageView) findViewById(R.id.pic);
        pic1 = (ImageView) findViewById(R.id.pic1);
        for (int i = 0; i < 2; i++) {
            final int finalI = i;
            ImgCompress.with(this).setOnComPress(new ImgCompress.OnCompress() {
                @Override
                public void compressBitmapSuccess(Bitmap bitmap) {
                    Log.d("compressFileSuccess", "1" + bitmap.getByteCount());
                    if (finalI == 0) {
                        pic.setImageBitmap(bitmap);
                    } else {
                        pic1.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void compressFileSuccess(File file) {
                    Log.d("compressFileSuccess", "2" + file);
                }

                @Override
                public void compressFail(Throwable message) {
                    Log.d("compressFileSuccess", "3" + message.getMessage());

                }
            }).load(bitmap[i]).launch();
        }
    }
}
