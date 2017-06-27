package common;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityManager {
	private static ActivityManager manager = null;
	private ArrayList<Activity> list = null;

	private ActivityManager() {
		list = new ArrayList<Activity>();
	}

	public static ActivityManager getInstance() {
		if (ActivityManager.manager == null)
			manager = new ActivityManager();
		return manager;
	}

	public ArrayList<Activity> getList() {
		return list;
	}

	public void addlist(Activity act) {
		list.add(act);
	}

	public boolean removeAct(Activity act) {

		act.finish();
		return list.remove(act);
	}

	public void Allremove(Activity act) {
		for (Activity act1 : list) {
			if (!act.equals(act1))
				act1.finish();
		}
	}
}
