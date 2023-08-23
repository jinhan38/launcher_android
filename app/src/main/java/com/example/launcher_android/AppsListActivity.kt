package com.example.launcher_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView


class AppsListActivity : Activity() {

    private var manager: PackageManager? = null
    private var apps: MutableList<AppDetail>? = null
    private var list: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps_list)

        loadApps();
        loadListView();
        addClickListener();

    }

    private fun loadApps() {
        manager = packageManager
        apps = ArrayList()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities = manager!!.queryIntentActivities(i, 0)
        manager!!.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY)

        for (ri in availableActivities) {
            val app = AppDetail(
                label = ri.loadLabel(manager).toString(),
                name = ri.activityInfo.packageName,
                icon = ri.activityInfo.loadIcon(manager)
            )
            apps!!.add(app)
        }
    }



    private fun loadListView() {
        list = findViewById<View>(R.id.apps_list) as ListView

        val adapter: ArrayAdapter<AppDetail> = object : ArrayAdapter<AppDetail>(this, R.layout.list_item, apps!!) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View? = layoutInflater.inflate(R.layout.list_item, null)
                val appIcon = view!!.findViewById<View>(R.id.item_app_icon) as ImageView
                appIcon.setImageDrawable(apps!![position].icon)
                val appLabel = view.findViewById<View>(R.id.item_app_label) as TextView
                appLabel.text = apps!![position].label
                val appName = view.findViewById<View>(R.id.item_app_name) as TextView
                appName.text = apps!![position].name
                return view
            }
        }

        list!!.adapter = adapter
    }

    private fun addClickListener() {
        list!!.onItemClickListener = OnItemClickListener { _, _, pos, _ ->
            val i = manager!!.getLaunchIntentForPackage(apps!![pos].name)
            this@AppsListActivity.startActivity(i)
        }
    }
    
}