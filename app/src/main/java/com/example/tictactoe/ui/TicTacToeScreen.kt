package com.example.tictactoe.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.TictactoeTheme
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun TicTacToeScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TicTacToeContent(
        state = state,
        onCellClick = viewModel::onCellClick,
        onResetClick = viewModel::resetGame,
    )
}

@Composable
fun TicTacToeContent(
    state: GameState,
    onCellClick: (Int) -> Unit,
    onResetClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Tic Tac Toe",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(24.dp))

            StatusText(state = state)

            Spacer(modifier = Modifier.height(32.dp))

            GameBoard(
                board = state.board,
                isGameOver = state.isGameOver,
                onCellClick = onCellClick,
            )

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = state.isGameOver,
                enter = fadeIn() + scaleIn(),
            ) {
                Button(
                    onClick = onResetClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text(text = "Play Again", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun StatusText(state: GameState) {
    val statusText = when {
        state.winner != null -> "Player ${state.winner.symbol} wins!"
        state.isDraw -> "It's a draw!"
        else -> "Player ${state.currentPlayer.symbol}'s turn"
    }
    Text(
        text = statusText,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun GameBoard(
    board: List<Player?>,
    isGameOver: Boolean,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    val index = row * 3 + col
                    GameCell(
                        player = board[index],
                        isClickable = !isGameOver && board[index] == null,
                        cellIndex = index,
                        onClick = { onCellClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCell(
    player: Player?,
    isClickable: Boolean,
    cellIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when (player) {
        Player.X -> MaterialTheme.colorScheme.primaryContainer
        Player.O -> MaterialTheme.colorScheme.secondaryContainer
        null -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (player) {
        Player.X -> MaterialTheme.colorScheme.onPrimaryContainer
        Player.O -> MaterialTheme.colorScheme.onSecondaryContainer
        null -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val cellDescription = when (player) {
        Player.X -> "Cell ${cellIndex + 1}, X"
        Player.O -> "Cell ${cellIndex + 1}, O"
        null -> if (isClickable) "Cell ${cellIndex + 1}, empty, tap to play" else "Cell ${cellIndex + 1}, empty"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(110.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .semantics { contentDescription = cellDescription }
            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        AnimatedVisibility(
            visible = player != null,
            enter = fadeIn() + scaleIn(),
        ) {
            Text(
                text = player?.symbol ?: "",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )
        }
    }
}

private val Player.symbol: String
    get() = when (this) {
        Player.X -> "X"
        Player.O -> "O"
    }

@Preview(showBackground = true)
@Composable
private fun TicTacToePreview() {
    TictactoeTheme {
        TicTacToeContent(
            state = GameState(),
            onCellClick = {},
            onResetClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicTacToeWinnerPreview() {
    TictactoeTheme {
        TicTacToeContent(
            state = GameState(
                board = listOf(Player.X, Player.X, Player.X, Player.O, Player.O, null, null, null, null),
                winner = Player.X,
            ),
            onCellClick = {},
            onResetClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicTacToeDrawPreview() {
    TictactoeTheme {
        TicTacToeContent(
            state = GameState(
                board = listOf(Player.X, Player.O, Player.X, Player.X, Player.X, Player.O, Player.O, Player.X, Player.O),
                isDraw = true,
            ),
            onCellClick = {},
            onResetClick = {},
        )
    }
}
