package com.tyris.data.networking

import com.tyris.domain.util.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException
import com.tyris.domain.util.Result as SafeResult

suspend inline fun <reified Response: Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): SafeResult<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.post(
    route: String,
    body: Request
): SafeResult<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response: Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): SafeResult<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): SafeResult<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return SafeResult.Error(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return SafeResult.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if(e is CancellationException) throw e
        e.printStackTrace()
        return SafeResult.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): SafeResult<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> SafeResult.Success(response.body<T>())
        401 -> SafeResult.Error(DataError.Network.UNAUTHORIZED)
        408 -> SafeResult.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> SafeResult.Error(DataError.Network.CONFLICT)
        413 -> SafeResult.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> SafeResult.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> SafeResult.Error(DataError.Network.SERVER_ERROR)
        else -> SafeResult.Error(DataError.Network.UNKNOWN)
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains("https://rickandmortyapi.com") -> route
        route.startsWith("/") -> "https://rickandmortyapi.com$route"
        else -> "https://rickandmortyapi.com/$route"
    }
}