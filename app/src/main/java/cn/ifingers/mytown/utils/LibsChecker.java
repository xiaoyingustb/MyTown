package cn.ifingers.mytown.utils;

import io.vov.vitamio.Vitamio;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public final class LibsChecker {
	public static final boolean checkVitamioLibs(Activity ctx) {
		if ((!Vitamio.isInitialized(ctx)) && (!ctx.getIntent().getBooleanExtra("fromVitamioInitActivity", false))) {
			Intent i = new Intent();
			i.setClassName(ctx.getPackageName(), "cn.ifingers.mytown.activity.InitActivity");
			i.putExtras(ctx.getIntent());
			i.setData(ctx.getIntent().getData());
			i.putExtra("package", ctx.getPackageName());
			i.putExtra("className", ctx.getClass().getName());
			ctx.startActivity(i);
			ctx.finish();
			return false;
		}
		return true;
	}
}