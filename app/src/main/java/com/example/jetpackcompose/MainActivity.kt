package com.example.jetpackcompose

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetpackcompose.app.AppQuanLyChiTieu
import com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction.ReadTransactionNoti

class MainActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_AUTO_START_ENABLED = "auto_start_enabled"
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kiểm tra và yêu cầu quyền Notification Listener
        checkAndRequestNotificationPermission(this)

        // Kiểm tra và yêu cầu bật AutoStart
        checkAndRequestAutoStart(this)

        // Khởi động service
        val serviceIntent = Intent(this, ReadTransactionNoti::class.java)
        startService(serviceIntent)

        setContent {
            AppQuanLyChiTieu()
        }
    }

    private fun checkAndRequestAutoStart(context: Context) {
        val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isAutoStartEnabled = sharedPrefs.getBoolean(KEY_AUTO_START_ENABLED, false)

        if (!isAutoStartEnabled) {
            showAutoStartDialog(context, sharedPrefs)
        } else {
            Log.d("AutoStartCheck", "AutoStart đã được bật.")
        }
    }

    private fun showAutoStartDialog(context: Context, sharedPrefs: SharedPreferences) {
        AlertDialog.Builder(context).apply {
            setTitle("Bật AutoStart")
            setMessage("Để ứng dụng hoạt động tốt hơn, vui lòng bật quyền AutoStart. Bạn có muốn thực hiện bây giờ?")
            setPositiveButton("Đồng ý") { _, _ ->
                redirectToAutoStart(context)

                // Lưu trạng thái AutoStart đã bật
                sharedPrefs.edit().putBoolean(KEY_AUTO_START_ENABLED, true).apply()
            }
            setNegativeButton("Hủy bỏ") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun redirectToAutoStart(context: Context) {
        val intent = Intent()
        val manufacturer = Build.MANUFACTURER.lowercase()

        when (manufacturer) {
            "xiaomi" -> intent.component = ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
            "oppo" -> intent.component = ComponentName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.permission.startup.StartupAppListActivity"
            )
            "vivo" -> intent.component = ComponentName(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            )
            "huawei" -> intent.component = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
            "samsung" -> intent.component = ComponentName(
                "com.samsung.android.sm_cn",
                "com.samsung.android.sm.ui.ram.AutoRunActivity"
            )
            else -> {
                // Nếu không biết nhà sản xuất, mở màn hình cài đặt ứng dụng
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = android.net.Uri.parse("package:${context.packageName}")
            }
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Nếu thất bại, mở màn hình cài đặt ứng dụng
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = android.net.Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        }
    }

    private fun checkAndRequestNotificationPermission(context: Context) {
        val isNotificationEnabled = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )?.contains(context.packageName) == true

        if (!isNotificationEnabled) {
            showNotificationPermissionDialog(context)
        } else {
            Log.d("PermissionCheck", "Quyền Notification Listener đã được cấp.")
        }
    }

    private fun showNotificationPermissionDialog(context: Context) {
        AlertDialog.Builder(context).apply {
            setTitle("Cấp quyền thông báo")
            setMessage("Ứng dụng cần quyền để lắng nghe thông báo. Bạn có muốn cấp quyền?")
            setPositiveButton("Đồng ý") { _, _ ->
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            }
            setNegativeButton("Hủy bỏ") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }
}
