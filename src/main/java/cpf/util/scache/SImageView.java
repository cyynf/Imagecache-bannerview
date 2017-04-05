package cpf.util.scache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SImageView extends ImageView {

	public SImageView(Context context) {
		super(context);
	}

	@SuppressLint("NewApi")
	public SImageView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public SImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setImageViewUrl(String url) {
		ImageCache.getInstance(getContext()).setImageUrl(this, url);
	}

}
