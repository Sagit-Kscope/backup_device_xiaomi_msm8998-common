package org.omnirom.device.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import static org.omnirom.device.Preference.SpectrumPreference.SPECTRUM_DEFAULT_PROFILE;
import static org.omnirom.device.Preference.SpectrumPreference.SPECTRUM_SYSTEM_PROPERTY;

public final class SpectrumSwitchPreference extends SwitchPreference implements
        Preference.OnPreferenceChangeListener {

    public static final String PREFERENCE_KEY = "pref_spectrum_enabled";

    public static final String PREFERENCE_SWITCH_OFF = Boolean.FALSE.toString();

    public static final KernelFeature<Boolean> FEATURE = new KernelFeature<Boolean>() {

        @Override
        public boolean isSupported() {
            return SpectrumPreference.FEATURE.isSupported();
        }

        @Override
        public Boolean getCurrentValue() {
            return !PREFERENCE_SWITCH_OFF.equals(SpectrumPreference.FEATURE.getCurrentValue());
        }

        @Override
        public boolean applyValue(Boolean enabled) {
            if (!enabled) {
                SystemProperties.set(SPECTRUM_SYSTEM_PROPERTY, PREFERENCE_SWITCH_OFF);
            }
            return true;
        }

        @Override
        public void applySharedPreferences(Boolean enabled, SharedPreferences sp) {
            sp.edit().putBoolean(PREFERENCE_KEY, enabled).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if (isEnabled(sp)) {
                return SpectrumPreference.FEATURE.restore(sp);
            } else {
                return applyValue(false);
            }
        }
    };

    public SpectrumSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
        boolean result;
        if (enabled) {
            result =  SpectrumPreference.restore(getSharedPreferences(), false);
        } else {
            result =  FEATURE.applyValue(false);
        }
        notifyDependencyChange(enabled);
        return result;
    }

    public static boolean isEnabled(SharedPreferences sp) {
        return sp.getBoolean(PREFERENCE_KEY, true);
    }
}
