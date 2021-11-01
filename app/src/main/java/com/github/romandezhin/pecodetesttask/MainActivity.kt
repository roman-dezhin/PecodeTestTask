package com.github.romandezhin.pecodetesttask

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity(), UserActions {

    private lateinit var viewPager: ViewPager2
    private val items: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = createViewPagerAdapter()
        setCurrentPage(intent.extras?.getInt(EXTRA_SCREEN_ID))
    }

    private fun createViewPagerAdapter(): RecyclerView.Adapter<*> {
        return object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): ScreenFragment {
                val itemId = items.itemId(position)
                return fragmentByTagOrNew(itemId)
            }

            override fun getItemCount(): Int = items.size
        }
    }

    private fun fragmentByTagOrNew(tag: Int): ScreenFragment {
        val fragment = supportFragmentManager.findFragmentByTag(tag.toString())
        return if (fragment != null) {
            fragment as ScreenFragment
        } else {
            ScreenFragment().withArgs {
                putInt(ScreenFragment.KEY_ID, tag)
            }
        }
    }

    private fun changeDataSet(performChanges: () -> Unit) {
        val idsOld = items.createIdSnapshot()
        performChanges()
        val idsNew = items.createIdSnapshot()
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = idsOld.size
            override fun getNewListSize(): Int = idsNew.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                idsOld[oldItemPosition] == idsNew[newItemPosition]

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areItemsTheSame(oldItemPosition, newItemPosition)
        }, true).dispatchUpdatesTo(viewPager.adapter!!)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val id = intent?.extras?.getInt(EXTRA_SCREEN_ID)
        setCurrentPage(id)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun addNewFragment() {
        changeDataSet { items.addNew() }
        viewPager.setCurrentItem(items.lastPosition(), true)
    }

    override fun removeLastFragment() {
        cancelAllFragmentNotification(items.lastPosition())
        changeDataSet { items.removeLast() }
    }

    override fun addNotification(screenNumber: Int) {
        val notification = createNotification(screenNumber)
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = NotificationID.ID
        notificationManager.notify(notificationId, notification)
        items.addNotification(screenNumber, notificationId)
    }

    private fun setCurrentPage(id: Int?) {
        if (id is Int && id > 0) {
            viewPager.currentItem = id - 1
        }
    }

    private fun createNotification(screenNumber: Int): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_ID, screenNumber)
            action = screenNumber.toString()
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_notification_large_icon
                )
            )
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text, screenNumber))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(R.color.notification_logo)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun cancelAllFragmentNotification(screenNumber: Int) {
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        for (id in items.notificationIds(screenNumber)) {
            notificationManager.cancel(id)
        }
    }

    companion object {
        private const val CHANNEL_ID = "Main"
        private const val EXTRA_SCREEN_ID = "Screen ID"
    }
}