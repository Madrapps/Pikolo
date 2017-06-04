package com.madrapps.pickcolor;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HSLColorPicker colorPicker = (HSLColorPicker) findViewById(R.id.colorPicker);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        });
    }
}
