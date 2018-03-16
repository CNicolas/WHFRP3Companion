package com.nicolas.whfrp3companion

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
import butterknife.Unbinder
import com.nicolas.whfrp3companion.fragments.EmptyFragment
import com.nicolas.whfrp3companion.fragments.players.PlayersFragment
import org.jetbrains.anko.intentFor


class MainActivity : AppCompatActivity() {
    @BindView(R.id.main_drawer_layout)
    lateinit var drawer: DrawerLayout

    private lateinit var unbinder: Unbinder

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        unbinder = ButterKnife.bind(this)

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

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
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
        val fragment = when (menuItemId) {
            R.id.nav_home -> PlayersFragment.newInstance()
//            R.id.nav_talents -> TalentTypesFragment()
//            R.id.nav_items -> ItemsFragment()
//            R.id.nav_skills -> SkillsFragment.newInstance()
//            R.id.nav_specializations -> SpecializationsFragment()
            else -> EmptyFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_frame, fragment)
                .commit()

        drawer.closeDrawer(GravityCompat.START)
    }

    private fun displaySelectedFragment(menuItem: MenuItem) {
        if (menuItem.itemId == R.id.nav_dice_launcher) {
            startActivity(this.intentFor<DiceLauncherActivity>())
        } else {
            displaySelectedFragment(menuItem.itemId)

            menuItem.isChecked = true
            title = menuItem.title
        }
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            displaySelectedFragment(it)
            true
        }
    }
}
