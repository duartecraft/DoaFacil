package br.com.doafacil.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes

object GoogleAuthHelper {
    // Código de requisição para identificar o retorno do login
    const val RC_SIGN_IN = 1001

    // Guardam as callbacks que serão chamadas depois, no handleSignInResult
    private var successCallback: ((GoogleSignInAccount) -> Unit)? = null
    private var failureCallback: ((String) -> Unit)? = null

    /**
     * Inicia o fluxo de login do Google, solicitando acesso ao e-mail do usuário
     * e ao escopo de Calendar (para criar eventos no calendário).
     *
     * @param activity A Activity que chamará o startActivityForResult
     * @param onSuccess Callback chamada em caso de sucesso, recebendo o GoogleSignInAccount
     * @param onFailure Callback chamada em caso de falha, recebendo a mensagem de erro
     */
    fun signInGoogle(
        activity: Activity,
        onSuccess: (GoogleSignInAccount) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Armazena as callbacks em variáveis estáticas
        successCallback = onSuccess
        failureCallback = onFailure

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            // Solicita o escopo de acesso ao Google Calendar
            .requestScopes(Scope(CalendarScopes.CALENDAR))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent

        // Inicia a Activity do Google Sign-In
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Trata o resultado do login, retornando um GoogleSignInAccount
     * caso o usuário tenha logado com sucesso e concedido as permissões.
     *
     * @param data O Intent retornado pelo Google Sign-In
     *  (geralmente vem do onActivityResult na Activity)
     */
    fun handleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                successCallback?.invoke(account)
            } else {
                failureCallback?.invoke("Conta não encontrada ou inválida.")
            }
        } catch (e: ApiException) {
            failureCallback?.invoke("Erro ao processar sign-in: ${e.localizedMessage}")
        } catch (e: Exception) {
            failureCallback?.invoke("Erro inesperado: ${e.localizedMessage}")
        } finally {
            // Limpa as callbacks para evitar manter referências após o login
            successCallback = null
            failureCallback = null
        }
    }
}
