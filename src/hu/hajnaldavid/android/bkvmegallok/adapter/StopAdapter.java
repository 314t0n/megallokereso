package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StopAdapter extends ArrayAdapter<Route> {

	private int selectedPosition;

	public interface OnRouteSelectedListener {
		public void onRouteSelected(Route selectedRoute, Stop selectedStop);
	}

	private OnRouteSelectedListener routeListener;

	private class OnItemClickListener implements OnClickListener {
		private final int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View v) {
			selectedPosition = mPosition;
			if (routeListener != null && !mObjects.isEmpty()) {
				// Route r = mObjects.get(mPosition);
				// r.setStopID(stop.getId());
				// r.setStopName(stop.getStopName());
				routeListener.onRouteSelected(mObjects.get(mPosition), stop);
			}
			Log.v("ROUTE ITEM CLICKED ", "onItemClick at position" + mPosition);
		}
	}

	List<Route> mObjects;
	Stop stop;

	public StopAdapter(Context context, int textViewResourceId,
			List<Route> objects, Stop selectedStop) {
		super(context, textViewResourceId, objects);
		mObjects = objects;
		routeListener = (OnRouteSelectedListener) context;
		stop = selectedStop;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.stopdetailerrow, null);
		} else {
			v = convertView;
		}
		Route route = mObjects.get(position);
		// ((TextView) v.findViewById(R.id.routeId)).setText(b.getNumber());
		((TextView) v.findViewById(R.id.routeId)).setText(route.getID());
		((TextView) v.findViewById(R.id.routeId))
				.setOnClickListener(new OnItemClickListener(position));
		;
		((TextView) v.findViewById(R.id.routeDepartureTime)).setText(route
				.getDepartureTimeAsString());

		((TextView) v.findViewById(R.id.routeDestination)).setText(route
				.getDestination());

		((TextView) v.findViewById(R.id.routeId)).setBackgroundResource(Helper
				.getRouteTypeColor(route.getType(), route.getID()));

		if (!route.isWheelchair()) {
			((ImageView) v.findViewById(R.id.wheelchairStopRow))
					.setVisibility(View.INVISIBLE);
		} else {
			System.out.println("happen");
		}
		return v;
	}
}
