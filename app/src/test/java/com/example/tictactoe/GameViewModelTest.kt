package com.example.tictactoe

import com.example.tictactoe.model.Player
import com.example.tictactoe.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        viewModel = GameViewModel()
    }

    @Test
    fun initialState_hasEmptyBoard() {
        assertTrue(viewModel.state.value.board.all { it == null })
    }

    @Test
    fun initialState_playerXGoesFirst() {
        assertEquals(Player.X, viewModel.state.value.currentPlayer)
    }

    @Test
    fun clickingCell_placesCurrentPlayer() {
        viewModel.onCellClick(0)
        assertEquals(Player.X, viewModel.state.value.board[0])
    }

    @Test
    fun clickingOccupiedCell_doesNothing() {
        viewModel.onCellClick(0)
        viewModel.onCellClick(0)
        assertEquals(Player.X, viewModel.state.value.board[0])
        assertEquals(Player.O, viewModel.state.value.currentPlayer)
    }

    @Test
    fun playersAlternateTurns() {
        viewModel.onCellClick(0) // X
        viewModel.onCellClick(1) // O
        assertEquals(Player.X, viewModel.state.value.board[0])
        assertEquals(Player.O, viewModel.state.value.board[1])
    }

    @Test
    fun rowWin_detectsWinner() {
        // X wins top row: 0, 1, 2
        viewModel.onCellClick(0) // X
        viewModel.onCellClick(3) // O
        viewModel.onCellClick(1) // X
        viewModel.onCellClick(4) // O
        viewModel.onCellClick(2) // X wins
        assertEquals(Player.X, viewModel.state.value.winner)
        assertTrue(viewModel.state.value.isGameOver)
    }

    @Test
    fun columnWin_detectsWinner() {
        // O wins first column: 0, 3, 6
        viewModel.onCellClick(1) // X
        viewModel.onCellClick(0) // O
        viewModel.onCellClick(2) // X
        viewModel.onCellClick(3) // O
        viewModel.onCellClick(4) // X
        viewModel.onCellClick(6) // O wins
        assertEquals(Player.O, viewModel.state.value.winner)
    }

    @Test
    fun diagonalWin_detectsWinner() {
        // X wins main diagonal: 0, 4, 8
        viewModel.onCellClick(0) // X
        viewModel.onCellClick(1) // O
        viewModel.onCellClick(4) // X
        viewModel.onCellClick(2) // O
        viewModel.onCellClick(8) // X wins
        assertEquals(Player.X, viewModel.state.value.winner)
    }

    @Test
    fun fullBoard_withoutWinner_isDraw() {
        // Produces: X O X / X X O / O X O
        listOf(0, 1, 2, 5, 3, 6, 4, 8, 7).forEach { viewModel.onCellClick(it) }
        assertTrue(viewModel.state.value.isDraw)
        assertNull(viewModel.state.value.winner)
        assertTrue(viewModel.state.value.isGameOver)
    }

    @Test
    fun clickAfterGameOver_doesNothing() {
        // X wins top row
        viewModel.onCellClick(0)
        viewModel.onCellClick(3)
        viewModel.onCellClick(1)
        viewModel.onCellClick(4)
        viewModel.onCellClick(2) // X wins
        val boardSnapshot = viewModel.state.value.board.toList()
        viewModel.onCellClick(5) // should be ignored
        assertEquals(boardSnapshot, viewModel.state.value.board)
    }

    @Test
    fun resetGame_clearsAllState() {
        viewModel.onCellClick(0)
        viewModel.onCellClick(3)
        viewModel.onCellClick(1)
        viewModel.onCellClick(4)
        viewModel.onCellClick(2) // X wins
        viewModel.resetGame()
        val state = viewModel.state.value
        assertTrue(state.board.all { it == null })
        assertNull(state.winner)
        assertEquals(Player.X, state.currentPlayer)
        assertTrue(!state.isGameOver)
    }
}
