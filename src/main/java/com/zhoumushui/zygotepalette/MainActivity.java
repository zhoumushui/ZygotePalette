package com.zhoumushui.zygotepalette;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textMuted;
    private TextView textVibrant;
    private TextView textDarkMuted;
    private TextView textDarkVibrant;
    private TextView textLightMuted;
    private TextView textLightVibrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMuted = (TextView) findViewById(R.id.textMuted);
        textVibrant = (TextView) findViewById(R.id.textVibrant);
        textDarkMuted = (TextView) findViewById(R.id.textDarkMuted);
        textDarkVibrant = (TextView) findViewById(R.id.textDarkVibrant);
        textLightMuted = (TextView) findViewById(R.id.textLightMuted);
        textLightVibrant = (TextView) findViewById(R.id.textLightVibrant);

        Palette.from(((BitmapDrawable) (getResources().getDrawable(R.drawable.test_zxq))).
                getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                if (mutedSwatch != null) {
                    textMuted.setText("[MutedSwatch]\n" + mutedSwatch.toString());
                    textMuted.setBackgroundColor(mutedSwatch.getRgb());
                }
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                if (vibrantSwatch != null) {
                    textVibrant.setText("[VibrantSwatch]\n" + vibrantSwatch.toString());
                    textVibrant.setBackgroundColor(vibrantSwatch.getRgb());
                }
                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                if (darkMutedSwatch != null) {
                    textDarkMuted.setText("[DarkMutedSwatch]\n" +
                            darkMutedSwatch.toString());
                    textDarkMuted.setBackgroundColor(darkMutedSwatch.getRgb());
                }
                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                if (darkVibrantSwatch != null) {
                    textDarkVibrant.setText("[DarkVibrantSwatch]\n" +
                            darkVibrantSwatch.toString());
                    textDarkVibrant.setBackgroundColor(darkVibrantSwatch.getRgb());
                }
                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
                if (lightMutedSwatch != null) {
                    textLightMuted.setText("[LightMutedSwatch]\n" +
                            lightMutedSwatch.toString());
                    textLightMuted.setBackgroundColor(lightMutedSwatch.getRgb());
                }
                Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                if (textLightVibrant != null) {
                    textLightVibrant.setText("[LightVibrantSwatch]\n" +
                            lightVibrantSwatch.toString());
                    textLightVibrant.setBackgroundColor(lightVibrantSwatch.getRgb());
                }

            }
        });
    }
}
