package br.com.estudo.appbot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class Action(private val action: NotificationCompat.Action) {

    fun sendReply(context: Context, message: String) {
        val intent = Intent()
        val bundle = Bundle()
        val remoteInputs = arrayListOf<RemoteInput>()

        // Processo de envio da mensagem
        if (action?.remoteInputs != null) {
            for (input in action.remoteInputs) {
                bundle.putCharSequence(input.resultKey, message)
                val newRemoteInput = RemoteInput.Builder(input.resultKey)
                    .setLabel(input.label)
                    .setChoices(input.choices)
                    .setAllowFreeFormInput(input.allowFreeFormInput)
                    .addExtras(input.extras)
                    .build()

                remoteInputs.add(newRemoteInput)
            }
        }

        RemoteInput.addResultsToIntent(remoteInputs.toTypedArray(), intent, bundle)
        action.actionIntent.send(context, 0, intent)
    }
}