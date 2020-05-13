package br.com.estudo.appbot

import android.app.Notification
import android.app.PendingIntent
import android.content.pm.ApplicationInfo
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotifierListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        println("Connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val notification = sbn?.notification
        val extras = notification?.extras

        println(extras)
        extras?.let {
            //Evento de limpeza da Statusbar
            this@NotifierListener.cancelNotification(sbn.key)

            //Verifica se é o WhatsApp
            var processName =
                (it["android.rebuild.applicationInfo"] as? ApplicationInfo)?.processName

            if (processName == null)
                processName = (it["android.appInfo"] as? ApplicationInfo)?.processName

            // Verifica o nome do contato e ignora resumo da mensagem e imagem recebida
            if (processName == "com.whatsapp"
                && it.getString(Notification.EXTRA_TITLE) == "Leo"
                && it.getString(Notification.EXTRA_BIG_TEXT) == null
                && it.getString(Notification.EXTRA_SUMMARY_TEXT) == null
            ) {

                //Aqui faz as respostas
                val action = Notificationutils.quickReplyAction(sbn.notification)

                try {
                    action?.sendReply(
                        applicationContext,
                        "Olá Leo, aqui é um robô te respondendo!!!"
                    )
                } catch (e: PendingIntent.CanceledException) {

                }
            }
        }
    }
}