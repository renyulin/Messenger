package com.largeimg.pic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.largeimg.R;
import com.largeimg.pic.view.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageViewSimpleBak extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);
        large();
        mImageView = (ImageView) findViewById(R.id.id_imageview);
        mImageView.setVisibility(View.VISIBLE);
        try {
            InputStream inputStream = getAssets().open("tangyan.jpg");
            //获得图片的宽、高
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, tmpOptions);
            int width = tmpOptions.outWidth;
            int height = tmpOptions.outHeight;

            //设置显示图片的中心区域
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(width / 2 - 100, height / 2 - 100,
                    width / 2 + 100, height / 2 + 100), options);
            mImageView.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void large() {
        LargeImageView mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        mLargeImageView.setVisibility(View.GONE);
//        try {
//            InputStream inputStream = getAssets().open("qm.jpg");
//            mLargeImageView.setInputStream(inputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
