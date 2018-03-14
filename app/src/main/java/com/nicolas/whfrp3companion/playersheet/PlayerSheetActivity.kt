package com.nicolas.whfrp3companion.playersheet

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.nicolas.whfrp3companion.CareersFragment
import com.nicolas.whfrp3companion.R
import org.jetbrains.anko.longToast

class PlayerSheetActivity : AppCompatActivity() {
    @BindView(R.id.drawer_layout)
    lateinit var drawer: DrawerLayout

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setupDrawerContent(navigationView)

        displaySelectedFragment(R.id.nav_home)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displaySelectedFragment(menuItemId: Int) {
        val fragment = CareersFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()

        longToast("Hey : $menuItemId")
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun displaySelectedFragment(menuItem: MenuItem) {
        displaySelectedFragment(menuItem.itemId)

        menuItem.isChecked = true
        title = menuItem.title
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            displaySelectedFragment(it)
            true
        }
    }
}
