package uz.gita.seabattle.presenter.ui.first

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import uz.gita.seabattle.R
import uz.gita.seabattle.databinding.FirstScreenBinding

class FirstScreen : Fragment(R.layout.first_screen) {
    private val binding by viewBinding(FirstScreenBinding::bind)
    private val viewModel: FirstViewModel by viewModels { FirstViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            viewModel.createNewGame()

        }

        binding.btnJoin.setOnClickListener {
            viewModel.signInOnly()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    binding.btnStart.isEnabled = !isLoading
                    binding.btnJoin.isEnabled = !isLoading
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.createGameSuccess.collect { gameId ->
                    val bundle = Bundle().apply {
                        putString("gameId", gameId)
                        putBoolean("isPlayer1", true)
                    }
                    findNavController().navigate(R.id.action_firstScreen_to_homeScreen, bundle)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loginOnlySuccess.collect {
                    findNavController().navigate(R.id.action_firstScreen_to_secondScreen)
                }
            }
        }
    }
}