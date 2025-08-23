package com.ikerfah.thmanyah

import com.ikerfah.thmanyah.data.mapper.toDomain
import com.ikerfah.thmanyah.data.remote.dto.SearchResponseDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchResponseParsingTest {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Test
    fun parsesHomePayload() {
        val jsonText = this::class.java.getResource("/search_response.json")!!.readText()
        val adapter = moshi.adapter(SearchResponseDto::class.java)
        val dto = adapter.fromJson(jsonText)!!
        val sections = dto.sections.map { it.toDomain() }
        assertTrue(sections.isNotEmpty())
        assertTrue(sections.any { it.name.contains("v") })
        assertEquals("Handcrafted Steel Gloves", sections[0].items[0].title)
    }
}