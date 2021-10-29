package com.github.romandezhin.pecodetesttask

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = createViewPagerAdapter()
    }

    private fun createViewPagerAdapter(): RecyclerView.Adapter<*> {
        val items = items // avoids resolving the ViewModel multiple times
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

    override fun addNewFragment() {
        changeDataSet { items.addNew() }
        viewPager.setCurrentItem(items.lastPosition(), true)
    }

    override fun removeLastFragment() {
        TODO("Not yet implemented")
    }

    override fun addNotification() {
        TODO("Not yet implemented")
    }
}