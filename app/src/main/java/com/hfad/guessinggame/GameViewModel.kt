package com.hfad.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val words = listOf("Android", "Studio", "Guessing", "Activity", "Fragment", "Word")
    private val secretWord = words.random().uppercase()
    private var correctGuesses = ""
    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String> get() = _secretWordDisplay
    private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String> get() = _incorrectGuesses
    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft: LiveData<Int> get() = _livesLeft
    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean> get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(ch: String): String {
        return when (correctGuesses.contains(ch)) {
            true -> ch
            else -> "_"
        }
    }

    fun makeGuess(guess: String) {
        if (guess.length != 1) return
        if (secretWord.contains(guess)) {
            correctGuesses += guess
            _secretWordDisplay.value = deriveSecretWordDisplay()
        } else {
            _incorrectGuesses.value += "$guess "
            _livesLeft.value = livesLeft.value?.minus(1)
        }
        if (isWon() || isLost()) {
            _gameOver.value = true
        }
    }

    private fun isWon(): Boolean {
        return secretWord.equals(secretWordDisplay.value, true)
    }

    private fun isLost(): Boolean {
        return livesLeft.value ?: 0 <= 0
    }

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"
        message += " The word was $secretWord"
        return message
    }

    fun finishGame() {
        _gameOver.value = true
    }
}