package hu.hajnaldavid.android.bkvmegallok.util;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Helper {

	public static Integer getRouteStringNumber(int number) {
		Integer returnNumber = 0;
		switch (number) {
		case 0:
			returnNumber = R.string.routes_tram;
			break;
		case 1:
			returnNumber = R.string.routes_metro;
			break;
		case 2:
			returnNumber = R.string.routes_rail;
			break;
		case 3:
			returnNumber = R.string.routes_bus;
			break;
		case 4:
			returnNumber = R.string.routes_ferry;
			break;
		case 7:
			returnNumber = R.string.routes_funicular;
			break;
		}

		return returnNumber;
	}

	public static Integer getRouteTypeColor(Route.Type type, String id) {

		Integer returnValue = null;

		if (null != type) {

			if (type.getNumber() == Route.Type.Tram.getNumber()) {
				returnValue = R.color.stoprow_bg_tram;
			} else if (type.getNumber() == Route.Type.Bus.getNumber()) {
				returnValue = R.color.stoprow_bg_bus;
			} else if (type.getNumber() == Route.Type.Metro.getNumber()) {
				if (id.equals("M1")) {
					returnValue = R.color.stoprow_bg_metro1;
				} else if (id.equals("M2")) {
					returnValue = R.color.stoprow_bg_metro2;
				} else if (id.equals("M3")) {
					returnValue = R.color.stoprow_bg_metro3;
				}
			} else if (type.getNumber() == Route.Type.Ferry.getNumber()) {
				returnValue = R.color.stoprow_bg_ferry;
			} else if (type.getNumber() == Route.Type.Funicular.getNumber()) {
				returnValue = R.color.stoprow_bg_funicular;
			} else if (type.getNumber() == Route.Type.Rail.getNumber()) {

				if (id.equals("H5")) {
					returnValue = R.color.stoprow_bg_hev5;
				} else if (id.equals("H6")) {
					returnValue = R.color.stoprow_bg_hev6;
				} else if (id.equals("H7")) {
					returnValue = R.color.stoprow_bg_hev7;
				} else if (id.equals("H8")) {
					returnValue = R.color.stoprow_bg_hev8_9;
				} else if (id.equals("H9")) {
					returnValue = R.color.stoprow_bg_hev8_9;
				}
			} else {
				returnValue = R.color.stoprow_bg_bus;
			}
		} else {
			returnValue = R.color.stoprow_bg_bus;
		}

		return returnValue;

	}

	public static String createQueryString(String url,
			Map<String, String> keyValues) {

		StringBuilder qs = new StringBuilder();

		qs.append(url);

		if (!keyValues.isEmpty()) {

			qs.append("?");
			Iterator<Entry<String, String>> it = keyValues.entrySet()
					.iterator();
			while (it.hasNext()) {

				Entry<String, String> pairs = it.next();

				qs.append(pairs.getKey());
				qs.append("=");
				qs.append(pairs.getValue());

				if (it.hasNext()) {
					qs.append("&");
				}

			}

		}

		return qs.toString();

	}

}
