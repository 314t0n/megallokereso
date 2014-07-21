package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import hu.hajnaldavid.android.bkvmegallok.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StopsAdapter extends BaseExpandableListAdapter {

	public interface OnAddFavListener {
		public void onAddFav(Stop stopID);
	}

	public interface OnStopSelectedListener {
		public void onStopSelected(Stop selectedStop);
	}

	private OnAddFavListener addFavListener;
	private int selectedPosition;
	private AlertDialog.Builder builder;

	private class OnFavItemClickListener implements OnClickListener {
		private final int mPosition;
		private final AlertDialog.Builder builder;

		OnFavItemClickListener(int position, AlertDialog.Builder builder) {
			mPosition = position;
			this.builder = builder;
		}

		@Override
		public void onClick(View v) {
			selectedPosition = mPosition;
			this.builder
					.setMessage(
							context.getResources().getString(
									R.string.dialog_add_fav)
									+ "?")
					.setPositiveButton(
							context.getResources().getString(
									R.string.dialog_yes), dialogClickListener)
					.setNegativeButton(
							context.getResources()
									.getString(R.string.dialog_no),
							dialogClickListener).show();
		}
	}

	private OnStopSelectedListener stopListener;

	private class OnStopClickListener implements OnClickListener {
		private int mPosition;

		OnStopClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View v) {
			selectedPosition = mPosition;
			if (stopListener != null && !groups.isEmpty()) {
				stopListener.onStopSelected(groups.get(mPosition));
			}
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:

				addFavListener.onAddFav(groups.get(selectedPosition));
				Toast.makeText(
						context,
						context.getResources().getString(R.string.dialog_saved),
						Toast.LENGTH_SHORT).show();

				groups.get(selectedPosition).setFav(true);

				notifyDataSetChanged();

				break;

			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	private final Context context;
	private ArrayList<Stop> groups;

	public StopsAdapter(Context context, ArrayList<Stop> groups) {
		super();
		addFavListener = (OnAddFavListener) context;
		stopListener = (OnStopSelectedListener) context;
		builder = new AlertDialog.Builder(context);
		this.context = context;
		this.groups = groups;

	}

	public void addItem(Stop stop) {
		if (!groups.contains(stop)) {
			groups.add(stop);
		}
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		Stop stop = groups.get(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_stoprow, null);
		}

		int dist = (int) Math.round(stop.getDistance() * 1000);

		DecimalFormat df = new DecimalFormat("#.#");

		((TextView) convertView.findViewById(R.id.stopName)).setText(stop
				.getStopName());
		((TextView) convertView.findViewById(R.id.stopName))
				.setOnClickListener(new OnStopClickListener(groupPosition));
		((TextView) convertView.findViewById(R.id.stopDist)).setText(df
				.format(dist)
				+ " "
				+ convertView.getResources().getString(R.string.meter));

		ImageButton btn = (ImageButton) convertView
				.findViewById(R.id.stopAddFav);
		btn.setOnClickListener(new OnFavItemClickListener(groupPosition,
				this.builder));

		if (stop.isFav()) {
			btn.setImageDrawable(context.getResources().getDrawable(
					R.drawable.add_fav_icon));
		} else {
			btn.setImageDrawable(context.getResources().getDrawable(
					R.drawable.add_fav_icon_off));
		}

		if (stop.getDirection() == 0) {
			((ImageView) convertView.findViewById(R.id.stopStopsDir))
					.setImageDrawable(context.getResources().getDrawable(
							R.drawable.dir_0_icon));

		} else {
			((ImageView) convertView.findViewById(R.id.stopStopsDir))
					.setImageDrawable(context.getResources().getDrawable(
							R.drawable.dir_1_icon));
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Route route = groups.get(groupPosition).getRoutesAsList()
				.get(childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.child_routelistrow,
					null);
		}

		((TextView) convertView.findViewById(R.id.stopsRouteId)).setText(route
				.getID());
		((TextView) convertView.findViewById(R.id.stopsRouteId))
				.setBackgroundResource(Helper.getRouteTypeColor(
						route.getType(), route.getID()));
		((TextView) convertView.findViewById(R.id.stopsRouteDest))
				.setText(route.getDestination());

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getRoutesAsList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getRoutesAsList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}
}
