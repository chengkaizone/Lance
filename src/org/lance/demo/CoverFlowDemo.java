package org.lance.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import org.lance.adapters.CoverFlowImageAdapter;
import org.lance.main.R;
import org.lance.widget.D3Gallery;

public class CoverFlowDemo extends BaseActivity {
	private D3Gallery gallery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cover_layout);
		gallery = (D3Gallery) findViewById(R.id.cover_gallery);
		// gallery.setBackgroundResource(R.drawable.shape);
		gallery.setBackgroundColor(Color.BLACK);
		final Integer[] mImageIds = { R.drawable.bg2, R.drawable.bg2,
				R.drawable.bg2, R.drawable.bg2, R.drawable.bg2, R.drawable.bg2, };
		final Class<?>[] target = { VotePageDemo.class, VotePageDemo.class,
				VotePageDemo.class, VotePageDemo.class, VotePageDemo.class,
				VotePageDemo.class, };
		CoverFlowImageAdapter imageAdapter = new CoverFlowImageAdapter(this,
				mImageIds, target);
		gallery.setAdapter(imageAdapter);
		// cf.setAlphaMode(false);
		// cf.setCircleMode(false);
		gallery.setSelection(2, true);
		gallery.setAnimationDuration(1000);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(CoverFlowDemo.this, target[arg2]));
			}
		});
	}

}