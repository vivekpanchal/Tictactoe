package com.example.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val WINNING_COMBINATIONS = listOf(
    listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // rows
    listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // columns
    listOf(0, 4, 8), listOf(2, 4, 6),                   // diagonals
)

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun onCellClick(index: Int) {
        val current = _state.value
        if (current.isGameOver || current.board[index] != null) return

        val newBoard = current.board.toMutableList().also { it[index] = current.currentPlayer }
        val winner = checkWinner(newBoard)
        val isDraw = winner == null && newBoard.none { it == null }

        _state.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (current.currentPlayer == Player.X) Player.O else Player.X,
                winner = winner,
                isDraw = isDraw,
            )
        }
    }

    fun resetGame() {
        _state.value = GameState()
    }

    private fun checkWinner(board: List<Player?>): Player? =
        WINNING_COMBINATIONS.firstNotNullOfOrNull { (a, b, c) ->
            board[a]?.takeIf { it == board[b] && it == board[c] }
        }
}
