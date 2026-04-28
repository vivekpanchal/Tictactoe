package com.example.tictactoe.model

enum class Player { X, O }

data class GameState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
) {
    val isGameOver: Boolean get() = winner != null || isDraw
}
