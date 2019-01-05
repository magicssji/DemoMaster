package wxd.demo.demomaster.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wxd.demo.demomaster.MyImageLoader;
import wxd.demo.demomaster.R;
import wxd.demo.demomaster.view.preimageview.PreImageActivity;
import wxd.demo.demomaster.view.preimageview.PreImageConfige;
import wxd.demo.demomaster.view.preimageview.PreImageHolder;

import static wxd.demo.demomaster.view.preimageview.PreImageActivity.CACHE_IMAGE;

//图片预览代码demo
public class OneActivity extends Activity {

    private String path1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545193509621&di=f1fd4e92c799a741eeef36456167cce3&imgtype=0&src=http%3A%2F%2Fs4.sinaimg.cn%2Fmw690%2F001oe1ezzy6T6QtDsBRb3%26690";
    private String path2 = "http://img1.qunarzz.com/travel/d2/1608/a1/28bb22a80beb67b5.jpg_r_509x680x95_a3e38ffe.jpg";
    private String path3 = "http://img.redocn.com/sheying/20150226/chuizhijiangluo_3959215.jpg";
    private String path4 = "http://img1.qunarzz.com/travel/d8/1608/30/bb4dd75b09dff7b5.jpg_r_549x680x95_6c2e27c8.jpg";
    private String path5 = "http://p17.qhimg.com/bdm/1600_900_85/d/_open360/fengjing0801/6.jpg";
    private String path6 = "http://img17.3lian.com/d/file/201702/10/6649171de5aa01019848317d778a3410.jpg";
    private String path7 = "http://img17.3lian.com/d/file/201702/23/dc458f76475470db9a5791848cb67801.jpg";
    private String path8 = "http://pic14.nipic.com/20110528/4397835_164854461183_2.png";
    private String path9 = "http://img17.3lian.com/201612/20/efd214cf59f120a55abf43fa271cefbd.jpg";

    private List<PreImageHolder> imageItems;

    private TextView jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jump = (TextView) findViewById(R.id.jump);
        PreImageConfige.getInstance().setImageLoader(new MyImageLoader());
        imageItems = new ArrayList<>();
        PreImageHolder imageItem1 = new PreImageHolder();
        imageItem1.path = path1;
        imageItems.add(imageItem1);
        PreImageHolder imageItem2 = new PreImageHolder();
        imageItem2.path = path2;
        imageItems.add(imageItem2);
        PreImageHolder imageItem3 = new PreImageHolder();
        imageItem3.path = path3;
        imageItems.add(imageItem3);
        PreImageHolder imageItem4 = new PreImageHolder();
        imageItem4.path = path4;
        imageItems.add(imageItem4);
        PreImageHolder imageItem5 = new PreImageHolder();
        imageItem5.path = path5;
        imageItems.add(imageItem5);
        PreImageHolder imageItem6 = new PreImageHolder();
        imageItem6.path = path6;
        imageItems.add(imageItem6);
        PreImageHolder imageItem7 = new PreImageHolder();
        imageItem7.path = path7;
        imageItems.add(imageItem7);
        PreImageHolder imageItem8 = new PreImageHolder();
        imageItem8.path = path8;
        imageItems.add(imageItem8);
        PreImageHolder imageItem9 = new PreImageHolder();
        imageItem9.path = path9;
        imageItems.add(imageItem9);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OneActivity.this, PreImageActivity.class);
                intent.putExtra(CACHE_IMAGE, (Serializable) imageItems);
                startActivity(intent);
            }
        });
    }
}
