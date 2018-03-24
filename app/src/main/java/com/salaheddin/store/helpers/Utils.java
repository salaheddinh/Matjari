package com.salaheddin.store.helpers;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {
	private static DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat sDateFormatWithoutSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static boolean canDrawOverlays(Context context){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}else{
			return Settings.canDrawOverlays(context);
		}
	}

	public static float convertDpToPixel(Context context,float dp) {
		DisplayMetrics mMetrics = context.getResources().getDisplayMetrics();
		return dp * mMetrics.density;
	}

	public static float convertPixelsToDp(Context context,float px) {
		DisplayMetrics mMetrics = context.getResources().getDisplayMetrics();
		return px / mMetrics.density;
	}

	public static void makeToast(Context context,String text) {
		Toast.makeText(context,text,Toast.LENGTH_LONG).show();
	}

	@SuppressLint("NewApi")
	public static Bitmap blurRenderScript(Context context,Bitmap smallBitmap, int radius) {

		try {
			smallBitmap = RGB565toARGB888(smallBitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}


		Bitmap bitmap = Bitmap.createBitmap(
				smallBitmap.getWidth(), smallBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);

		RenderScript renderScript = RenderScript.create(context);

		Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
		Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
				Element.U8_4(renderScript));
		blur.setInput(blurInput);
		blur.setRadius(radius); // radius must be 0 < r <= 25
		blur.forEach(blurOutput);

		blurOutput.copyTo(bitmap);
		renderScript.destroy();

		return bitmap;

	}

	private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
		int numPixels = img.getWidth() * img.getHeight();
		int[] pixels = new int[numPixels];

		//Get JPEG pixels.  Each int is the color values for one pixel.
		img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

		//Create a Bitmap of the appropriate format.
		Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

		//Set RGB pixels.
		result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
		return result;
	}

	public static int getRightNum(int num,int limit){
		float percentage = (float) Math.abs(num) / (float)limit;
		float rightNum = 255 * percentage;
		int iRightNum = (int) rightNum;
		return iRightNum;
	}

	public static void showDialog(Context activity, String errorMessage) {
		new AlertDialog.Builder(activity,R.style.AlertDialogTheme)
				.setMessage(errorMessage)
				.setPositiveButton(activity.getString(R.string.dialog_error_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	public static void showDialog(Activity activity, int errorId) {
		showDialog(activity, activity.getString(R.string.error_connection));
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}

	public static void showProgress(ProgressDialog dialog) {
		if (dialog != null && !dialog.isShowing()) {
			dialog.setCancelable(false);
			dialog.setMessage(MatjariApplication.getInstance()
					.getString(R.string.progress_dialog_loading));
			try {
				dialog.show();
			}
			catch(Exception e){}
		}
	}

	public static void dismissProgress(ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			}
			catch(Exception e){}

		}
	}

}
