package com.coal.plugin;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coal.plugin.R;
import com.coal.plugin.impl.IDynamic;

import dalvik.system.DexClassLoader;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static final String LOGTAG = "PlaceholderFragment";

		public PlaceholderFragment() {
		}
		
		private void showPluginContent() {
            final File optimizedDexOutputPath = new File("/sdcard/a.jar");
            Log.e(LOGTAG,"file="+optimizedDexOutputPath.getAbsolutePath());
            File dexOutputDir = getActivity().getDir("dex", 0);
            DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(),
            		dexOutputDir.getAbsolutePath(), null, getActivity().getClassLoader());
                Class libProviderClazz = null;
                try {
                    libProviderClazz = cl.loadClass("com.coal.plugin.impl.DynamicImpl");
                    IDynamic lib = (IDynamic)libProviderClazz.newInstance();
                    Toast.makeText(getActivity(), lib.helloWorld(), Toast.LENGTH_SHORT).show();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			View v=rootView.findViewById(R.id.showToastFromPlugin);
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					showPluginContent();
//					Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
					
				}
			});
			return rootView;
		}
	}
}
