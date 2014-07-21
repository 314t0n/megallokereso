package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.fragment.RouteFragment;
import hu.hajnaldavid.android.bkvmegallok.fragment.StopFragment;
import hu.hajnaldavid.android.bkvmegallok.fragment.StopsFragment;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class StopsFragmentPagerAdapter extends FragmentStatePagerAdapter {

	public StopsFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		mPageReferenceMap = new HashMap<Integer, Fragment>();
	}

	protected Map<Integer, Fragment> mPageReferenceMap;

	protected StopFragment stopFragment;
	protected RouteFragment routeFragment;
	private Fragment myFragment;

	public StopFragment getStopFragment() {
		if (stopFragment == null)
			stopFragment = new StopFragment();
		return stopFragment;
	}

	public RouteFragment getRouteFragment() {
		if (routeFragment == null)
			routeFragment = new RouteFragment();
		return routeFragment;
	}

	private int pos;

	@Override
	public Fragment getItem(int index) {
		myFragment = null;
		pos = index;
		switch (index) {
		case 0:
			myFragment = new StopsFragment();
			break;
		case 1:
			stopFragment = new StopFragment();
			myFragment = stopFragment;
			// notifyDataSetChanged();
			break;
		case 2:
			routeFragment = new RouteFragment();
			myFragment = routeFragment;
			// notifyDataSetChanged();
			break;
		}
		mPageReferenceMap.put(index, myFragment);
		return myFragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mPageReferenceMap.remove(position);
	}

	@Override
	public int getCount() {
		return 3;
	}

}
