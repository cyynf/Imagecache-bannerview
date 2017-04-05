package cpf.util.scache;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageCache {
	private static int threadNums = 4;
	private static ImageCache imageCache;
	private MemCache memCache;
	private DiskCache diskCache;
	boolean isLoad = true;
	private ExecutorService executorService = Executors
			.newFixedThreadPool(threadNums);

	private ImageCache(Context context) {
		memCache = new MemCache((int) (Runtime.getRuntime().maxMemory() / 8));
		diskCache = new DiskCache(context);
	}

	public Bitmap getBitmap(String url) {
		Bitmap bitmap = memCache.getBitmap(url);
		if (bitmap != null) {
			return bitmap;
		} else
			try {
				if ((bitmap = diskCache.getDisk(url)) != null) {
					memCache.putBitmap(url, bitmap);
					return bitmap;
				}
			} catch (FileNotFoundException e) {
				return null;
			}
		return null;
	}

	public void setImageUrl(ImageView view, String url) {
		Bitmap bitmap;
		if ((bitmap = getBitmap(url)) != null) {
			view.setImageBitmap(bitmap);
		} else if (isLoad) {
			view.setTag(url);
			executorService.execute(new CacheTask(view));
		}

	}

	public void clearCache() {
		diskCache.clear();
	}

	/**
	 * Get a picture cache instances
	 * 
	 * @param context
	 * @return
	 */
	public static ImageCache getInstance(Context context) {
		if (imageCache == null) {
			imageCache = new ImageCache(context);
		}
		return imageCache;
	}

	class CacheTask implements Runnable {
		private ImageView view;

		public CacheTask(ImageView view) {
			this.view = view;
		}

		@Override
		public void run() {
			URL u;
			try {
				String url = (String) view.getTag();
				u = new URL(url);
				InputStream inputStream = u.openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
				memCache.putBitmap(url, bitmap);
				diskCache.putDisk(url, bitmap);
				Message msg = new Message();
				msg.obj = view;
				handler.sendMessage(msg);
			} catch (Exception e) {
				//executorService.execute(this);
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ImageView view = (ImageView) msg.obj;
			view.setImageBitmap(getBitmap((String) view.getTag()));
		}
	};

	public void onDestory() {
		executorService.shutdown();
		imageCache = null;
	}

}
