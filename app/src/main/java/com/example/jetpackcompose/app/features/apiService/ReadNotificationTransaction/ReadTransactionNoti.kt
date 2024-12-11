package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.ComponentCallbacks2
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.jetpackcompose.R
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

    @SuppressLint("NewApi")
    override fun onCreate() {
        super.onCreate()

        // Tải dữ liệu từ bộ nhớ trong khi khởi tạo dịch vụ
        transactionList.addAll(transactionStorage.loadTransactions())
        Log.d("NotificationService", "Tải dữ liệu từ bộ nhớ trong: $transactionList")

        // Tạo thông báo Foreground
        startForegroundService()
    }

    private fun startForegroundService() {
        // Tạo kênh thông báo (yêu cầu từ Android 8.0 trở lên)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "transaction_notification_service"
            val channelName = "Transaction Notification Listener"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channel = android.app.NotificationChannel(
                channelId,
                channelName,
                android.app.NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
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
        val transactionData = getTransactionData(packageName, notificationText)

        transactionData?.let {
            // Thêm giao dịch vào danh sách
            transactionList.add(it)

            // Lưu danh sách giao dịch vào bộ nhớ trong
            transactionStorage.saveTransactions(transactionList)

            Log.d("NotificationService", "Danh sách giao dịch đã được lưu: $transactionList")
        }
    }

    private fun getTransactionData(packageName: String, text: String): TransactionReadNoti? {
        // Chuyển packageName thành chữ thường và tách ra bằng dấu "."
        val validPackageNames = listOf(
            "bidv", "techcombank", "vcb", "vib", "acb", "vnpay", "mbmobile", "viettinbank",
            "sgbank", "dongabank", "lpb", "hdbank", "ncb", "ocb", "sacombank", "cake", "tpb",
            "msb", "bplus", "facebook", "agribank3"
        )

        val packageNameParts = packageName.toLowerCase(Locale.getDefault()).split(".")
        Log.d("NotificationService", "Package Name Parts: $packageNameParts")

        // Kiểm tra xem packageName có thuộc danh sách hợp lệ không
        if (validPackageNames.any { it in packageNameParts }) {

            val transactionStartIndex = text.indexOfFirst { it == '+' || it == '-' }
            if (transactionStartIndex == -1) return null
            val transactionText = text.substring(transactionStartIndex)

            // Sử dụng Regex để nhận diện số dư và loại giao dịch
            val regex = """([+-])(\d{1,3}(?:,\d{3})*)(\s?VND)$""".toRegex()
            val matchResult = regex.find(transactionText)

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

        // Trả về null nếu không hợp lệ
        return null
    }



    private fun ensureServiceRunning() {
        val intent = Intent(this, ReadTransactionNoti::class.java)
        startService(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationService", "Thông báo đã bị xóa: ${sbn.packageName}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationListener", "onStartCommand triggered")
        startForegroundService()
        // Dùng super với đầy đủ tham số
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("NotificationListener", "onDestroy triggered")
        sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("NotificationListener", "onTaskRemoved triggered")
        sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        super.onTaskRemoved(rootIntent)
    }

    override fun onTrimMemory(level: Int) {
        Log.d("NotificationListener", "onTrimMemory triggered with level $level")
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        }
        super.onTrimMemory(level)
    }

    override fun sendBroadcast(intent: Intent?) {
        Log.d("NotificationListener", "sendBroadcast triggered")
        startForegroundService()
        super.sendBroadcast(intent)
    }
}

