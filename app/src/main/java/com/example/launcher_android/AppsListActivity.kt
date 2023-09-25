package com.example.launcher_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AppsListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps_list)

        val adapter = CustomAdapter(dataList = loadApps(), context = this)
        findViewById<RecyclerView>(R.id.recyclerview).adapter = adapter
    }

    @SuppressLint("QueryPermissionsNeeded", "InlinedApi")
    private fun loadApps(): ArrayList<AppInfo> {
        val dataList = ArrayList<AppInfo>()
        val manager = packageManager

        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_GIDS)

        for (app in apps) {
            if (pm.getLaunchIntentForPackage(app.packageName) != null) {
                if (app.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                    // system apps
                } else {
                    dataList.add(
                        AppInfo(
                            appName = app.loadLabel(manager).toString(),
                            packageName = app.packageName,
                            icon = app.loadIcon(manager)
                        )
                    )
                }
            }
        }
        return dataList
    }
}

class CustomAdapter(private val dataList: ArrayList<AppInfo>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linearlayout: LinearLayout = view.findViewById(R.id.linearlayout)
        var appName: TextView = view.findViewById(R.id.app_name)
        var packageName: TextView = view.findViewById(R.id.package_name)
        var appIcon: ImageView = view.findViewById(R.id.app_icon)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            appName.text = dataList[position].appName
            packageName.text = dataList[position].packageName
            appIcon.setImageDrawable(dataList[position].icon)

            linearlayout.setOnClickListener {
                val i =
                    context.packageManager!!.getLaunchIntentForPackage(dataList[position].packageName)
                context.startActivity(i)
            }
        }
    }

    override fun getItemCount() = dataList.size
}