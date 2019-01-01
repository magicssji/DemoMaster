package wxd.demo.demomaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import wxd.demo.demomaster.R;
import wxd.demo.demomaster.activity.OneActivity;
import wxd.demo.demomaster.activity.TwoActivity;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView one;
    private TextView two;
    private TextView three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initLisenter();
    }

    private void initView() {
        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
    }

    private void initLisenter() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:      //图片预览
                startActivity(new Intent(this, OneActivity.class));
                break;
            case R.id.two:      //图片压缩
                startActivity(new Intent(this, TwoActivity.class));
                break;
            case R.id.three:    //圆形图片
                startActivity(new Intent(this, ThreeActivity.class));
                break;
        }
    }
}
