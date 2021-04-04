package io.alcatraz.xperiacameraremapping;

import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;

public class MainService extends AccessibilityService {
    public static MainService mService;

    private float mHeavyX, mHeavyY, mLightX, mLightY = 0;
    private static String mLightFunction, mHeavyFunction;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        updatePreference();
        mService = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
        mService = null;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int toApplyKeyCode = -1;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_FOCUS:
                switch (mLightFunction) {
                    case "no_override":
                        return false;
                    case "home":
                        toApplyKeyCode = KeyEvent.KEYCODE_HOME;
                        break;
                    case "task_screen":
                        toApplyKeyCode = KeyEvent.KEYCODE_MENU;
                        break;
                    case "back":
                        toApplyKeyCode = KeyEvent.KEYCODE_BACK;
                        break;
                    case "touch_simulation":
                        dispatchTouchEvent(mLightX, mLightY);
                        return true;
                    case "same_with_heavy_press":
                        break;
                }

                if (toApplyKeyCode != -1) {
                    dispatchKeyEvent(toApplyKeyCode);
                    return true;
                }
            case KeyEvent.KEYCODE_CAMERA:
                switch (mHeavyFunction) {
                    case "no_override":
                        return false;
                    case "home":
                        toApplyKeyCode = KeyEvent.KEYCODE_HOME;
                        break;
                    case "task_screen":
                        toApplyKeyCode = KeyEvent.KEYCODE_MENU;
                        break;
                    case "back":
                        toApplyKeyCode = KeyEvent.KEYCODE_BACK;
                        break;
                    case "touch_simulation":
                        dispatchTouchEvent(mHeavyX, mHeavyY);
                        return true;
                }
                if (toApplyKeyCode != -1) {
                    dispatchKeyEvent(toApplyKeyCode);
                    return true;
                }
                break;
        }
        return super.onKeyEvent(event);
    }

    @SuppressWarnings("ConstantConditions")
    public void updatePreference() {
        SharedPreferences mPreference = getSharedPreferences("camera_button_mapper", Context.MODE_PRIVATE);
        mLightFunction = mPreference.getString(Constants.PREF_LIGHT_PRESS_FUNCTION_SELECTOR,
                Constants.DEFAULT_VALUE_PREF_LIGHT_PRESS_FUNCTION_SELECTOR);
        mHeavyFunction = mPreference.getString(Constants.PREF_HEAVY_PRESS_FUNCTION_SELECTOR,
                Constants.DEFAULT_VALUE_PREF_HEAVY_PRESS_FUNCTION_SELECTOR);
        String heavyPosition = mPreference.getString(Constants.PREF_HEAVY_PRESS_TOUCH_SIMULATION_POSITION,
                Constants.DEFAULT_VALUE_PREF_HEAVY_PRESS_TOUCH_SIMULATION_POSITION);
        String lightPosition = mPreference.getString(Constants.PREF_LIGHT_PRESS_TOUCH_SIMULATION_POSITION,
                Constants.DEFAULT_VALUE_PREF_LIGHT_PRESS_TOUCH_SIMULATION_POSITION);
        String[] heavyProcess = heavyPosition.split(",");
        mHeavyX = Float.parseFloat(heavyProcess[0]);
        mHeavyY = Float.parseFloat(heavyProcess[1]);
        String[] lightProcess = lightPosition.split(",");
        mLightX = Float.parseFloat(lightProcess[0]);
        mLightY = Float.parseFloat(lightProcess[1]);
    }

    public static void updatePreferenceStatic() {
        if (mService != null) {
            mService.updatePreference();
        }
    }

    private void dispatchKeyEvent(final int keycode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation mInst = new Instrumentation();
                mInst.sendKeyDownUpSync(keycode);
            }
        }).start();
    }

    private void dispatchTouchEvent(final float x, final float y) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation mInst = new Instrumentation();
                mInst.sendPointerSync(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
                mInst.sendPointerSync(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
            }
        }).start();
    }
}
