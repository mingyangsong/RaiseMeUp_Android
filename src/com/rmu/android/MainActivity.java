package com.rmu.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import com.rmu.android.R;

public class MainActivity extends Activity {
	protected ProgressDialog progressDialog;
	protected String apkName = "Chrome28.apk";

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
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("Preparing");
			progressDialog.setMessage("Installing the latest Chrome Browser");
			progressDialog.setCancelable(false);
			progressDialog.show();
			Thread installChrome = new Thread(new installChrome());
			installChrome.start();
		}
	}

	protected class installChrome implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AssetManager assetManager = getAssets();

			InputStream in = null;
			OutputStream out = null;

			try {
				in = assetManager.open(apkName);
				out = new FileOutputStream(
						Environment.getExternalStorageDirectory() + "/"
								+ apkName);
				byte[] buffer = new byte[1024];

				int read;
				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}

				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
				progressDialog.cancel();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory() + "/" + apkName)),
						"application/vnd.android.package-archive");
				startActivity(intent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		boolean installed = appInstalledOrNot("com.android.chrome");
		if (installed) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + apkName);
			if (file.exists())
				file.delete();
			Intent i = new Intent("android.intent.action.MAIN");
			i.setComponent(ComponentName
					.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
			i.addCategory("android.intent.category.LAUNCHER");
			i.setData(Uri.parse("http://54.245.108.132/game"));
			startActivity(i);
			finish();
		} else {
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("Preparing");
			progressDialog.setMessage("Installing the latest Chrome Browser");
			progressDialog.setCancelable(false);
			progressDialog.show();
			Thread installChrome = new Thread(new installChrome());
			installChrome.start();
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
