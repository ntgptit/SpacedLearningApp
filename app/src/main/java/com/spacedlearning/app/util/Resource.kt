package com.spacedlearning.app.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun error(message: String, code: Int? = null): Resource<Nothing> = Error(message, code)
        fun loading(): Resource<Nothing> = Loading
    }

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = if (this is Success) data else null

    fun errorMessage(): String? = if (this is Error) message else null

    fun errorCode(): Int? = if (this is Error) code else null

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (String, Int?) -> Unit): Resource<T> {
        if (this is Error) action(message, code)
        return this
    }

    inline fun onLoading(action: () -> Unit): Resource<T> {
        if (this is Loading) action()
        return this
    }

    inline fun <R> map(transform: (T) -> R): Resource<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(message, code)
            is Loading -> Loading
        }
    }

    suspend inline fun <R> suspendMap(crossinline transform: suspend (T) -> R): Resource<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(message, code)
            is Loading -> Loading
        }
    }
}