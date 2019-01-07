# DemoMaster
安卓进阶训练demo

代码1-1(图片预览)
使用方法：
```
public class MyImageLoader implements PreImageLoader {
    @Override    
    public void showView(Context context, ImageView img, PreImageHolder imgUrl) {
        Glide.with(context).load(imgUrl.getPath()).into(img);       
    }
}
PreImageConfige.getInstance().setImageLoader(new MyImageLoader());
List<PreImageHolder> imageList = new ArrayList<>();
Intent intent = new Intent(context, PreImageActivity.class);
intent.putExtra(CACHE_IMAGE, (Serializable) imageList);
intent.putExtra(PRE_DEFAULT_CHOSE, position + 1);
context.startActivity(intent);
```

参考简书文章：https://www.jianshu.com/p/043f151c00df

代码1-3(圆形图片)
使用方法：
```
            <wxd.demo.demomaster.view.RotatingCircleView
                android:layout_width="200dp"
                android:layout_height="100dp"
                android:src="@mipmap/pic"
                app:circle_color="#3ae40b"
                app:circle_kind="top_left"
                app:circle_radius="20"
                app:circle_width="2dp" />
            <wxd.demo.demomaster.view.RotatingCircleView
                android:layout_width="200dp"
                android:layout_height="100dp"
                android:src="@mipmap/pic"
                app:circle_color="#3ae40b"
                app:circle_kind="top_right"
                app:circle_radius="20"
                app:circle_width="2dp" />
```
也支持圆角多属性的设置，比如：
```
            <wxd.demo.demomaster.view.RotatingCircleView
                android:layout_width="200dp"
                android:layout_height="100dp"
                android:src="@mipmap/pic"
                app:circle_color="#3ae40b"
                app:circle_kind="top_right|bottom_left"
                app:circle_radius="20"
                app:circle_width="2dp" />
```
但是只支持俩个并列属性，多了无效，circle_radius表示每个圆角的弯曲半径，circle_color表示外圈线条颜色，circle_width表示外圈线条宽度，不设置则没有外圈。

参考简书文章：https://www.jianshu.com/p/a7498f174a32
