package com.example.github2.view.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}