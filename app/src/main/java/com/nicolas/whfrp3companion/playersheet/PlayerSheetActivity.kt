package com.nicolas.whfrp3companion.playersheet

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.fragments.EmptyFragment
import com.nicolas.whfrp3companion.playersheet.characteristics.PlayerCharacteristicsFragment
import com.nicolas.whfrp3companion.playersheet.inventory.PlayerInventoryFragment
import com.nicolas.whfrp3companion.playersheet.skills.PlayerSkillsFragment
import com.nicolas.whfrp3companion.playersheet.state.PlayerStateFragment
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import org.koin.android.ext.android.inject

class PlayerSheetActivity : AppCompatActivity() {
    private lateinit var player: Player

    private val drawer by bind<DrawerLayout>(R.id.playersheet_drawer_layout)
    private val playerFacade by inject<PlayerFacade>()

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playersheet)

        player = playerFacade.find(intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT))!!
        title = "${player.name} - ${getString(player.race.labelId)}"

        ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setupDrawerContent(navigationView)

        displaySelectedFragment(R.id.nav_player_characteristics)
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
            R.id.nav_player_characteristics -> PlayerCharacteristicsFragment.newInstance(player.name)
            R.id.nav_player_state -> PlayerStateFragment.newInstance(player.name)
            R.id.nav_player_skills -> PlayerSkillsFragment.newInstance(player.name)
            R.id.nav_player_inventory -> PlayerInventoryFragment.newInstance(player.name)
//            R.id.nav_player_actions -> PlayerActionsFragment()
//            R.id.nav_player_talents -> PlayerTalentsFragment()
            else -> EmptyFragment.newInstance()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.playersheet_content_frame, fragment)
                .commit()

        drawer.closeDrawer(GravityCompat.START)
        closeKeyboard()
    }

    private fun displaySelectedFragment(menuItem: MenuItem) {
        displaySelectedFragment(menuItem.itemId)

        menuItem.isChecked = true
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            displaySelectedFragment(it)
            true
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(drawer.windowToken, 0)
    }
}
