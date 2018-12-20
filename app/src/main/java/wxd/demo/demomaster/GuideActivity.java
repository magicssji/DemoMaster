package wxd.demo.demomaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView one;
    private TextView two;

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
    }

    private void initLisenter() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
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
        }
    }
}
