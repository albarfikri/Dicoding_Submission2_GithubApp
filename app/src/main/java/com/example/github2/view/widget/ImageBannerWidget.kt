package com.example.github2.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.example.github2.R


class ImageBannerWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.example.github2.TOAST_ACTION"

        //pindahkan fungsi ini ke companion object, karena kita akan memanggil fungsi ini dari luar kelas
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
            views.setRemoteAdapter(R.id.listwidget, intent)
            val toastIntent = Intent(context, StackWidgetService::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.listwidget, toastPendingIntent)
            appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.layout.image_banner_widget
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                Toast.makeText(context, "Touched view", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEnabled(context: Context) {
        val message = context.resources.getString(R.string.loading)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context) {
        val message = context.resources.getString(R.string.loading)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}