package com.task.capstone_made.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.capstone_made.R
import com.task.capstone_made.databinding.FollowFragmentBinding
import com.task.capstone_made.ui.detail.DetailFragmentDirections
import com.task.core.ui.UserAdapter
import com.task.core.utils.TypeView
import org.koin.android.viewmodel.ext.android.viewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FollowFragmentBinding
    private lateinit var followAdapter: UserAdapter
    private lateinit var username: String
    private var type: String? = null
    private val followViewModel: FollowViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
            type = it.getString(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowFragmentBinding.inflate(layoutInflater, container, false)
        binding.progress.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(
                DetailFragmentDirections.actionDeatilFragmentToDetailFragment(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        binding.recylerFollow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = followAdapter
        }

        when (type) {
            resources.getString(R.string.following) -> followViewModel.setFollow(
                username,
                TypeView.FOLLOWING
            )
            resources.getString(R.string.followers) -> followViewModel.setFollow(
                username,
                TypeView.FOLLOWER
            )
            else -> onErrorState(binding, null)
        }

        observeFollow()
    }

    private fun observeFollow() {
        followViewModel.favoriteUsers.observe(viewLifecycleOwner, {
            it.let {
                when (it) {
                    is com.task.core.data.Resource.Success ->
                        if (!it.data.isNullOrEmpty()) {
                            onSuccessState(binding)
                            followAdapter.run { setData(it.data) }
                        } else {
                            onErrorState(
                                binding,
                                resources.getString(R.string.not_have, username, type)
                            )
                        }
                    is com.task.core.data.Resource.Loading -> onLoadingState(binding)
                    is com.task.core.data.Resource.Error -> onErrorState(
                        binding,
                        it.message
                    )
                }
            }
        })
    }

    private fun onSuccessState(followFragmentBinding: FollowFragmentBinding?) {
        followFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.GONE
            recylerFollow.visibility = View.VISIBLE
        }
    }

    private fun onLoadingState(followFragmentBinding: FollowFragmentBinding?) {
        followFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.VISIBLE
            recylerFollow.visibility = View.GONE
        }
    }

    private fun onErrorState(
        followFragmentBinding: FollowFragmentBinding?,
        message: String?
    ) {
        followFragmentBinding?.apply {
            errLayout.apply {
                mainNotFound.visibility = View.VISIBLE
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            progress.visibility = View.GONE
            recylerFollow.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(username: String, type: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }

        private const val USERNAME = "username"
        private const val TYPE = "type"
    }
}