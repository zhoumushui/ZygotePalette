package com.zhoumushui.zygotepalette;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoumushui.zygotepalette.util.HintUtil;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    private TextView textMuted;
    private TextView textVibrant;
    private TextView textDarkMuted;
    private TextView textDarkVibrant;
    private TextView textLightMuted;
    private TextView textLightVibrant;

    private ImageView imageTest;
    private Bitmap bitmap;

    private static final int REQUEST_PICK_IMAGE = 0x31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        initialLayout();

        bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.test_zxq))).
                getBitmap();
        paletteBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uriSelectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
            Cursor cursor = getContentResolver().query(uriSelectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            int width = cursor.getInt(cursor.getColumnIndex(filePathColumn[1]));
            int height = cursor.getInt(cursor.getColumnIndex(filePathColumn[2]));
            cursor.close();
            if (width > 0 && height > 0) {
            } else {
                width = 800;
                height = 800;
            }
            bitmap = pathToBitmap(path, width, height); // path:/storage/emulated/0/DCIM/test.png

            if (bitmap != null) {
                imageTest.setImageBitmap(bitmap);
                paletteBitmap(pathToBitmap(path, width, height));
            } else
                HintUtil.showToast(context, "bitmap is NULL");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pick:
                pickImageAndPalette();
                break;
        }
        return true;
    }

    private void initialLayout() {
        textMuted = (TextView) findViewById(R.id.textMuted);
        textVibrant = (TextView) findViewById(R.id.textVibrant);
        textDarkMuted = (TextView) findViewById(R.id.textDarkMuted);
        textDarkVibrant = (TextView) findViewById(R.id.textDarkVibrant);
        textLightMuted = (TextView) findViewById(R.id.textLightMuted);
        textLightVibrant = (TextView) findViewById(R.id.textLightVibrant);

        imageTest = (ImageView) findViewById(R.id.imageTest);

        FloatingActionButton floatActionPalette = (FloatingActionButton)
                findViewById(R.id.floatActionPalette);
        floatActionPalette.setOnClickListener(myOnClickListener);
    }

    private void pickImageAndPalette() {
        int permissionWrite = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    PERMISSION_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent intentPick = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentPick, REQUEST_PICK_IMAGE);
        }
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatActionPalette:
                    pickImageAndPalette();
                    break;
            }
        }
    };


    private void paletteBitmap(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                updateTextBySwatch("Muted", textMuted, mutedSwatch);

                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                updateTextBySwatch("Vibrant", textVibrant, vibrantSwatch);

                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                updateTextBySwatch("DarkMuted", textDarkMuted, darkMutedSwatch);

                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                updateTextBySwatch("DarkVibrant", textDarkVibrant, darkVibrantSwatch);

                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
                updateTextBySwatch("LightMuted", textLightMuted, lightMutedSwatch);

                Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                updateTextBySwatch("LightVibrant", textLightVibrant, lightVibrantSwatch);
            }
        });
    }

    private void updateTextBySwatch(String title, TextView textView, Palette.Swatch swatch) {
        if (swatch != null) {
            textView.setBackgroundColor(swatch.getRgb());
            textView.setText(title + "\n[RGB:#" + Integer.toHexString(swatch.getRgb())
                    + "]\t[Population:" + swatch.getPopulation() + "]");
        } else
            textView.setText(title + " swatch is NULL");
    }

    /**
     * 从图片路径获取Bitmap
     *
     * @param path
     * @param wantWidth
     * @param wantHeight
     * @return
     */
    public Bitmap pathToBitmap(String path, int wantWidth, int wantHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置为ture只获取图片大小
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, options); // 返回为空
        int realWidth = options.outWidth;
        int readlHeight = options.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (realWidth > wantWidth || readlHeight > wantHeight) { // 缩放
            scaleWidth = ((float) realWidth) / wantWidth;
            scaleHeight = ((float) readlHeight) / wantHeight;
        }
        options.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        options.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeFile(path, options));
        return Bitmap.createScaledBitmap(weak.get(), wantWidth, wantHeight, true);
    }

}
