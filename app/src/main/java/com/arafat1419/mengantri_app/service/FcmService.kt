package com.arafat1419.mengantri_app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    private val customerSessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(this)
    }

    private val companySessionManager: CompanySessionManager by lazy {
        CompanySessionManager(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: ${message.from}")

        when {
            // WHEN LOGIN AS COMPANY
            customerSessionManager.fetchCustomerId() != -1 && companySessionManager.fetchCompanyId() != -1 -> {
                Log.d(TAG, "onMessageReceived: COMPANY")
            }
            // WHEN LOGIN AS CUSTOMER
            customerSessionManager.fetchCustomerId() != -1 && companySessionManager.fetchCompanyId() == -1 -> {
                Log.d(TAG, "onMessageReceived: CUSTOMER")
            }
        }

        sendNotification(message)
        /*if (message.data.isNotEmpty()) {
            val extras = Bundle()
            for ((key, value) in message.data) {
                extras.putString(key, value)
            }
            if (extras.containsKey("message") && !extras.getString("message").isNullOrBlank()) {

            }
        }*/
    }

    private fun sendNotification(message: RemoteMessage) {
        Log.d(TAG, "sendNotification: JALAN")
        Log.d(TAG, "sendNotification: ${message.notification?.title}")
        /*val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)*/

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder: NotificationCompat.Builder? = null
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                packageName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = packageName
            notificationManager.createNotificationChannel(channel)
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }
        } else {
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }
        }
        notificationBuilder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        const val TAG = "FcmService"
    }
}