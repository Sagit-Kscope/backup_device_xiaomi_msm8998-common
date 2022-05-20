/*
 * Copyright (C) 2019 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.omnirom.device.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

public final class SpectrumPreference extends ListPreference implements
        Preference.OnPreferenceChangeListener {

    public static final String SPECTRUM_KEY = "spectrum";
    private static final String SPECTRUM_DEFAULT_PROFILE = "0";
    private static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    public static final KernelFeature<String> FEATURE = new KernelFeature<String>() {

        @Override
        public boolean isSupported() {
            return !TextUtils.isEmpty(SystemProperties.get(SPECTRUM_SYSTEM_PROPERTY, null));
        }

        @Override
        public String getCurrentValue() {
            return SystemProperties.get(SPECTRUM_SYSTEM_PROPERTY, SPECTRUM_DEFAULT_PROFILE);
        }

        @Override
        public boolean applyValue(String newValue) {
            SystemProperties.set(SPECTRUM_SYSTEM_PROPERTY, newValue);
            return newValue.equals(getCurrentValue());
        }

        @Override
        public void applySharedPreferences(String newValue, SharedPreferences sp) {
            sp.edit().putString(SPECTRUM_KEY, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if(!isSupported()) return false;

            String value = sp.getString(SPECTRUM_KEY, SPECTRUM_DEFAULT_PROFILE);
            return applyValue(value);
        }
    };

    public SpectrumPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        if (FEATURE.applyValue(value))
            FEATURE.applySharedPreferences(value, getSharedPreferences());

        return true;
    }
}
