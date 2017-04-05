package cpf.util.scache;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

/**
 * 可以实现滚动时不加载，滚动停止时加载
 * 
 * @author CPF
 * 
 */
public class OnScrollLoadListener implements OnScrollListener {

	private ImageCache imageCache;
	private BaseAdapter adapter;

	public OnScrollLoadListener(ImageCache imageCache, BaseAdapter adapter) {
		this.imageCache = imageCache;
		this.adapter = adapter;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_FLING:
			imageCache.isLoad = false;
			break;
		case SCROLL_STATE_IDLE:
			imageCache.isLoad = true;
			adapter.notifyDataSetChanged();
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			imageCache.isLoad = false;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// start = firstVisibleItem;
		// end = visibleItemCount + firstVisibleItem - 1;
	}

}
