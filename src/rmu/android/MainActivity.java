package rmu.android;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		boolean installed = appInstalledOrNot("com.android.chrome");
		if (installed) {
			Intent i = new Intent("android.intent.action.MAIN");
			i.setComponent(ComponentName
					.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
			i.addCategory("android.intent.category.LAUNCHER");
			i.setData(Uri.parse("http://54.245.108.132/game"));
			startActivity(i);
			finish();
		} else {
			System.out.println("No Chrome on the device!");
		}
	}
	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
}
