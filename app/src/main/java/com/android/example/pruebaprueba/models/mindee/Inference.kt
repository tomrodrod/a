package com.android.example.pruebaprueba.models.mindee

data class InferenceResponse(
    val inference: Inference
)
data class Inference(
    val id: String,
    val model: ModelInfor,
    val file: FileInfor,
    val result: Result
)

data class Result(
    val fields: FieldsFactura,
)


data class FieldsFactura(
    val supplierName: FieldValue<String>?,
    val date: FieldValue<String>?,
    val totalAmount: FieldValue<Double>?,
    val lineItems: ItemList<LineItem>?
)

data class ModelInfor(
    val id: String
)

data class FileInfor(
    val name: String,
    val alias: String?,
    val page_count: Int,
    val mime_type: String,
)

data class FieldValue<T>(
    val value: T?
)

data class ItemList<T>(
    val items: List<T>?
)

data class LineItem(
    val fields: Item?
)


data class Item(
//PRODUCT
    val description: FieldValue<String>,
    val quantity: FieldValue<Int>,
    val unit_price: FieldValue<Double>,
    val total_price: FieldValue<Double>,
)