package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.content.Intent
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class ReadTransactionNoti : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Lấy thông tin thông báo
        val packageName = sbn.packageName
        val notificationTitle = sbn.notification.extras.getString("android.title") ?: "Unknown"
        val notificationText = sbn.notification.extras.getString("android.text") ?: "Unknown"

        Log.d("NotificationService", "Thông báo mới được nhận:")
        Log.d("NotificationService", "Package Name: $packageName")
        Log.d("NotificationService", "Title: $notificationTitle")
        Log.d("NotificationService", "Text: $notificationText")

        // Kiểm tra xem thông báo có chứa thông tin về biến động số dư không
        if (isBalanceChange(notificationText)) {
            Log.d("NotificationService", "Đã nhận thông báo biến động số dư: $notificationText")
            // Thêm logic xử lý tại đây nếu cần
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationService", "Thông báo đã bị xóa: ${sbn.packageName}")
    }

    private fun isBalanceChange(text: String): Boolean {
        // Kiểm tra nếu thông báo chứa chuỗi số bắt đầu với dấu + hoặc -
        return text.matches(".*[+-]\\d.*".toRegex())
    }

    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val setting = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return setting?.contains(packageName) == true
    }
}
