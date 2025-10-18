package com.android.example.pruebaprueba.repositorys

import com.android.example.pruebaprueba.config.AppDatabase
import com.android.example.pruebaprueba.models.Articulo
import com.android.example.pruebaprueba.models.ArticuloConCantidad
import com.android.example.pruebaprueba.models.Persona
import com.android.example.pruebaprueba.models.PersonaConTotal
import java.io.Serializable



suspend fun calcularConsumoPorPersona(facturaId: Int, db: AppDatabase): List<PersonaConTotal> {
    val articulos = db.articuloDao().obtenerArticulosPorFacturaId(facturaId)
    val relaciones = db.articulo_persona().obtenerTodosArticuloPersona()
    val personas = db.personaDao().obtenerPersonas()

    val consumoMap = mutableMapOf<Int, MutableList<ArticuloConCantidad>>()

    for (art in articulos) {
        val relacionadosAP = relaciones.filter { it.articuloId == art.id }
        //for(relacion in relaciones)  it

        if (relacionadosAP.isEmpty()) {
            val listaSinAsignar = consumoMap.getOrPut(-1) { mutableListOf() }
            listaSinAsignar.add(ArticuloConCantidad(art, art.precioUnitario * art.cantidad, art.cantidad))
        } else {
            val cantidadTotalAsignada = relacionadosAP.sumOf { it.cantidadIndividual }

            for (relacion in relacionadosAP) {
                val personaId = relacion.personaId
                val cantidadAsignada = relacion.cantidadIndividual
                val costo = art.precioUnitario * cantidadAsignada

                val listaPersona = consumoMap.getOrPut(personaId) { mutableListOf() }
                listaPersona.add(ArticuloConCantidad(art, costo, cantidadAsignada))
            }

            if (cantidadTotalAsignada < art.cantidad) {
                val cantidadSinAsignar = art.cantidad - cantidadTotalAsignada
                val listaSinAsignar = consumoMap.getOrPut(-1) { mutableListOf() }
                listaSinAsignar.add(ArticuloConCantidad(art, art.precioUnitario * cantidadSinAsignar, cantidadSinAsignar))
            }
        }
    }

    val resultado = mutableListOf<PersonaConTotal>()

    for (persona in personas) {
        val articulosYcostos = consumoMap[persona.id] ?: emptyList()
        val total = articulosYcostos.sumOf { it.costo }
        resultado.add(PersonaConTotal(persona.id, "${persona.nombre} ${persona.apellido}", total, articulosYcostos))
    }

    val sinAsignarArticulos = consumoMap[-1]
    if (!sinAsignarArticulos.isNullOrEmpty()) {
        val totalSinAsignar = sinAsignarArticulos.sumOf { it.costo }
        resultado.add(PersonaConTotal(-1, "Sin asignar", totalSinAsignar, sinAsignarArticulos))
    }

    return resultado
}

/*
suspend fun calcularConsumoPorPersona2(facturaId: Int, db: AppDatabase): List<PersonaConTotal> {
    val articulos = db.articuloDao().obtenerArticulosPorFacturaId(facturaId)
    val relaciones = db.articulo_persona().obtenerTodosArticuloPersona()
    val personas = db.personaDao().obtenerPersonas()

    // Mapa personaId -> lista de pares (Articulo, costo)
    val consumoMap = mutableMapOf<Int, MutableList<Pair<Articulo, Double>>>()

    for (art in articulos) {
        // Obtengo las relaciones de este artículo
        val relacionadosAP = relaciones.filter { it.articuloId == art.id }

        if (relacionadosAP.isEmpty()) {
            // No hay asignación para este artículo -> lo agrego bajo "Sin asignar" (id = -1)
            val listaSinAsignar = consumoMap.getOrPut(-1) { mutableListOf() }
            listaSinAsignar.add(Pair(art, art.precioUnitario * art.cantidad)) // costo total del artículo completo
        } else {
            // Sí hay asignaciones para este artículo
            // Sumar todas las cantidades asignadas para repartir el costo proporcionalmente
            val cantidadTotalAsignada = relacionadosAP.sumOf { it.cantidadIndividual }

            for (relacion in relacionadosAP) {
                val personaId = relacion.personaId
                val cantidadAsignada = relacion.cantidadIndividual
                // Calculamos costo proporcional para esta persona
                val costo = art.precioUnitario * cantidadAsignada

                val listaPersona = consumoMap.getOrPut(personaId) { mutableListOf() }
                listaPersona.add(Pair(art, costo))
            }

            // Si la suma de cantidades asignadas es menor que la cantidad total del artículo,
            // significa que queda una parte sin asignar, la agregamos también a "Sin asignar"
            if (cantidadTotalAsignada < art.cantidad) {
                val cantidadSinAsignar = art.cantidad - cantidadTotalAsignada
                val listaSinAsignar = consumoMap.getOrPut(-1) { mutableListOf() }
                listaSinAsignar.add(Pair(art, art.precioUnitario * cantidadSinAsignar))
            }
        }
    }

    // Ahora crear la lista final de PersonaConTotal
    val resultado = mutableListOf<PersonaConTotal>()

    // Agregar personas con sus consumos
    for (persona in personas) {
        val articulosYcostos = consumoMap[persona.id] ?: emptyList()
        val total = articulosYcostos.sumOf { it.second }
        resultado.add(PersonaConTotal(persona, articulosYcostos, total))
    }

    // Agregar "Sin asignar" con id = -1
    val sinAsignarArticulos = consumoMap[-1]
    if (!sinAsignarArticulos.isNullOrEmpty()) {
        val personaSinAsignar = Persona(id = -1, nombre = "Sin asignar", apellido = "")
        val totalSinAsignar = sinAsignarArticulos.sumOf { it.second }
        resultado.add(PersonaConTotal(personaSinAsignar, sinAsignarArticulos, totalSinAsignar))
    }

    return resultado
}
 */