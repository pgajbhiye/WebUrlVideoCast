package com.orion.videourlcast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;

import java.util.List;

public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {

        CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setExpandedControllerActivityClassName(ExpandedControlActivity.class.getName()).build();

        return new CastOptions.Builder()
                .setReceiverApplicationId(context.getString(R.string.app_id)) //If using your own custom receiver, replace this id with your appid registered on Cast SDK Console
                .setEnableReconnectionService(true)
                .setResumeSavedSession(true)
                .setCastMediaOptions(mediaOptions)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
