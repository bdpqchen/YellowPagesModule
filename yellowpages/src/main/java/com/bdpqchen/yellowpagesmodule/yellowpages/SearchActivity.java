package com.bdpqchen.yellowpagesmodule.yellowpages;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chen on 16-11-26.
 */
public class SearchActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private Toolbar mToolbar;
    private EditText mSearchBox;
    private TextView inputText;
    private ImageView mTextClear;
    private AutoCompleteTextView autoCompleteTextView;


    private static final String[] COUNTRIES = {"China", "Russia", "Germany",
            "Ukraine", "Belarus", "USA", "China1", "China2", "USA1"};


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchBox = (EditText) findViewById(R.id.search_box);
//        inputText = (TextView) findViewById(R.id.input_text);
        mTextClear = (ImageView) findViewById(R.id.text_clear);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete);


//        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Animation rotateAnimation = new RotateAnimation(0f, 360f);
        rotateAnimation.setDuration(1000);
        //初始化
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        //设置动画时间
        alphaAnimation.setDuration(3000);
        //ScaleAnimation(float fromX, float toX, float fromY, float toY,int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        Animation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
        //设置动画时间
        scaleAnimation.setDuration(700);
        mToolbar.setAnimation(scaleAnimation);
//        mToolbar.setAnimation(rotateAnimation);
//        mToolbar.setAnimation(alphaAnimation);

        mSearchBox.addTextChangedListener(this);
        mTextClear.setOnClickListener(this);
        setSoftInputShow(mSearchBox);

        //创建一个ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        //获取AutoCompleteTextView对象

        //将AutoCompleteTextView与ArrayAdapter进行绑定
        autoCompleteTextView.setAdapter(adapter);
        //设置AutoCompleteTextView输入1个字符就进行提示
        autoCompleteTextView.setThreshold(2);

    }
*/
    public void setSoftInputShow(final EditText editText) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 400);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mSearchBox.getText().length() > 0) {
            mTextClear.setVisibility(View.VISIBLE);
        } else {
            mTextClear.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

    }


   /* @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.text_clear:
                mSearchBox.setText("");
                break;
        }
    }*/
}
