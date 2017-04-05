package cpf.util.scache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * Hard disk cache
 * 
 * @author chenpengfei
 * 
 */
class DiskCache {

	private File dir;

	public DiskCache(Context context) {
		dir = context.getExternalCacheDir();
	}

	/**
	 * The cache to hard disk
	 * 
	 * @param name
	 * @param bitmap
	 * @throws IOException
	 */
	public void putDisk(String url, Bitmap bitmap) throws IOException {
		File file = new File(dir, String.valueOf(url.hashCode()));
		OutputStream os = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, os);
		os.flush();
		os.close();
	}

	/**
	 * Get the name of the specified file input stream
	 * 
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap getDisk(String url) throws FileNotFoundException {
		File file = new File(dir, String.valueOf(url.hashCode()));
		return BitmapFactory.decodeStream(new FileInputStream(file));
	}

	public void clear() {
		File[] files = dir.listFiles();
		for (File file : files) {
			file.delete();
		}
	}
}
