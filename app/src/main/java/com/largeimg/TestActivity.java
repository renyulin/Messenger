package com.largeimg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.largeimg.pic.LargeImageViewActivity;
import com.largeimg.pic.LargeImageViewSimpleBak;

public class TestActivity extends Activity {
    Toast mtoast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        mtoast = Toast.makeText(this, "A", Toast.LENGTH_SHORT);
    }

    private int i;

    public void testClick(View view) {
        switch (view.getId()) {
            case R.id.test_btn1:
                startActivity(new Intent(this, LargeImageViewActivity.class));
                break;
            case R.id.test_btn2:
                startActivity(new Intent(this, LargeImageViewSimpleBak.class));
                break;
        }
    }
}
