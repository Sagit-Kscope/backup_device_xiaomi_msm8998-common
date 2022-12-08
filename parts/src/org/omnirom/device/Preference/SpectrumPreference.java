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

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.util.AttributeSet;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import org.omnirom.device.R;
import org.omnirom.device.SpectrumTileService;

import java.io.File;

public final class SpectrumPreference extends ListPreference implements
        Preference.OnPreferenceChangeListener {

    /**
     * A boolean value stored in sp indicates that the user has tried the new qs tile or not.
     *
     * @see     SpectrumPreference#onClick()
     * @see     SpectrumTileService#onTileAdded()
     * */
    public static final String SPECTRUM_PREFERENCE_ADD_QS_TILE = "spec_qs";

    public static final String PREFERENCE_KEY = "spectrum";

    private static final String SPECTRUM_DEFAULT_PROFILE = "0";
    private static final String SPECTRUM_PROPERTY = "vendor.spectrum.profile";

    private static final File SPECTRUM_INIT_FILE = new File("/vendor/etc/init/init.spectrum.rc");
    
    public static final KernelFeature<String> FEATURE = new KernelFeature<String>() {

        @Override
        public boolean isSupported() {
            return SPECTRUM_INIT_FILE.exists();
        }

        @Override
        public String getCurrentValue() {
            return SystemProperties.get(SPECTRUM_PROPERTY, "-1");
        }

        @Override
        public boolean applyValue(String newValue) {
            if (getCurrentValue().equals(newValue)) return true;

            SystemProperties.set(SPECTRUM_PROPERTY, newValue);
            return newValue.equals(getCurrentValue());
        }

        @Override
        public void applySharedPreferences(String newValue, SharedPreferences sp) {
            sp.edit().putString(PREFERENCE_KEY, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if(!isSupported()) return false;
            String value = sp.getString(PREFERENCE_KEY, SPECTRUM_DEFAULT_PROFILE);
            return FEATURE.applyValue(value);
        }
    };

    public SpectrumPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FEATURE.isSupported()) {
            setOnPreferenceChangeListener(this);
        } else {
            setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        if (FEATURE.applyValue(value))
            FEATURE.applySharedPreferences(value, getSharedPreferences());

        return true;
    }

    @Override
    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        setVisible(!disableDependent);
    }

    @Override
    protected void onClick() {
        SharedPreferences sp = getSharedPreferences();

        boolean showPrompts = sp.getBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, true);
        if (showPrompts) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.specturm_qs_prompt_title)
                    .setMessage(R.string.specturm_qs_prompt_text)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        sp.edit().putBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, false).apply();
                        dialog.dismiss();
                        super.onClick();
                    }).show();
        } else {
            super.onClick();
        }
    }
}
