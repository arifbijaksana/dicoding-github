package com.arifbm.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifbm.dicoding.githubuserapp.R
import com.arifbm.dicoding.githubuserapp.databinding.FragmentFollowBinding
import com.arifbm.dicoding.githubuserapp.ui.main.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_follow) {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        _binding = FragmentFollowBinding.bind(view)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner,{
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.proggresBar.visibility = View.VISIBLE
        } else {
            binding.proggresBar.visibility = View.GONE
        }
    }

}