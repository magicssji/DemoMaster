package wxd.demo.demomaster.view.preimageview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wxd.demo.demomaster.R;

/*
 * 图片预览Activity
 *
 * 使用方法：
 * 使用时直接传入图片的item集合即可，需要设置图片加载器，继承PreImageLoader即可，不设置则无法使用
 * 图片和默认选中第几张，通过intent传递CACHE_IMAGE，PRE_DEFAULT_CHOSE即可
 *
 *
 * 2018/12/17      QQ:2381144912       WANXUEDONG
 */

public class PreImageActivity extends Activity implements View.OnClickListener {

    private RelativeLayout preImgPagerHeadHolder;
    private PreViewPager viewPager;
    private ImageView preImgPagerBack;           //返回按钮
    private TextView preImgPagerCount;          //显示当前翻到的页面
    private PreImageAdapter preImageAdapter;    //图片适配器
    private List<PreImageHolder> imageList;      //存放图片的集合
    private PreImageLoader preImageLoader;       //图片加载器
    public static final String CACHE_IMAGE = "pre_image_show";   //intent利用此值传递图片数据
    public static final String PRE_DEFAULT_CHOSE = "pre_default_show";  //intent利用此值传递默认选中第几张图片
    public int currentShow = 1;   //默认选中的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_image);
        initView();
        initData();
        initlisenter();
    }

    private void initView() {
        preImgPagerHeadHolder = (RelativeLayout) findViewById(R.id.pre_img_pager_head_holder);
        viewPager = (PreViewPager) findViewById(R.id.pre_img_pager_show);
        preImgPagerBack = (ImageView) findViewById(R.id.pre_img_pager_back);
        preImgPagerCount = (TextView) findViewById(R.id.pre_img_pager_count);
    }

    private void initData() {
        imageList = new ArrayList<>();
        imageList = (ArrayList<PreImageHolder>) getIntent().getSerializableExtra(CACHE_IMAGE);
        currentShow = getIntent().getIntExtra(PRE_DEFAULT_CHOSE, 1);
        if (currentShow < 1) {
            currentShow = 1;
        }
        if (currentShow > imageList.size()) {
            currentShow = imageList.size();
        }
        preImageLoader = PreImageConfige.getInstance().getPreImageLoader();
        if (imageList != null && imageList.size() > 0) {
            preImageAdapter = new PreImageAdapter(imageList);
            if (preImageLoader != null) {
                setImageLoader(preImageLoader);
            }
            viewPager.setAdapter(preImageAdapter);
            viewPager.setCurrentItem(currentShow - 1);
            preImgPagerCount.setText(currentShow + "/" + imageList.size());
        } else {
            preImgPagerCount.setText("未传入图片");
        }
    }

    public void setImageLoader(PreImageLoader preImageLoader) {
        if (preImageAdapter != null) {
            preImageAdapter.setImageLoader(preImageLoader);
        }
    }

    private void initlisenter() {
        preImgPagerBack.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                preImgPagerCount.setText(i + 1 + "/" + imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        if (preImageAdapter != null) {
            preImageAdapter.setOnImageClick(new PreImageAdapter.OnImageClick() {
                @Override
                public void click(int position) {     //当图片被点击时，隐藏或者展示头部
                    if (preImgPagerHeadHolder.getVisibility() == View.VISIBLE) {
                        preImgPagerHeadHolder.setAnimation(AnimationUtils.loadAnimation(PreImageActivity.this, R.anim.pre_image_top_out));
                        preImgPagerHeadHolder.setVisibility(View.GONE);
                    } else {
                        preImgPagerHeadHolder.setVisibility(View.VISIBLE);
                        preImgPagerHeadHolder.setAnimation(AnimationUtils.loadAnimation(PreImageActivity.this, R.anim.pre_image_top_in));
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_img_pager_back:      //点击返回
                finish();
                break;
        }
    }

}
