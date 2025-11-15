package com.android.example.pruebaprueba.core.platform.service

import com.android.example.pruebaprueba.core.functional.Either
import com.android.example.pruebaprueba.core.exception.Failure
import retrofit2.Response
import javax.inject.Inject

class Request @Inject constructor(private val networkHandler: NetworkHandler) {

    fun <T> launch(call: Response<T>): Either<Failure, T> =
        if (networkHandler.hasInternetConnection()) {
            call.runCatching {
                if (this.isSuccessful && this.body() != null) {
                    Either.Right(this.body()!!)
                } else {
                    Either.Left(Failure.ServerError(this.code(), this.message()))
                }
            }.getOrElse {
                Either.Left(error = Failure.Throwable(it))
            }
        } else {
            Either.Left(Failure.NetworkConnection)
        }
}
