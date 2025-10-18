package com.android.example.pruebaprueba.models.mindee

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class InferenceResponse(
    val inference: Inference
)

@JsonClass(generateAdapter = true)
data class Inference(
    val id: String,
    val model: ModelInfor,
    val file: FileInfor,
    val result: Result
)
@JsonClass(generateAdapter = true)
data class Result(
    val fields: FieldsFactura,
)


@JsonClass(generateAdapter = true)
data class FieldsFactura(
    @Json(name="supplier_name") val supplierName: FieldValue<String>?,
    @Json(name="date") val date: FieldValue<String>?,
    @Json(name="total_amount") val totalAmount: FieldValue<Double>?,
    @Json(name="line_items") val lineItems: ItemList<LineItem>?
)

@JsonClass(generateAdapter = true)
data class ModelInfor(
    val id :String
)

@JsonClass(generateAdapter = true)
data class FileInfor(
    val name: String,
    val alias: String?,
    val page_count: Int,
    val mime_type: String,
)



@JsonClass(generateAdapter = true)
data class FieldValue<T>(
    val value: T?
)

@JsonClass(generateAdapter = true)
data class ItemList<T>(
    val items: List<T>?
)

@JsonClass(generateAdapter = true)
data class LineItem(
    val fields: Item?
)


@JsonClass(generateAdapter = true)
data class Item(//PRODUCT
    val description: FieldValue<String>,
    val quantity: FieldValue<Int>,
    val unit_price: FieldValue<Double>,
    val total_price: FieldValue<Double>,
)