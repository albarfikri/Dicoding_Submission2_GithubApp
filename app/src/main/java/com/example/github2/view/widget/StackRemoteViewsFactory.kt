package com.example.github2.view.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.github2.db.DatabaseUser
import com.example.github2.db.UserFavorite
import com.example.github2.db.UserFavoriteDao

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private lateinit var list: List<UserFavorite>
    private lateinit var userDao: UserFavoriteDao

    override fun onCreate() {
        userDao = DatabaseUser.getUserDatabase(mContext)!!.userFavDao()
    }

    override fun onDataSetChanged() {
        list = userDao.getfavWidget()

    }

    override fun onDestroy() {}

    override fun getCount(): Int = list.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, android.R.layout.simple_list_item_1)
        remoteViews.setTextViewText(android.R.id.text1, list[position].username)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = list[i].id.toLong()

    override fun hasStableIds(): Boolean = true

}