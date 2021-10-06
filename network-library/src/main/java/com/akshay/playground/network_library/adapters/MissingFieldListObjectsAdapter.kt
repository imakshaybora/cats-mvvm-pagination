package com.akshay.playground.network_library.adapters

import com.squareup.moshi.*
import java.lang.reflect.Type

class MissingFieldListObjectsAdapter : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        return if (annotations.isEmpty() && Types.getRawType(type) == MutableList::class.java) {
            val elementType = Types.collectionElementType(type, MutableList::class.java)
            val elementAdapter: JsonAdapter<Any> = moshi.adapter<Any>(elementType)
            return SkipBadListObjectsAdapter(elementAdapter)
        } else {
            null
        }
    }

    private class SkipBadListObjectsAdapter(private val elementAdapter: JsonAdapter<Any>) :
        JsonAdapter<MutableList<Any?>>() {

        override fun fromJson(reader: JsonReader): MutableList<Any?>? {
            val result = mutableListOf<Any?>()

            reader.beginArray()

            while (reader.hasNext()) {


                val peeked = reader.peekJson()
                result += elementAdapter.fromJson(peeked)
                reader.skipValue()
            }
            reader.endArray()
            return result

        }

        override fun toJson(writer: JsonWriter, value: MutableList<Any?>?) {
            if (value == null) {
                throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
            }
            writer.beginArray()
            for (i in value.indices) {
                elementAdapter.toJson(writer, value[i])
            }
            writer.endArray()
        }
    }
}