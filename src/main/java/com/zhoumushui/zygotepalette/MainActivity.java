package com.zhoumushui.zygotepalette;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textMuted;
    private TextView textVibrant;
    private TextView textDarkMuted;
    private TextView textDarkVibrant;
    private TextView textLightMuted;
    private TextView textLightVibrant;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialLayout();

        bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.test_zxq))).
                getBitmap();
        paletteBitmap(bitmap);
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
}
