package io.alcatraz.xperiacameraremapping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import androidx.annotation.Nullable;

public class MainFragment extends PreferenceFragment {
    private ListPreference mLightPressFunctionSelector, mHeavyPressFunctionSelector;
    private PreferenceScreen mLightPressTouchSimulation, mHeavyPressTouchSimulation;

    SharedPreferences mPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences
                = getContext().getSharedPreferences("camera_button_mapper", Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.preference_main);
        findPreferences();
        bindListeners();
        updateSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSummary();
    }

    public void updateSummary() {
        String heavyValue = mPreferences.getString(Constants.PREF_HEAVY_PRESS_FUNCTION_SELECTOR,
                Constants.DEFAULT_VALUE_PREF_HEAVY_PRESS_FUNCTION_SELECTOR);
        String[] heavyEntryValue
                = getContext().getResources().getStringArray(R.array.entryvalue_for_heavy_function_selector);
        String[] heavyEntry
                = getContext().getResources().getStringArray(R.array.entries_for_heavy_function_selector);
        for (int i = 0; i < heavyEntryValue.length; i++) {
            if (heavyEntryValue[i].equals(heavyValue)) {
                mHeavyPressFunctionSelector.setSummary(heavyEntry[i]);
            }
        }

        String lightValue = mPreferences.getString(Constants.PREF_LIGHT_PRESS_FUNCTION_SELECTOR,
                Constants.DEFAULT_VALUE_PREF_LIGHT_PRESS_FUNCTION_SELECTOR);
        String[] lightEntryValue
                = getContext().getResources().getStringArray(R.array.entryvalue_for_light_function_selector);
        String[] lightEntry
                = getContext().getResources().getStringArray(R.array.entries_for_light_function_selector);
        for (int i = 0; i < lightEntryValue.length; i++) {
            if (lightEntryValue[i].equals(lightValue)) {
                mLightPressFunctionSelector.setSummary(lightEntry[i]);
            }
        }
    }

    private void findPreferences() {
        mHeavyPressFunctionSelector
                = (ListPreference) findPreference(Constants.PREF_HEAVY_PRESS_FUNCTION_SELECTOR);
        mHeavyPressTouchSimulation
                = (PreferenceScreen) findPreference(Constants.PREF_HEAVY_PRESS_TOUCH_SIMULATION);
        mLightPressFunctionSelector
                = (ListPreference) findPreference(Constants.PREF_LIGHT_PRESS_FUNCTION_SELECTOR);
        mLightPressTouchSimulation
                = (PreferenceScreen) findPreference(Constants.PREF_LIGHT_PRESS_TOUCH_SIMULATION);
    }

    private void bindListeners() {
        mLightPressFunctionSelector.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mPreferences.edit()
                        .putString(Constants.PREF_LIGHT_PRESS_FUNCTION_SELECTOR, (String) newValue).apply();
                updateSummary();
                MainService.updatePreferenceStatic();
                return true;
            }
        });

        mHeavyPressFunctionSelector.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mPreferences.edit()
                        .putString(Constants.PREF_HEAVY_PRESS_FUNCTION_SELECTOR, (String) newValue).apply();
                updateSummary();
                MainService.updatePreferenceStatic();
                return true;
            }
        });

        mLightPressTouchSimulation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(
                        new Intent(getContext(), CoordinateActivity.class)
                                .setAction(CoordinateActivity.TYPE_LIGHT_PICK));
                return true;
            }
        });

        mHeavyPressTouchSimulation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getContext(), CoordinateActivity.class)
                        .setAction(CoordinateActivity.TYPE_HEAVY_PICK));
                return true;
            }
        });
    }
}
