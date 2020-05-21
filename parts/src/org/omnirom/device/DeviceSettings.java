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

package org.omnirom.device;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;

import org.omnirom.device.Preference.ButtonSwapPreference;
import org.omnirom.device.Preference.FastChargePreference;
import org.omnirom.device.Preference.S2SVibratorStrengthPreference;
import org.omnirom.device.Preference.SpectrumSwitchPreference;
import org.omnirom.device.Preference.SweepToSleepPreference;

import static org.omnirom.device.Preference.ButtonSwapPreference.BUTTONS_SWAP_KEY;
import static org.omnirom.device.Preference.FastChargePreference.USB_FAST_CHARGE_KEY;
import static org.omnirom.device.Preference.S2SVibratorStrengthPreference.KEY_S2S_VIBSTRENGTH;
import static org.omnirom.device.Preference.SweepToSleepPreference.S2S_KEY;

public final class DeviceSettings extends PreferenceFragment {

    private static final String KEY_CATEGORY_DISPLAY = "display";
    private static final String KEY_CATEGORY_KCAL = "kcal";
    private static final String KEY_CATEGORY_HW_BUTTONS = "hw_buttons";
    private static final String KEY_CATEGORY_USB_FASTCHARGE = "usb_fastcharge";

    private final String KEY_DEVICE_DOZE = "device_doze";
    private final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.lineageos.settings.doze";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        PreferenceScreen prefSet = getPreferenceScreen();

        ButtonSwapPreference mButtonSwap = (ButtonSwapPreference) prefSet.findPreference(BUTTONS_SWAP_KEY);
        FastChargePreference mFastCharge = (FastChargePreference) findPreference(USB_FAST_CHARGE_KEY);
        SweepToSleepPreference mSweep = (SweepToSleepPreference) findPreference(S2S_KEY);
        S2SVibratorStrengthPreference mVibratorStrengthS2S = (S2SVibratorStrengthPreference) findPreference(KEY_S2S_VIBSTRENGTH);

        mButtonSwap.setEnabled(ButtonSwapPreference.FEATURE.isSupported());
        mFastCharge.setEnabled(FastChargePreference.FEATURE.isSupported());
        mSweep.setEnabled(SweepToSleepPreference.FEATURE.isSupported());
        mVibratorStrengthS2S.setEnabled(S2SVibratorStrengthPreference.FEATURE.isSupported());

        findPreference(KEY_CATEGORY_KCAL).setEnabled(DisplayCalibration.isSupported());

        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            PreferenceCategory displayCategory = findPreference(KEY_CATEGORY_DISPLAY);
            displayCategory.removePreference(findPreference(KEY_DEVICE_DOZE));
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (KEY_CATEGORY_KCAL.equals(preference.getKey())) {
            DisplayCalibrationActivity.startActivity(getContext());
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }
}