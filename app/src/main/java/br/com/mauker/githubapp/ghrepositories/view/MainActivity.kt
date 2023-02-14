package br.com.mauker.githubapp.ghrepositories.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mauker.githubapp.R
import br.com.mauker.githubapp.databinding.ActivityMainBinding
import br.com.mauker.materialsearchview.MaterialSearchView
import br.com.mauker.materialsearchview.db.model.History
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var pageAdapter: GhRepositoryListAdapter? = null
    private var curQuery: String? = null

    //region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupSearchView()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.onViewResumed()
    }

    override fun onStop() {
        super.onStop()
        binding.searchView.onViewStopped()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.i("Saving state.")
        Timber.i("Current query: $curQuery")
        curQuery?.let {
            outState.putString(KEY_CURRENT_QUERY, it)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Timber.i("Restoring state.")

        savedInstanceState.getString(KEY_CURRENT_QUERY)?.let {
            Timber.i("Restored query: $it")
            curQuery = it
            lifecycleScope.launch {
                viewModel.restoreGhRepositories()?.collectLatest { pagingData ->
                    pageAdapter?.submitData(pagingData)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                // Open the search view on the menu item click.
                binding.searchView.openSearch()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (searchWrd.isNotBlank()) {
                    binding.searchView.setQuery(searchWrd, false)
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //endregion

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.searchView.isOpen) {
            // Close the search on the back button press.
            binding.searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    //region private utility methods
    private fun setupSearchView() {
        val clickListener = object: MaterialSearchView.OnHistoryItemClickListener {
            override fun onClick(history: History) {
                binding.searchView.setQuery(history.query, false)
            }

            override fun onLongClick(history: History) = Unit
        }

        binding.searchView.run {
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Timber.i("Search query submitted. Query: $query")
                    runQuery(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
            setOnItemClickListener(clickListener)
            adjustTintAlpha(0.8f)
        }
    }

    private fun setupRecyclerView() {
        val ghAdapter = GhRepositoryListAdapter()
        pageAdapter = ghAdapter
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isTablet = resources.getBoolean(R.bool.isTablet)
        binding.rvRepositories.run {
            layoutManager = when {
                isLandscape && !isTablet || !isLandscape && isTablet -> {
                    GridLayoutManager(this@MainActivity, 2)
                }
                // We can assume that it's a tablet as well
                isLandscape -> {
                    GridLayoutManager(this@MainActivity, 3)
                }
                else -> {
                    LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
            adapter = pageAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ghAdapter.loadStateFlow.collect {
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading || it.source.refresh is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                    binding.txtInitState.isVisible = it.source.refresh is LoadState.NotLoading && ghAdapter.itemCount == 0
                    when {
                        it.source.append is LoadState.Error ||
                        it.source.prepend is LoadState.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.error_loading_data),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun runQuery(query: String) {
        curQuery = query
        if (query.isNotBlank()) {
            lifecycleScope.launch {
                viewModel.getGhRepositories(query).collectLatest {
                    pageAdapter?.submitData(it)
                }
            }
        }
    }
    //endregion

    companion object {
        private const val KEY_CURRENT_QUERY = "KEY_CURRENT_QUERY"
    }
}