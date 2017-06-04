package com.madrapps.pickcolor;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HSLColorPicker colorPicker;
    private ImageView imageView;
    private Button randomColorButton;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        colorPicker = (HSLColorPicker) findViewById(R.id.colorPicker);
        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                // Do whatever you want with the color
                imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        });

        initializeColorButtons();

        randomColorButton = (Button) findViewById(R.id.randomColorButton);
        randomColorButton.setOnClickListener(this);
    }

    private void initializeColorButtons() {
        findViewById(R.id.imageButton1).setOnClickListener(this);
        findViewById(R.id.imageButton2).setOnClickListener(this);
        findViewById(R.id.imageButton3).setOnClickListener(this);
        findViewById(R.id.imageButton4).setOnClickListener(this);
        findViewById(R.id.imageButton5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.randomColorButton){
            final int color = ColorUtils.HSLToColor(new float[]{random.nextInt(360), random.nextFloat(), random.nextFloat()});
            final String hexColor = String.format("#%06X", (0xFFFFFF & color));
            randomColorButton.setText(hexColor);
            randomColorButton.setBackgroundColor(color);
            imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            colorPicker.setColor(color);
        }
        if(v instanceof ImageButton){
            final int color = ((ColorDrawable) ((ImageButton) v).getDrawable()).getColor();
            imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            colorPicker.setColor(color);
        }
    }
}
