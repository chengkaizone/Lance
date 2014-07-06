package org.lance.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;

public class MergeAdapter extends BaseAdapter implements SectionIndexer {
	private List<ListAdapter> pieces = new ArrayList<ListAdapter>();

	public void addAdapter(ListAdapter paramListAdapter) {
		this.pieces.add(paramListAdapter);
		paramListAdapter.registerDataSetObserver(new CascadeDataSetObserver());
	}

	public void addView(View paramView) {
		addView(paramView, false);
	}

	public void addView(View paramView, boolean paramBoolean) {
		List<View> list = new ArrayList<View>(1);
		list.add(paramView);
		addViews(list, paramBoolean);
	}

	public void addViews(List<View> paramList) {
		addViews(paramList, false);
	}

	public void addViews(List<View> paramList, boolean paramBoolean) {
		if (!paramBoolean)
			addAdapter(new SackOfViewsAdapter(paramList));
		else
			addAdapter(new EnabledSackAdapter(paramList));
	}

	@Override
	public int getCount() {
		int i = 0;
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		while (iterator.hasNext()) {
			i += ((ListAdapter) iterator.next()).getCount();
		}
		return i;
	}

	public long getItemId(int paramInt) {
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter;
		while (true) {
			if (!iterator.hasNext())
				return -1l;
			adapter = (ListAdapter) iterator.next();
			int i = adapter.getCount();
			if (paramInt < i)
				break;
			paramInt -= i;
		}
		return adapter.getItemId(paramInt);
	}

	public int getItemViewType(int paramInt) {
		int j = 0;
		int k = -1;
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter = null;
		while (true) {
			if (!iterator.hasNext()) {
				return k;
			}
			adapter = (ListAdapter) iterator.next();
			int i = adapter.getCount();
			if (paramInt < i)
				break;
			paramInt -= i;
			j += adapter.getViewTypeCount();
		}
		k = j + adapter.getItemViewType(paramInt);
		return k;
	}

	public Object getItem(int position) {
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		Object obj = null;
		while (true) {
			if (!iterator.hasNext()) {
				return null;
			}
			obj = (ListAdapter) iterator.next();
			int i = ((ListAdapter) obj).getCount();
			if (position < i)
				break;
			position -= i;
		}
		obj = ((ListAdapter) obj).getItem(position);
		return obj;
	}

	public boolean isEnabled(int paramInt) {
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter = null;
		while (true) {
			if (!iterator.hasNext()) {
				return false;
			}
			adapter = (ListAdapter) iterator.next();
			int i = adapter.getCount();
			if (paramInt < i)
				break;
			paramInt -= i;
		}
		boolean bool = adapter.isEnabled(paramInt);
		return bool;
	}

	public int getViewTypeCount() {
		int i = 0;
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		while (iterator.hasNext()) {
			i += ((ListAdapter) iterator.next()).getViewTypeCount();
		}
		return Math.max(i, 1);
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter = null;
		while (true) {
			if (!iterator.hasNext()) {
				return null;
			}
			adapter = (ListAdapter) iterator.next();
			int i = adapter.getCount();
			if (position < i)
				break;
			position -= i;
		}
		View localView = adapter.getView(position, convertView, parent);
		return localView;
	}

	@Override
	public Object[] getSections() {
		List<Object> list = new ArrayList<Object>();
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		Object[] arrayOfObject = null;
		do {
			ListAdapter adapter = null;
			do {
				if (!iterator.hasNext()) {
					return list.toArray(new Object[0]);
				}
				adapter = iterator.next();
			} while (!(adapter instanceof SectionIndexer));
			arrayOfObject = ((SectionIndexer) adapter).getSections();
		} while (arrayOfObject == null);
		for (int i = 0; i < arrayOfObject.length; ++i) {
			list.add(arrayOfObject[i]);
		}
		return arrayOfObject;
	}

	@Override
	public int getPositionForSection(int section) {
		int k = 0;
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter = null;
		int i = 0;
		while (true) {
			if (!iterator.hasNext()) {
				return 0;
			}
			adapter = (ListAdapter) iterator.next();
			if (adapter instanceof SectionIndexer) {
				Object[] arrayOfObject = ((SectionIndexer) adapter)
						.getSections();
				i = 0;
				if (arrayOfObject != null)
					i = arrayOfObject.length;
				if (section < i)
					break;
				if (arrayOfObject != null)
					section -= i;
			}
			k += adapter.getCount();
		}
		i = k + ((SectionIndexer) adapter).getPositionForSection(section);
		return i;
	}

	@Override
	public int getSectionForPosition(int position) {
		int j = 0;
		int k = 0;
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		Object obj = null;
		while (true) {
			if (!iterator.hasNext()) {
				return 0;
			}
			obj = (ListAdapter) iterator.next();
			int i = ((ListAdapter) obj).getCount();
			if (position < i)
				break;
			if (obj instanceof SectionIndexer) {
				obj = ((SectionIndexer) obj).getSections();
				if (obj != null) {
					k += ((Object[]) obj).length;
				}
			}
			position -= i;
		}
		if (obj instanceof SectionIndexer)
			j = k + ((SectionIndexer) obj).getSectionForPosition(position);
		return j;
	}

	public ListAdapter getAdapter(int position) {
		Iterator<ListAdapter> iterator = this.pieces.iterator();
		ListAdapter adapter = null;
		while (iterator.hasNext()) {
			adapter = (ListAdapter) iterator.next();
			int i = adapter.getCount();
			if (position < i)
				return adapter;
			position -= i;
		}
		return adapter;
	}

	private class CascadeDataSetObserver extends DataSetObserver {
		private CascadeDataSetObserver() {
		}

		public void onChanged() {
			MergeAdapter.this.notifyDataSetChanged();
		}

		public void onInvalidated() {
			MergeAdapter.this.notifyDataSetInvalidated();
		}
	}

	private static class EnabledSackAdapter extends SackOfViewsAdapter {
		public EnabledSackAdapter(List<View> paramList) {
			super(paramList);
		}

		public boolean areAllItemsEnabled() {
			return true;
		}

		public boolean isEnabled(int paramInt) {
			return true;
		}
	}
}
