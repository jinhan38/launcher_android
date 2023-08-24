package com.example.launcher_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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

        val adapter = CustomAdapter(loadApps())

        findViewById<RecyclerView>(R.id.recyclerview).adapter = adapter
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun loadApps(): ArrayList<AppInfo> {
        val dataList = ArrayList<AppInfo>()
        val manager = packageManager
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities = manager!!.queryIntentActivities(i, 0)

        for (ri in availableActivities) {
            val app = AppInfo(
                appName = ri.loadLabel(manager).toString(),
                packageName = ri.activityInfo.packageName,
                icon = ri.activityInfo.loadIcon(manager)
            )
            dataList.add(app)
        }
        return dataList
    }

    inner class CustomAdapter(private val dataList: ArrayList<AppInfo>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var linearlayout: LinearLayout
            var appName: TextView
            var packageName: TextView
            var appIcon: ImageView

            init {
                linearlayout = view.findViewById(R.id.linearlayout)
                appName = view.findViewById(R.id.app_name)
                packageName = view.findViewById(R.id.package_name)
                appIcon = view.findViewById(R.id.app_icon)
            }
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
                    val i = packageManager!!.getLaunchIntentForPackage(dataList[position].packageName)
                    startActivity(i)
                }
            }
        }

        override fun getItemCount() = dataList.size
    }

}
