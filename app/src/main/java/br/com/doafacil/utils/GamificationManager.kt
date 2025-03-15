package br.com.doafacil.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object GamificationManager {
    private lateinit var prefs: SharedPreferences


    fun init(context: Context) {
        prefs = context.getSharedPreferences("gamification_prefs", Context.MODE_PRIVATE)
    }

    // Adicionaos os pontos com base na ação
    fun addPointsForAction(action: GamificationAction) {
        if (!this::prefs.isInitialized) {
            throw IllegalStateException("GamificationManager não foi inicializado. Chame GamificationManager.init(context) antes de usar.")
        }

        val currentPoints = getPoints()
        val newPoints = currentPoints + action.points
        prefs.edit { putInt("points", newPoints) }
    }

    // Retorna a pontuação atual do usuário
    fun getPoints(): Int {
        if (!this::prefs.isInitialized) {
            throw IllegalStateException("GamificationManager não foi inicializado.")
        }
        return prefs.getInt("points", 0)
    }

    // Retorna o nível do usuário baseado na pontuação
    fun getLevel(): String {
        val points = getPoints()
        return when {
            points >= 50000 -> "Rubi"
            points >= 10000 -> "Safira"
            points >= 5000 -> "Diamante"
            points >= 1000 -> "Ouro"
            points >= 500 -> "Prata"
            points >= 100 -> "Bronze"
            else -> "Iniciante"
        }
    }

    // Verifica se o usuário já avaliou o app para não ganhar pontos mais de uma vez
    fun hasRatedBefore(): Boolean {
        return prefs.getBoolean("hasRated", false)
    }

    // Marca que o usuário já fez uma avaliação do app
    fun markAsRated() {
        prefs.edit { putBoolean("hasRated", true) }
    }
}

// Enum para definir os tipos de ações e seus pontos associados
enum class GamificationAction(val points: Int) {
    DONATION(10),
    FIRST_REVIEW(10),
    APPOINTMENT(1)
}