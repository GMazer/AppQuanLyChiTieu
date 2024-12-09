package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.content.Intent
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackcompose.app.features.apiService.TransactionAPI.PostTransactionViewModel
import com.example.jetpackcompose.app.features.inputFeatures.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ReadTransactionNoti : NotificationListenerService() {

    private lateinit var viewModel: PostTransactionViewModel

    override fun onCreate() {
        super.onCreate()
        // Initialize ViewModel using the ApplicationContext
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PostTransactionViewModel::class.java)
    }

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
        val transactionData = getTransactionData(notificationText)

        transactionData?.let {
            // Gọi API postTransaction nếu là thông báo biến động số dư
            viewModel.postTransaction(it, { status ->
                Log.d("NotificationService", status)
            }, { error ->
                Log.e("NotificationService", error)
            })
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationService", "Thông báo đã bị xóa: ${sbn.packageName}")
    }

    private fun getTransactionData(text: String): Transaction? {
        // Kiểm tra nếu là biến động số dư và tách dữ liệu
        val regex = """[+-]\d{1,3}(?:,\d{3})*(\s?VND)?""".toRegex()
        val matchResult = regex.find(text)

        return matchResult?.let {
            val sign = it.groupValues[1]
            val amountStr = it.groupValues[2].replace(",", "")
            val amount = amountStr.toLongOrNull()
            val note = if (sign == "+") "income" else "expense"

            amount?.let {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())


                Transaction(
                    category_id = 1,
                    amount = it,
                    transaction_date = currentDate,
                    note = note
                )
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
}



