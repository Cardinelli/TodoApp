package com.cst.todotasks.extensions

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(mainFragmentId: Int, baseFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(mainFragmentId, baseFragment, baseFragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }