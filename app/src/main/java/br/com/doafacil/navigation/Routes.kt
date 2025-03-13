package br.com.doafacil.navigation

object Routes {
    const val HOME = "home"
    const val NGO_LIST = "ngo_list"
    const val NGO_DETAILS = "ngo_details/{ngoId}"
    const val DONATION_HISTORY = "donation_history"
    const val AGENDAMENTO = "agendamento/{ngoId}"
    const val LOGIN = "login"
    const val RATE = "Rate"

    // Funções auxiliares para construir rotas com parâmetros
    fun ngoDetails(ngoId: String) = "ngo_details/$ngoId"
    fun agendamento(ngoId: String) = "agendamento/$ngoId"
}