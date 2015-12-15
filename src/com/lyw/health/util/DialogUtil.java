package com.lyw.health.util;


import java.util.List;

import com.baidu.location.b.d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class DialogUtil {
	private static AlertDialog dialog = null;

	private static Toast toast;

	public static void showToast(Context context, String msg) {

		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}
	public static void showAlertDialog(Context context, String title,
			String[] items, OnClickListener dialogInterface) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items, dialogInterface);
		dialog = builder.create();
		dialog.show();
	}
	public static void showAlertDialog(Context context, String title,
			List<String> items, OnClickListener dialogInterface) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items.toArray(new String[]{}), dialogInterface);
		dialog = builder.create();
		dialog.show();
	}

	public static void showAlertDialog(Context context, String title,
			int items, OnClickListener dialogInterface) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items, dialogInterface);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 关闭弹出�?
	 */
	public static void closeAlertDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog=null;
		}
	}

	/**
	 * show a closeDialog
	 * 
	 * @param context
	 * @param dialogInterface
	 *            确定按钮事件
	 */
	public static void showExitAlertDialog(Context context,
			OnClickListener dialogInterface) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("关闭").setMessage("您确认要推出吗？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						closeAlertDialog();
					}
				}).setPositiveButton("确定", dialogInterface);
		dialog = builder.create();
		dialog.show();
	}
	/**
	 * show a closeDialog
	 * 
	 * @param context
	 * @param dialogInterface
	 *            确定按钮事件
	 */
	public static void showExitAlertDialog(Context context,String msg,
			OnClickListener dialogInterface) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("确定关闭").setMessage(msg)
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeAlertDialog();
			}
		}).setPositiveButton("确定", dialogInterface);
		dialog = builder.create();
		dialog.show();
	}

	public static void showWaitDialog(Context context) {
		dialog = ProgressDialog.show(context, "下载", "正在下载");
	}

	public static void showWaitDialog(Context context, String title,
			String message) {
//		dialog = ProgressDialog.show(context, title, message);
		if(dialog==null ){
			dialog = ProgressDialog.show(context, title, message);
		}
//		return dialog;
	}
}

