package com.android.example.pruebaprueba.data.models

import com.android.example.pruebaprueba.data.service.models.JobResponseDTO

data class JobResponseEntity(
    val job: JobEntity?
)

fun JobResponseDTO.toData() = JobResponseEntity(
    job = this.job?.toData()
)
