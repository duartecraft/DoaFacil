package br.com.doafacil.navigation

object Routes {
    const val HOME = "home"
    const val NGO_LIST = "ngo_list"
    const val NGO_DETAILS = "ngo_details/{ngoId}"
    const val DONATION_HISTORY = "donation_history"
    const val AGENDAMENTO = "agendamento/{ngoId}"
    const val PAYMENT = "payment/{ngoId}/{ngoName}"
    const val LOGIN = "login"
    const val RATE = "Rate"
    const val DONATION_CONFIRMATION = "donation_confirmation/{ngoId}/{ngoName}"


    fun ngoDetails(ngoId: String) = "ngo_details/$ngoId"
    fun agendamento(ngoId: String) = "agendamento/$ngoId"
    fun payment(ngoId: String, ngoName: String) = "payment/$ngoId/$ngoName"
    fun donationConfirmation(ngoId: String, ngoName: String) = "donation_confirmation/$ngoId/$ngoName"
}