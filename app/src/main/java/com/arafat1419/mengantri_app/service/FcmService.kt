package com.arafat1419.mengantri_app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.domain.model.NotificationDomain
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_NOTIFICATION_STATUS
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_NOTIFICATION_TICKET_ID
import com.arafat1419.mengantri_app.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            val data = JSONObject(message.data as Map<String?, String?>)
            val notification = NotificationDomain(
                data.optInt("ticket_id"),
                data.optString("service_name"),
                data.optString("customer_name"),
                data.optString("ticket_date")
            )
            sendNotification(notification)
        }
    }

    private fun sendNotification(notificationDomain: NotificationDomain) {
        val companyId = CompanySessionManager(this).fetchCompanyId()

        val isIntent: Boolean

        val notificationTitle: String
        val notificationBody: String
        val intent: Intent

        if (notificationDomain.ticketDate.isNullOrEmpty()) {
            notificationTitle =
                getString(com.arafat1419.mengantri_app.assets.R.string.customer_notification_title)
            notificationBody = getString(
                com.arafat1419.mengantri_app.assets.R.string.customer_notification_body,
                notificationDomain.ticketId,
                notificationDomain.serviceName
            )

            intent = Intent(this, MainActivity::class.java)
            isIntent = companyId == -1
        } else {
            notificationTitle =
                getString(com.arafat1419.mengantri_app.assets.R.string.company_notification_title)
            notificationBody = getString(
                com.arafat1419.mengantri_app.assets.R.string.company_notification_body,
                notificationDomain.customerName,
                notificationDomain.serviceName,
                DateHelper.toUpdateLabel(notificationDomain.ticketDate!!)
            )
            intent = Intent(
                this,
                Class.forName("com.arafat1419.mengantri_app.company.CompanyActivity")
            )
            isIntent = companyId != -1
        }

        intent.also {
            it.putExtra(EXTRA_NOTIFICATION_STATUS, true)
            it.putExtra(EXTRA_NOTIFICATION_TICKET_ID, notificationDomain.ticketId)
        }

        val pendingFlag: Int

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder: NotificationCompat.Builder? = null
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

            pendingFlag = PendingIntent.FLAG_MUTABLE
        } else {
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }

            pendingFlag = PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0 /* Request code */, intent, pendingFlag)

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationTitle))
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)

        if (isIntent) notificationBuilder.setContentIntent(pendingIntent)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        const val TAG = "FcmService"
    }
}