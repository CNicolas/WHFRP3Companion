package com.nicolas.whfrp3companion

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.nicolas.whfrp3companion.fragments.DiceRollerFragment
import com.nicolas.whfrp3companion.fragments.EmptyFragment
import com.nicolas.whfrp3companion.fragments.actions.ActionsFragment
import com.nicolas.whfrp3companion.fragments.players.PlayersFragment
import com.nicolas.whfrp3companion.fragments.skills.SkillsFragment
import com.nicolas.whfrp3companion.fragments.talents.TalentTypesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, mainDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mainDrawerLayout.addDrawerListener(toggle)

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
        val fragment = when (menuItemId) {
            R.id.nav_home -> PlayersFragment.newInstance()
            R.id.nav_dice_roller -> DiceRollerFragment.newInstance()
            R.id.nav_skills -> SkillsFragment.newInstance()
            R.id.nav_talents -> TalentTypesFragment.newInstance()
            R.id.nav_actions -> ActionsFragment.newInstance()
            else -> EmptyFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_frame, fragment)
                .commit()

        mainDrawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            displaySelectedFragment(it.itemId)

            it.isChecked = true
            title = it.title

            true
        }
    }
}
