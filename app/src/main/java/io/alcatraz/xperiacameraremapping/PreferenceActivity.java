package io.alcatraz.xperiacameraremapping;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class PreferenceActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perference);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.preference_act_fragment_container, new MainFragment())
                .commit();
    }
}
