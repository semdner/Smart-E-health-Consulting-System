package com.ehealthsystem.map;

import com.google.maps.GeoApiContext;

public class Context {
    static GeoApiContext context = null;

    static GeoApiContext getContext() {
        if (context != null)
            return context;

        // set API Key
        context = new GeoApiContext.Builder().apiKey("API_KEY").queryRateLimit(1000).build();
        return context;
    }
}
