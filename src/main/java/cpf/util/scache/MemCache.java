package cpf.util.scache;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Memory cache
 * 
 * @author chenpengfei
 * 
 */
@SuppressLint("NewApi")
class MemCache {

	private LruCache<String, Bitmap> cache;

	public MemCache(int maxSize) {
		cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime()
				.maxMemory() / 8));
	}

	public void putBitmap(String url, Bitmap bitmap) {
		cache.put(String.valueOf(url.hashCode()), bitmap);
	}

	public Bitmap getBitmap(String url) {
		return cache.get(String.valueOf(url.hashCode()));
	}

	public void clear() {
		cache.evictAll();
	}
}
