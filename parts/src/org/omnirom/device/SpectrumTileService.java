package org.omnirom.device;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.preference.PreferenceManager;

import static org.omnirom.device.Preference.SpectrumPreference.FEATURE;
import static org.omnirom.device.Preference.SpectrumPreference.SPECTRUM_PREFERENCE_ADD_QS_TILE;

/**
 * TileService for spectrum profile switch
 *
 * Created by 0ranko0P <ranko0p@outlook.com> on 2020.01.10
 * */
public final class SpectrumTileService extends TileService {

    private Tile mTile;

    private  String[] profiles;

    /**
     * Update the profile only when the user closes the QS settings.
     * This will ensure the profile update only happen once and
     * avoid unnecessary I/O operation.
     *
     * @see SpectrumTileService#onStopListening()
     * */
    private int finalProfile = -1;
    private int oldProfile;

    @Override
    public IBinder onBind(Intent intent) {
        profiles = getResources().getStringArray(R.array.spectrum_profiles);
        return super.onBind(intent);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // disable prompt dialog on preference
        if (sp.getBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, true))
            sp.edit().putBoolean(SPECTRUM_PREFERENCE_ADD_QS_TILE, false).apply();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        oldProfile = getCurrentProfile();

        mTile = getQsTile();
        mTile.setState(Tile.STATE_ACTIVE);
        mTile.setIcon(Icon.createWithResource(this, R.drawable.ic_spectrum));

        mTile.setLabel(profiles[getCurrentProfile()]);
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
                FEATURE.applySharedPreferences(newProfile, PreferenceManager.getDefaultSharedPreferences(this));
        }
    }

    private int switchProfile() {
        // make it loop
        final int current = (finalProfile == -1) ? oldProfile : finalProfile;
        finalProfile = (current == profiles.length - 1) ? 0 : current + 1;
        return finalProfile;
    }

    private int getCurrentProfile(){
        return Integer.valueOf(FEATURE.getCurrentValue());
    }
}
