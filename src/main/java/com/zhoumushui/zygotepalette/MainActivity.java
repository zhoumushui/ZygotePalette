package com.zhoumushui.zygotepalette;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private TextView textMuted;
    private TextView textVibrant;
    private TextView textDarkMuted;
    private TextView textDarkVibrant;
    private TextView textLightMuted;
    private TextView textLightVibrant;

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
            // path:/storage/emulated/0/DCIM/test.png
            textMuted.setText(width + "x" + height + "path:" + path);
//            paletteBitmap(pathToBitmap(path, width, height));
//            paletteBitmap(BitmapFactory.decodeFile("file://" + path));
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
                Intent intentPick = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentPick, REQUEST_PICK_IMAGE);
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

        FloatingActionButton floatActionPalette = (FloatingActionButton)
                findViewById(R.id.floatActionPalette);
        floatActionPalette.setOnClickListener(myOnClickListener);
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatActionPalette:
                    if (bitmap != null)
                        paletteBitmap(bitmap);
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

    public Bitmap pathToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true; // 设置为ture只获取图片大小
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts); // 返回为空
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) { // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

}
