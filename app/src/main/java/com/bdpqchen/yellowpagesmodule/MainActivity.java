package com.bdpqchen.yellowpagesmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.activity.CustomViewActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.tv_btn)
    TextView tvBtn;
    @InjectView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
//        startActivity(new Intent(this, HomeActivity.class));


    }


    @OnClick({R.id.tv_btn, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn:
            case R.id.button:
                startYellowPage();
                break;

        }
    }

    private void startYellowPage() {
        startActivity(new Intent(this, CustomViewActivity.class));
//        startActivity(new Intent(this, HomeActivity.class));
    }
}
