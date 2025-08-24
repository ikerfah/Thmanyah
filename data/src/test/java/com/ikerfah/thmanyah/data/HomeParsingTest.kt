package com.ikerfah.thmanyah.data

import com.ikerfah.thmanyah.data.mapper.toDomain
import com.ikerfah.thmanyah.data.remote.dto.HomeSectionDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeParsingTest {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Test
    fun parsesHomePayload() {
        val jsonText = this::class.java.getResource("/home_response.json")!!.readText()
        val adapter = moshi.adapter(HomeSectionDto::class.java)
        val dto = adapter.fromJson(jsonText)!!
        val sections = dto.sections.map { it.toDomain() }
        assertTrue(sections.isNotEmpty())
        assertTrue(sections.any { it.name.contains("Top Podcasts") })
    }
}