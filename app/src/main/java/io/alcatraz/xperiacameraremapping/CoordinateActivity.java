package io.alcatraz.xperiacameraremapping;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CoordinateActivity extends Activity {
    public static final String TYPE_LIGHT_PICK = "type_light_pick";
    public static final String TYPE_HEAVY_PICK = "type_heavy_pick";

    private TextView mCoordinateIndicator;
    private ImageButton mConfirmButton;

    private String mType;
    private float mX, mY = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getAction();
        setContentView(R.layout.activity_coordinate);
        findViews();
        bindListeners();
    }

    private void findViews() {
        mCoordinateIndicator = findViewById(R.id.coordinate_indicator);
        mConfirmButton = findViewById(R.id.coordinate_confirm);
    }

    private void bindListeners() {
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mX = event.getRawX();
                mY = event.getRawY();

                mCoordinateIndicator.setText(String.format(getString(R.string.coordinate_text), mX, mY));
                return false;
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSave = mX + "," + mY;
                SharedPreferences mPreferences
                        = getSharedPreferences("camera_button_mapper", Context.MODE_PRIVATE);
                if (mType.equals(TYPE_HEAVY_PICK)) {
                    mPreferences.edit()
                            .putString(Constants.PREF_HEAVY_PRESS_TOUCH_SIMULATION_POSITION, toSave).apply();
                } else {
                    mPreferences.edit()
                            .putString(Constants.PREF_LIGHT_PRESS_TOUCH_SIMULATION_POSITION, toSave).apply();
                }
                finish();
            }
        });
    }
}
