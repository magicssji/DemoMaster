# DemoMaster
安卓进阶训练demo
代码1-1(图片预览)
使用方法：
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
参考简书文章：https://www.jianshu.com/p/043f151c00df
