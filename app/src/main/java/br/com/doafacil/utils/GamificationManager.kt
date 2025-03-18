package br.com.doafacil.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object GamificationManager {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("gamification_prefs", Context.MODE_PRIVATE)
    }

    fun addPointsForAction(action: GamificationAction) {
        val sharedPrefs = prefs ?: throw IllegalStateException("GamificationManager não foi inicializado. Chame GamificationManager.init(context) antes de usar.")

        val currentPoints = getPoints()
        val newPoints = currentPoints + action.points
        sharedPrefs.edit { putInt("points", newPoints) }
    }

    fun getPoints(): Int {
        return prefs?.getInt("points", 0) ?: 0
    }

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
        return prefs?.getBoolean("hasRated", false) ?: false
    }

    // Marca que o usuário já fez uma avaliação do app
    fun markAsRated() {
        prefs?.edit { putBoolean("hasRated", true) }
    }
}

// Enum para definir os tipos de ações e seus pontos associados
enum class GamificationAction(val points: Int) {
    DONATION(10),
    FIRST_REVIEW(10),
    APPOINTMENT(1)
}