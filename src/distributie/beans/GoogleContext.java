package distributie.beans;

import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;

import distributie.utils.Constants;

public class GoogleContext {

	private static GeoApiContext instance;

	private GoogleContext() {

	}

	public static GeoApiContext getContext() {
		if (instance == null) {
			instance = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
			instance.setQueryRateLimit(2);
			instance.setRetryTimeout(0, TimeUnit.SECONDS);
			instance.setConnectTimeout(1, TimeUnit.SECONDS);
		}

		return instance;
	}

}
