package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReadTransactionNoti : NotificationListenerService() {

    // Lưu trữ danh sách giao dịch vào bộ nhớ
    private val transactionList = mutableListOf<TransactionReadNoti>()

    // Sử dụng TransactionStorage để quản lý lưu trữ
    private val transactionStorage: TransactionStorage by lazy {
        TransactionStorage(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        // Tải danh sách giao dịch đã lưu từ bộ nhớ trong khi khởi tạo dịch vụ
        transactionList.addAll(transactionStorage.loadTransactions())
        Log.d("NotificationService", "Tải dữ liệu từ bộ nhớ trong: $transactionList")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Lấy nội dung thông báo
        val notificationText = sbn.notification.extras.getString("android.text") ?: "Unknown"
        Log.d("NotificationService", "Thông báo mới được nhận: $notificationText")

        // Kiểm tra xem thông báo có chứa thông tin về biến động số dư không
        val transactionData = getTransactionData(notificationText)

        transactionData?.let {
            // Thêm giao dịch vào danh sách
            transactionList.add(it)

            // Lưu danh sách giao dịch vào bộ nhớ trong
            transactionStorage.saveTransactions(transactionList)

            Log.d("NotificationService", "Danh sách giao dịch đã được lưu: $transactionList")
        }
    }

    private fun getTransactionData(text: String): TransactionReadNoti? {
        // Sử dụng Regex để nhận diện số dư và loại giao dịch
        val regex = """([+-])(\d{1,3}(?:,\d{3})*)(\s?VND)?""".toRegex()
        val matchResult = regex.find(text)

        return matchResult?.let {
            val sign = it.groupValues[1]  // Dấu + hoặc -
            val amountStr = it.groupValues[2].replace(",", "")  // Loại bỏ dấu phẩy
            val amount = amountStr.toLongOrNull()  // Chuyển đổi sang kiểu Long
            val note = if (sign == "+") "income" else "expense"

            amount?.let {
                // Lấy ngày hiện tại với định dạng "yyyy-MM-dd"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // Tạo đối tượng TransactionReadNoti với dữ liệu đã nhận diện
                TransactionReadNoti(
                    type = note,
                    amount = it,
                    date = currentDate
                )
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationService", "Thông báo đã bị xóa: ${sbn.packageName}")
    }
}
