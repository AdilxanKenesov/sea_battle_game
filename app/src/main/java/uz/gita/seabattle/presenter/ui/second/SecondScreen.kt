package uz.gita.seabattle.presenter.ui.second

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
import uz.gita.seabattle.databinding.SecondScreenBinding

class SecondScreen: Fragment(R.layout.second_screen) {
    private val binding by viewBinding(SecondScreenBinding::bind)
    private val viewModel: SecondViewModel by viewModels{ SecondViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            val gameId = binding.editTextId.text.toString()
            viewModel.joinGame(gameId)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.joinSuccess.collect {
                    val gameId = binding.editTextId.text.toString()
                    val bundle = Bundle().apply {
                        putString("gameId", gameId)
                        putBoolean("isPlayer1", false)
                    }
                    findNavController().navigate(R.id.action_secondScreen_to_homeScreen, bundle)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    binding.btnStart.isEnabled = !isLoading
                }
            }
        }

    }

}