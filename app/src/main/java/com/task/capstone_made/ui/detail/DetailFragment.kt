package com.task.capstone_made.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.task.capstone_made.R
import com.task.capstone_made.databinding.DetailFragmentBinding
import com.task.capstone_made.ui.follow.FollowFragment
import com.task.core.domain.model.User
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    private lateinit var binding: DetailFragmentBinding
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var user: User
    private var isFavorite = false
    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = args.username
        binding = DetailFragmentBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        observeDetail()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabList = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following)
        )
        pagerAdapter = PagerAdapter(tabList, args.username, this)
        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun observeDetail() {

        detailViewModel.detailUsers(args.username).observe(viewLifecycleOwner, {
            when (it) {
                is com.task.core.data.Resource.Success -> {
                    user = it.data!!
                    binding.data = it.data
                    detailViewModel.getDetailState(args.username)
                        ?.observe(viewLifecycleOwner, { user ->
                            isFavorite = user.isFavorite == true
                            changedFavorite(isFavorite)
                        })
                    binding.fabFavorite.show()
                }

                is com.task.core.data.Resource.Error -> {
                    binding.fabFavorite.hide()
                }

                is com.task.core.data.Resource.Loading -> {
                    binding.fabFavorite.hide()
                }
            }
            changedFavorite(isFavorite)
            binding.fabFavorite.setOnClickListener {
                addOrRemoveFavorite()
                changedFavorite(isFavorite)
            }
        })
    }

    private fun addOrRemoveFavorite() {
        if (!isFavorite) {
            user.isFavorite = !isFavorite
            detailViewModel.insertFavorite(user)
            Toast.makeText(
                binding.root.context,
                resources.getString(R.string.favorite_add, user.login),
                Toast.LENGTH_SHORT
            ).show()
            isFavorite = !isFavorite
        } else {
            user.isFavorite = !isFavorite
            detailViewModel.deleteFavorite(user)
            Toast.makeText(
                binding.root.context,
                resources.getString(R.string.favorite_remove, user.login),
                Toast.LENGTH_SHORT
            ).show()
            isFavorite = !isFavorite
        }
    }

    private fun changedFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    inner class PagerAdapter(
        private val tabList: Array<String>,
        private val username: String,
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = tabList.size

        override fun createFragment(position: Int): Fragment =
            FollowFragment.newInstance(username, tabList[position])
    }
}