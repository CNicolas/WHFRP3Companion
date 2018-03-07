package com.nicolas.whfrp3companion

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.nicolas.whfrp3companion.players.PlayersFragment

class MainActivity : AppCompatActivity()/*, NavigationView.OnNavigationItemSelectedListener*/ {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
//        displaySelectedFragment(R.id.nav_home)
    }
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        val toggle = ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer.setDrawerListener(toggle)
//        toggle.syncState()
//
//        val navigationView = findViewById(R.id.nav_view) as NavigationView
//        navigationView.setNavigationItemSelectedListener(this)
//    }
//
//    override fun onBackPressed() {
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        val id = item.itemId
//
//        displaySelectedFragment(id)
//        return true
//    }
//
    private fun displaySelectedFragment(id: Int) {
        val fragment: Fragment? = when (id) {
            R.id.nav_home -> PlayersFragment()
//            R.id.nav_talents -> TalentTypesFragment()
//            R.id.nav_careers -> CareersFragment()
//            R.id.nav_items -> ItemsFragment()
//            R.id.nav_skills -> SkillsFragment()
//            R.id.nav_specializations -> SpecializationsFragment()
            else -> null
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment)
            ft.commit()
        }

//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        drawer.closeDrawer(GravityCompat.START)
    }
}
