package uz.gita.seabattle.presenter.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.seabattle.R
import uz.gita.seabattle.databinding.GameScreenBinding

class GameScreen: Fragment(R.layout.game_screen) {
    private val binding by viewBinding(GameScreenBinding::bind)
    private val viewModel: GameViewModel by viewModels{ GameViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}