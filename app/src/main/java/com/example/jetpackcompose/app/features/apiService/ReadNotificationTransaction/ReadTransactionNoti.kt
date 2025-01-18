package com.example.jetpackcompose.app.features.apiService.ReadNotificationTransaction

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.jetpackcompose.TransactionNotiActivity
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
        val postTime = sbn.postTime
        val notificationTitle = sbn.notification.extras.getString("android.title") ?: "Unknown"
        val notificationText = sbn.notification.extras.getString("android.text") ?: "Unknown"
        Log.d("NotificationService", "Thông báo mới được nhận:")
        Log.d("NotificationService", "Package Name: $packageName")
        Log.d("NotificationService", "Title: $notificationTitle")
        Log.d("NotificationService", "Text: $notificationText")

        // Sử dụng SharedPreferences để lưu thời gian thông báo cuối cùng
        val sharedPreferences = this.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val lastNotificationTime = sharedPreferences.getLong("last_notification_time", 0L)

        // Kiểm tra xem thông báo có chứa thông tin về biến động số dư không
        val transactionData = getTransactionData(packageName, notificationText)

        transactionData?.let {

            if (postTime == lastNotificationTime) {
                return
            } else {
                // Thêm giao dịch vào danh sách
                transactionList.add(it)

                // Lưu danh sách giao dịch vào bộ nhớ trong
                transactionStorage.saveTransactions(transactionList)

                sharedPreferences.edit().putBoolean("is_dialog_shown", false).apply()

                sharedPreferences.edit().putLong("last_notification_time", postTime).apply()

                showReceivedNotification( if (it.type == "expense") "Expense" else "Income", it.amount.toString() + "VND")

                Log.d("NotificationService", "Danh sách giao dịch đã được lưu: $transactionList")
            }
        }
    }

    private fun getTransactionData(packageName: String, text: String): TransactionReadNoti? {
        // Chuyển packageName thành chữ thường và tách ra bằng dấu "."
        val validPackageNames = listOf(
            "bidv", "techcombank", "vcb", "vib", "acb", "vnpay", "mbmobile", "viettinbank",
            "sgbank", "dongabank", "lpb", "hdbank", "ncb", "ocb", "sacombank", "cake", "tpb",
            "msb", "bplus", "agribank3"
        )

        val packageNameParts = packageName.toLowerCase(Locale.getDefault()).split(".")
        Log.d("NotificationService", "Package Name Parts: $packageNameParts")

        // Kiểm tra xem packageName có thuộc danh sách hợp lệ không
        if (validPackageNames.any { it in packageNameParts }) {

            val transactionStartIndex = text.indexOfFirst { it == '+' || it == '-' }
            if (transactionStartIndex == -1) return null
            val vndIndex = text.indexOf("VND", transactionStartIndex)
            if (vndIndex == -1) return null
            val transactionText = text.substring(transactionStartIndex, vndIndex + 3) // +3 để bao gồm "VND"

            // Sử dụng Regex để nhận diện số dư và loại giao dịch
            val regex = """([+-])(\d{1,3}(?:,\d{3})*)(\s?VND)$""".toRegex()
            val matchResult = regex.find(transactionText)

            return matchResult?.let {
                val sign = it.groupValues[1]  // Dấu + hoặc -
                val amountStr = it.groupValues[2].replace(",", "")  // Loại bỏ dấu phẩy
                val amount = amountStr.toLongOrNull()  //
                if (amount != null) {
                    if (amount >= 50000000) return null
                }
                val note = if (sign == "+") "income" else "expense"


                amount?.let {

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

    @SuppressLint("NewApi", "NotificationPermission")
    private fun showReceivedNotification(title: String, text: String) {
        val channelId = "received_notification_channel"
        val channelName = "Received Notifications"

        // Tạo Intent để mở TransactionNotiActivity
        val intent = Intent(this, TransactionNotiActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channel = android.app.NotificationChannel(
                channelId, channelName, android.app.NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Đã nhận thông báo")
            .setContentText("$title: $text")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
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

        if (!isServiceRunning()) {
            startForegroundService()
        } else {
            Log.d("NotificationListener", "Service is already running.")
        }

        return START_STICKY
    }

    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any {
            it.service.className == ReadTransactionNoti::class.java.name
        }
    }

    override fun onDestroy() {
        Log.d("NotificationListener", "onDestroy triggered")
        if (!isServiceRunning()) {
            sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        } else {
            Log.d("NotificationListener", "Service is already running.")
        }
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("NotificationListener", "onTaskRemoved triggered")
        if (!isServiceRunning()) {
            sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        } else {
            Log.d("NotificationListener", "Service is already running.")
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onTrimMemory(level: Int) {
        Log.d("NotificationListener", "onTrimMemory triggered with level $level")
        if (!isServiceRunning()) {
            sendBroadcast(Intent(this, RestartServiceReceiver::class.java))
        } else {
            Log.d("NotificationListener", "Service is already running.")
        }
        super.onTrimMemory(level)
    }

    override fun sendBroadcast(intent: Intent?) {
        Log.d("NotificationListener", "sendBroadcast triggered")
        if (!isServiceRunning()) {
            startForegroundService()
        } else {
            Log.d("NotificationListener", "Service is already running.")
        }
        super.sendBroadcast(intent)
    }
}

