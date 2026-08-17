package com.example.data.local

import com.example.domain.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonSerializer {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T> toJson(data: T?): String? {
        if (data == null) return null
        return try {
            val adapter = moshi.adapter(T::class.java)
            adapter.toJson(data)
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> fromJson(json: String?): T? {
        if (json.isNullOrBlank()) return null
        return try {
            val adapter = moshi.adapter(T::class.java)
            adapter.fromJson(json)
        } catch (e: Exception) {
            null
        }
    }

    fun referenceFramesFromJson(json: String?): List<ReferenceFrame> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val type = Types.newParameterizedType(List::class.java, ReferenceFrame::class.java)
            val adapter = moshi.adapter<List<ReferenceFrame>>(type)
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun referenceFramesToJson(frames: List<ReferenceFrame>): String {
        return try {
            val type = Types.newParameterizedType(List::class.java, ReferenceFrame::class.java)
            val adapter = moshi.adapter<List<ReferenceFrame>>(type)
            adapter.toJson(frames)
        } catch (e: Exception) {
            "[]"
        }
    }
}
