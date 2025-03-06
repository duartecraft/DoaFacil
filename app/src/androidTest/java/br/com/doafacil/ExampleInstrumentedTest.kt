package br.com.doafacil

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Teste instrumentado para verificar o pacote do aplicativo.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Contexto do aplicativo em teste
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Verifica se o pacote está correto
        assertEquals("br.com.doafacil", appContext.packageName)
    }
} 