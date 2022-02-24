package com.task.favorite.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.capstone_made.R
import com.task.capstone_made.databinding.FavoriteFragmentBinding
import com.task.core.ui.UserAdapter
import com.task.favorite.di.favoriteModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: UserAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.favorite)
        _binding = FavoriteFragmentBinding.inflate(layoutInflater, container, false)
        loadKoinModules(favoriteModule)
        binding.progress.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = UserAdapter(arrayListOf()) {username, iv ->
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        binding.recyclerFav.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }

        observeDetail()
    }

    private fun observeDetail() {
        onLoadingState(favoriteFragmentBinding = binding)
        favoriteViewModel.favoriteUsers.observe(viewLifecycleOwner, {
            it.let {
                if (!it.isNullOrEmpty()) {
                    onSuccessState(favoriteFragmentBinding = binding)
                    favoriteAdapter.setData(it)
                } else {
                    onErrorState(
                        binding,
                        resources.getString(R.string.not_have, "", resources.getString(R.string.favorite))
                    )
                }
            }
        })
    }
     private fun onSuccessState(favoriteFragmentBinding: FavoriteFragmentBinding?) {
        favoriteFragmentBinding?.apply {
            errlayout.mainNotFound.visibility = View.GONE
            binding.progress.visibility = View.GONE
            recyclerFav.visibility = View.VISIBLE
        }
    }
     private fun onLoadingState(favoriteFragmentBinding: FavoriteFragmentBinding?) {
        favoriteFragmentBinding?.apply {
            errlayout.mainNotFound.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            recyclerFav.visibility = View.GONE
        }
    }
     private fun onErrorState(favoriteFragmentBinding: FavoriteFragmentBinding?,
                              message: String?) {
        favoriteFragmentBinding?.apply {
            errlayout.apply {
                mainNotFound.visibility = View.VISIBLE
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            binding.progress.visibility = View.GONE
            recyclerFav.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        binding.recyclerFav.adapter = null
        _binding = null
        super.onDestroyView()
    }
}