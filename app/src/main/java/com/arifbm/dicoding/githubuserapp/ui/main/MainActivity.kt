package com.arifbm.dicoding.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arifbm.dicoding.githubuserapp.R
import com.arifbm.dicoding.githubuserapp.data.local.SettingPreferences
import com.arifbm.dicoding.githubuserapp.data.model.User
import com.arifbm.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.arifbm.dicoding.githubuserapp.ui.detail.DetailUserActivity
import com.arifbm.dicoding.githubuserapp.ui.favorite.FavoriteActivity
import com.arifbm.dicoding.githubuserapp.ui.theme.ThemeViewModel
import com.arifbm.dicoding.githubuserapp.ui.theme.ThemeViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var themeViewModel: ThemeViewModel
    private var isDarkMode: Boolean = false

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val pref = SettingPreferences.getInstance(dataStore)
        themeViewModel = ViewModelProvider(this@MainActivity, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        themeViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    isDarkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    isDarkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        adapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {

                searchUser()
            }

            editQuery.setOnKeyListener { view, keyCode, event ->

                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.proggresBar.visibility = View.VISIBLE
        } else {
            binding.proggresBar.visibility = View.GONE
        }
    }

    private fun searchUser() {
        binding.apply {
            val query = editQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.action_light -> {
                if (isDarkMode) {
                    themeViewModel.saveThemeSetting(!isDarkMode)
                }
                msgShow("Light Theme")
            }
            R.id.action_dark -> {
                if (!isDarkMode) {
                    themeViewModel.saveThemeSetting(!isDarkMode)
                }
                msgShow("Dark Theme")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}