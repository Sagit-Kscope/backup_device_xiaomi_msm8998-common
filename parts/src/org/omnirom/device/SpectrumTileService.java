package org.omnirom.device;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.preference.PreferenceManager;

import org.omnirom.device.Preference.SpectrumSwitchPreference;

import static org.omnirom.device.Preference.SpectrumPreference.FEATURE;
import static org.omnirom.device.Preference.SpectrumPreference.SPECTRUM_PREFERENCE_ADD_QS_TILE;

/**
 * TileService for spectrum profile switch
 * <p>
 * Created by 0ranko0P <ranko0p@outlook.com> on 2020.01.10
 */
public final class SpectrumTileService extends TileService {

    private Tile mTile;
    private SharedPreferences sp;

    private String[] profiles;

    /**
     * Update the profile only when the user closes the QS settings.
     * This will ensure the profile update only happen once and
     * avoid unnecessary I/O operation.
     *
     * @see SpectrumTileService#onStopListening()
     */
    private int finalProfile = -1;
    private int oldProfile;

    @Override
    public IBinder onBind(Intent intent) {
        profiles = getResources().getStringArray(R.array.spectrum_profiles);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // disable prompt dialog on preference
        if (sp.getBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, true))
            sp.edit().putBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, false).apply();
        return super.onBind(intent);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        mTile = getQsTile();
        mTile.setIcon(Icon.createWithResource(this, R.drawable.ic_spectrum));

        if (!SpectrumSwitchPreference.isEnabled(sp)) {
            mTile.setState(Tile.STATE_UNAVAILABLE);
            mTile.updateTile();
        } else {
            oldProfile = getCurrentProfile();
            mTile.setState(Tile.STATE_ACTIVE);
            mTile.setLabel(profiles[oldProfile]);
        }
        mTile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        mTile.setLabel(profiles[switchProfile()]);
        mTile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        if (finalProfile != -1 && finalProfile != oldProfile) {
            String newProfile = finalProfile + "";
            if (FEATURE.applyValue(newProfile))
                FEATURE.applySharedPreferences(newProfile, sp);
        }
    }

    private int switchProfile() {
        // make it loop
        final int current = (finalProfile == -1) ? oldProfile : finalProfile;
        finalProfile = (current == profiles.length - 1) ? 0 : current + 1;
        return finalProfile;
    }

    private int getCurrentProfile() {
        return Integer.parseInt(FEATURE.getCurrentValue());
    }
}
