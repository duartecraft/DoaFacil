package br.com.doafacil.data

import br.com.doafacil.model.NGO

object NGORepository {
    val ngos = listOf(
        NGO("1", "Amigos do Bem", "São Paulo, SP", "Combate à fome e pobreza em regiões carentes"),
        NGO("2", "Médicos Sem Fronteiras", "Rio de Janeiro, RJ", "Assistência médica humanitária em áreas de conflito"),
        NGO("3", "WWF Brasil", "Brasília, DF", "Conservação da natureza e redução do impacto humano"),
        NGO("4", "UNICEF Brasil", "São Paulo, SP", "Defesa dos direitos das crianças e adolescentes"),
        NGO("5", "Teto Brasil", "São Paulo, SP", "Construção de moradias emergenciais para famílias vulneráveis")
    )

    //Retorna a lista de todas as ONGs disponíveis.
    fun getAllNGOs(): List<NGO> = ngos

    //Busca uma ONG pelo ID e trata exceções de forma segura.
    fun getNGOById(id: String): NGO {
        return ngos.find { it.id == id } ?: throw IllegalArgumentException("ONG com ID $id não encontrada")
    }
}
