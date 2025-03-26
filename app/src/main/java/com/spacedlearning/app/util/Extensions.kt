package com.spacedlearning.app.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spacedlearning.app.data.remote.dto.common.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// LiveData Extensions
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner) { value ->
        value?.let {
            observer(it)
            if (this is MutableLiveData) {
                this.value = null
            }
        }
    }
}

// Flow Extensions
fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this
        .map { Resource.success(it) }
        .onStart { emit(Resource.loading()) }
        .catch { e -> emit(handleException(e)) }
}

// Exception Handling
fun handleException(e: Throwable): Resource<Nothing> {
    return when (e) {
        is HttpException -> {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson<ErrorResponse>(errorBody, object : TypeToken<ErrorResponse>() {}.type)
            } catch (e: Exception) {
                null
            }

            Resource.error(
                message = errorResponse?.message ?: e.message ?: "Unknown error occurred",
                code = e.code()
            )
        }
        else -> Resource.error(e.message ?: "Unknown error occurred")
    }
}

// Date Extensions
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.formatToString(pattern: String = Constants.DEFAULT_DATE_FORMAT): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToString(pattern: String = Constants.DEFAULT_DATETIME_FORMAT): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(pattern: String = Constants.DEFAULT_DATE_FORMAT): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
    } catch (e: Exception) {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(pattern: String = Constants.DEFAULT_DATETIME_FORMAT): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
    } catch (e: Exception) {
        null
    }
}

fun Date.formatToString(pattern: String = Constants.DEFAULT_DATE_FORMAT): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

fun String.toDate(pattern: String = Constants.DEFAULT_DATE_FORMAT): Date? {
    return try {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.parse(this)
    } catch (e: Exception) {
        null
    }
}