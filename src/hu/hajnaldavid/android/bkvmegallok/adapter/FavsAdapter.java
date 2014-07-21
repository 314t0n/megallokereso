package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FavsAdapter extends ArrayAdapter<Stop> {

	public interface OnDeleteFavListener {
		public void onFavDelete(String stopID);
	}

	private final OnDeleteFavListener deleteFavListener;
	private int selectedPosition;

	private class OnItemClickListener implements OnClickListener {
		private final int mPosition;
		private final AlertDialog.Builder builder;

		OnItemClickListener(int position, AlertDialog.Builder builder) {
			mPosition = position;
			this.builder = builder;
		}

		@Override
		public void onClick(View v) {
			selectedPosition = mPosition;
			this.builder
					.setMessage(
							context.getResources().getString(
									R.string.dialog_del_fav)
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

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				deleteFavListener.onFavDelete(stops.get(selectedPosition)
						.getId());
				stops.remove(selectedPosition);
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.dialog_deleted), Toast.LENGTH_SHORT)
						.show();
				notifyDataSetChanged();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	AlertDialog.Builder builder;
	List<Stop> stops;
	private final Context context;

	public FavsAdapter(Context context, int textViewResourceId,
			List<Stop> objects) {
		super(context, textViewResourceId, objects);
		deleteFavListener = (OnDeleteFavListener) context;
		builder = new AlertDialog.Builder(context);
		this.context = context;
		stops = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.favs_stoprow, null);
		} else {
			v = convertView;
		}

		Stop stop = stops.get(position);

		ImageButton btn = (ImageButton) v.findViewById(R.id.favsRouteDelete);
		btn.setOnClickListener(new OnItemClickListener(position, this.builder));

		((TextView) v.findViewById(R.id.favStopsListTitle)).setText(stop
				.getStopName());
		((TextView) v.findViewById(R.id.favsRoutes)).setText(stop
				.getRoutesAsString());

		if (stop.getDirection() == 0) {
			((ImageView) v.findViewById(R.id.favStopsDir))
					.setImageDrawable(context.getResources().getDrawable(
							R.drawable.dir_0_icon));

		} else {
			((ImageView) v.findViewById(R.id.favStopsDir))
					.setImageDrawable(context.getResources().getDrawable(
							R.drawable.dir_1_icon));
		}

		// v.setOnClickListener(new OnItemClickListener(position));

		return v;
	}
}
