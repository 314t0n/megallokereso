package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Megálló listában Járat lista
 */
public class RouteSimpleListAdapter extends ArrayAdapter<Route> {

	List<Route> routes;
	private Context myContext;

	public RouteSimpleListAdapter(Context context, int textViewResourceId,
			List<Route> objects) {
		super(context, textViewResourceId, objects);
		routes = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.routelist_stop, null);
		} else {
			v = convertView;
		}
		Route b = routes.get(position);

		Integer color = Helper.getRouteTypeColor(b.getType(), b.getID());

		((TextView) v.findViewById(R.id.routeSimpleListItem))
				.setText(b.getID());

		// v.setBackgroundColor(color);

		// ((TextView) v.findViewById(R.id.routeSimpleListItem))
		// .setBackgroundColor(myContext.getResources().getColor(color));

		// ((TextView) v.findViewById(R.id.routeSimpleListItem))
		// .setBackgroundColor(myContext.getResources().getColor(
		// R.color.stoprow_bg_bus));

		return v;
	}

}
