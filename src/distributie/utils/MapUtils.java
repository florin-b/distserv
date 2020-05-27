package distributie.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import distributie.beans.Address;
import distributie.beans.CoordonateGps;
import distributie.beans.GoogleContext;

public class MapUtils {

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static CoordonateGps geocodeAddress(Address address) throws Exception {
		CoordonateGps coordonateGps = null;

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreet() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getStreet());
			strAddress.append(",");
		}

		if (address.getNumber() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getNumber());
			strAddress.append(",");
		}

		if (address.getSector() != null && !address.getSector().equals("")) {
			strAddress.append(address.getSector());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().equals("")) {
			strAddress.append(address.getCity());
			strAddress.append(",");
		}

		strAddress.append(address.getCountry());

		GeoApiContext context = GoogleContext.getContext();

		GeocodingResult[] results = GeocodingApi.geocode(context, strAddress.toString()).await();

		latitude = results[0].geometry.location.lat;
		longitude = results[0].geometry.location.lng;

		coordonateGps = new CoordonateGps(latitude, longitude);

		return coordonateGps;
	}

}
