package app.project.com.hygieia

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_front_screen.*
import kotlinx.android.synthetic.main.app_bar_front_screen.*

class FrontScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnTouchAdd {






    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_screen)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        var fragment=HomeFragment(applicationContext,this)
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment,fragment)
        fragmentTransaction.commit()



    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.home->{
                var fragment=HomeFragment(applicationContext,this)
                fragmentManager = getSupportFragmentManager()
                fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment,fragment)
                fragmentTransaction.commit()
            }


            R.id.hydration -> {
                // Handle the camera action
                val hydra=Hydraaation(this)
                fragmentManager = getSupportFragmentManager()
                fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment,hydra)
                fragmentTransaction.commit()
            }

            R.id.Progress->{
                val Progress=WeightTracker(this)
                fragmentManager = getSupportFragmentManager()
                fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment,Progress)
                fragmentTransaction.commit()
            }

            R.id.settings->{
                var fragment=settings(applicationContext)
                fragmentManager = getSupportFragmentManager()
                fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment,fragment)
                fragmentTransaction.commit()
            }
            R.id.goal->{
                var fragment=GoalSettingFragment()
                fragmentManager = getSupportFragmentManager()
                fragmentTransaction=fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment,fragment)
                fragmentTransaction.commit()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun ontouchadd() {
        var fragment=SearchFoodFragment()
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment,fragment)
        fragmentTransaction.commit()
    }

}
