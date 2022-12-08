package org.omnirom.device.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import org.omnirom.device.R;

public final class SpectrumSwitchPreference extends SwitchPreference implements
        Preference.OnPreferenceChangeListener {

    private static final String PREFERENCE_KEY = "pref_spectrum_enabled";

    public static final KernelFeature<Boolean> FEATURE = new KernelFeature<Boolean>() {

        @Override
        public boolean isSupported() {
            return SpectrumPreference.FEATURE.isSupported();
        }

        @Override
        public Boolean getCurrentValue() {
            throw new IllegalStateException();
        }

        @Override
        public boolean applyValue(Boolean enabled) {
            throw new IllegalStateException();
        }

        @Override
        public void applySharedPreferences(Boolean enabled, SharedPreferences sp) {
            sp.edit().putBoolean(PREFERENCE_KEY, enabled).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if (SpectrumSwitchPreference.isEnabled(sp)) {
                return SpectrumPreference.FEATURE.restore(sp);
            } else {
                return true;
            }
        }
    };

    public SpectrumSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FEATURE.isSupported()) {
            setOnPreferenceChangeListener(this);
        } else {
            setEnabled(false);
            setSummary(context.getString(R.string.app_feature_unsupported));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
        if (enabled) {
            SpectrumPreference.FEATURE.restore(getSharedPreferences());
        }
        notifyDependencyChange(enabled);
        return true;
    }

    public static boolean isEnabled(SharedPreferences sp) {
        return sp.getBoolean(PREFERENCE_KEY, true);
    }
}
