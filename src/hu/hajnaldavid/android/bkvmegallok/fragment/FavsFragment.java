package hu.hajnaldavid.android.bkvmegallok.fragment;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.activity.MainActivity;
import hu.hajnaldavid.android.bkvmegallok.adapter.FavsAdapter;
import hu.hajnaldavid.android.bkvmegallok.database.Database;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class FavsFragment extends ListFragment {
	public interface OnFavSelectedListener {
		public void onFavSelected(Stop stop);
	}

	private OnFavSelectedListener listener;
	private List<Stop> stops;
	private Database datasource;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnFavSelectedListener) activity;
		} catch (ClassCastException ccex) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.favs_stoplist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void moveBack() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onStart() {
		super.onStart();

		ImageView back = (ImageView) getView().findViewById(
				R.id.favsListBackButton);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.favsListBackButton:

					moveBack();
					break;
				}
			}

		});

		datasource = new Database(getActivity());
		datasource.open();

		stops = datasource.getStops();

		FavsAdapter adapter = new FavsAdapter(getActivity(), 0, stops);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (listener != null && !stops.isEmpty()) {
			listener.onFavSelected(stops.get(position));
		}
	}

}
