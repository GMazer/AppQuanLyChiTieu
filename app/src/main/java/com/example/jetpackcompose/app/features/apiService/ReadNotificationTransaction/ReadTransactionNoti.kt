package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.content.Intent
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class ReadTransactionNoti : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Lấy nội dung thông báo
        val notificationText = sbn.notification.extras.getString("android.text")

        notificationText?.let {
            // Kiểm tra xem thông báo có chứa thông tin về biến động số dư không
            if (isBalanceChange(it)) {
                Log.d("NotificationService", "Đã nhận thông báo biến động số dư: $it")
                // Xử lý logic cho việc nhận diện số dư
            }
        }
    }

    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val setting = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return setting?.contains(packageName) == true
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Xử lý khi thông báo bị xóa (nếu cần)
    }

    private fun isBalanceChange(text: String): Boolean {
        // Kiểm tra nếu thông báo chứa chuỗi số bắt đầu bằng dấu + hoặc -
        return text.matches(".*[+-]\\d.*".toRegex())
    }
}
