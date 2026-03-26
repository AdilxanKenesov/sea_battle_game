package uz.gita.seabattle.presenter.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import uz.gita.seabattle.R
import uz.gita.seabattle.databinding.HomeScreenBinding
import uz.gita.seabattle.presenter.adapter.MyChipsAdapter

class HomeScreen: Fragment(R.layout.home_screen) {
    private val binding by viewBinding(HomeScreenBinding::bind)
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }

    private val gameId by lazy { arguments?.getString("gameId") ?: "" }
    private val isPlayer1 by lazy { arguments?.getBoolean("isPlayer1") ?: false }
    private var lastSelectedView: View? = null
    private val adapter = MyChipsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.txtUserId.text = "Game ID: $gameId"
        viewModel.startListening(gameId)

        adapter.setOnItemClickListener {id ->
            if (viewModel.placeShip(id)){
                lastSelectedView?.visibility = View.INVISIBLE
                lastSelectedView = null
            }else{
                Toast.makeText(context,"xato", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvBattleground.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.katak.collect { adapter.submitList(it) }
            }
        }

        binding.chip5.setOnClickListener {
            viewModel.selectShipSize(5)
            lastSelectedView = it
        }
        binding.chip4.setOnClickListener {
            viewModel.selectShipSize(4)
            lastSelectedView = it
        }
        binding.chip3.setOnClickListener {
            viewModel.selectShipSize(3)
            lastSelectedView = it }
        binding.chip2.setOnClickListener {
            viewModel.selectShipSize(2)
            lastSelectedView = it
        }

        binding.btnStart.setOnClickListener {
            viewModel.finishSetup(gameId, isPlayer1)
            Toast.makeText(context, "Raqibni kuting", Toast.LENGTH_SHORT).show()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.gameData.collect { game ->
                    if (game?.player1?.isReady == true && game.player2?.isReady == true) {
                        findNavController().navigate(R.id.action_homeScreen_to_gameScreen)
                    }
                }
            }
        }
    }

}