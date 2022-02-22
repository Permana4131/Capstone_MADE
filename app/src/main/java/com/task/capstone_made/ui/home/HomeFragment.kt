package com.task.capstone_made.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.capstone_made.R
import com.task.capstone_made.databinding.HomeFragmentBinding
import com.task.core.ui.UserAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.progress.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home)
        homeViewModel.setSearch(randomName())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            userAdapter = UserAdapter(arrayListOf()) { username, iv ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(username),
                    FragmentNavigatorExtras(iv to username)
                )
            }
            binding.recyclerHome.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = userAdapter
            }

            binding.searchView.apply {
                queryHint = resources.getString(R.string.search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        homeViewModel.setSearch(query)
                        binding.searchView.clearFocus()
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean = false
                })
            }


            homeViewModel.users.observe(viewLifecycleOwner, { users ->
                if (users != null) {
                    when (users) {
                        is com.task.core.data.Resource.Success -> {
                            onSuccessState(binding)
                            userAdapter.setData(users.data)
                        }
                        is com.task.core.data.Resource.Loading -> onLoadingState(binding)
                        is com.task.core.data.Resource.Error -> onErrorState(
                            binding,
                            users.message
                        )

                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onSuccessState(
        homeFragmentBinding: HomeFragmentBinding?
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.GONE
            recyclerHome.visibility = View.VISIBLE
            resources
        }
    }

    private fun onLoadingState(
        homeFragmentBinding: HomeFragmentBinding?,
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.VISIBLE
            recyclerHome.visibility = View.GONE
        }
    }

    private fun onErrorState(
        homeFragmentBinding: HomeFragmentBinding?,
        message: String?
    ) {
        homeFragmentBinding?.apply {
            errLayout.apply {
                mainNotFound.visibility = View.VISIBLE
                if (message == null) {
                    emptyText.text = resources.getString(R.string.not_found)
                    ivSearch.setImageResource(R.drawable.ic_search_reset)
                } else {
                    emptyText.text = message
                    ivSearch.setImageResource(R.drawable.ic_search_off)
                }
            }
            progress.visibility = View.GONE
            recyclerHome.visibility = View.GONE
        }
    }

    private fun randomName(): String = List(3) {
        (('a'..'z') + ('A'..'Z')).random()
    }.joinToString("")

}