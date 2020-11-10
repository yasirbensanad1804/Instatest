package com.example.instatest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.instatest.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.setOnNavigationItemSelectedListener(bottomnavListener)
        val fragmentt = supportFragmentManager.beginTransaction()
        fragmentt.add(R.id.container, HomeFragment())
        fragmentt.commit()

    }
    private val bottomnavListener = BottomNavigationView.OnNavigationItemSelectedListener { a -> var bottomnav: Fragment = HomeFragment()
        when(a.itemId) {
            R.id.navigation_home -> {
                bottomnav = HomeFragment()
            }
            R.id.navigation_explore -> {
                bottomnav = ExploreFragment()
            }
            R.id.navigation_account -> {
                bottomnav = AccountFragment()
            }
            R.id.navigation_activity -> {
                bottomnav = ActivityFragment()
            }
            R.id.navigation_post -> {
                startActivity(Intent(this, AddPostActivity::class.java))
            }
        }
        val fragmentt = supportFragmentManager.beginTransaction()
        fragmentt.replace(R.id.container,bottomnav)
        fragmentt.commit()

        true
    }

}