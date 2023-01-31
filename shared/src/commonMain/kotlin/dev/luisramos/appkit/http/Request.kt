package dev.luisramos.appkit.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NetworkError(
    val statusCode: Int,
    val statusDescription: String,
    val requestUrl: String,
    val responseBody: String
) : Throwable(
    "Error $statusCode $statusDescription for request with url $requestUrl" +
            " - Error response body: $responseBody"
)

suspend inline fun <reified T> request(
    client: HttpClient,
    requestBuilder: HttpRequestBuilder,
    crossinline onSuccessResponseTransform: suspend (HttpResponse) -> Result<T>
): Result<T> =
    try {
        val statement = client.prepareRequest(requestBuilder)
        statement.execute { response: HttpResponse ->
            when {
                response.status.isSuccess() ->
                    onSuccessResponseTransform(response)

                else -> {
                    val error = NetworkError(
                        statusCode = response.status.value,
                        statusDescription = response.status.description,
                        requestUrl = requestBuilder.url.encodedPath,
                        responseBody = response.body()
                    )
                    Result.failure(error)
                }
            }
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }

suspend inline fun <reified T> request(
    client: HttpClient,
    context: CoroutineContext,
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit
): Result<T> {
    val builder = HttpRequestBuilder()
        .apply { url(url) }
        .apply(block)
    return withContext(context) { request(client, builder) { Result.success(it.body()) } }
}

suspend inline fun <reified T> httpGet(
    client: HttpClient,
    context: CoroutineContext,
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit
): Result<T> =
    request(client, context, url) {
        method = HttpMethod.Get
        block()
    }

suspend inline fun <reified T> httpPost(
    client: HttpClient,
    context: CoroutineContext,
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit
): Result<T> =
    request(client, context, url) {
        method = HttpMethod.Post
        block()
    }

suspend inline fun <reified T> httpPut(
    client: HttpClient,
    context: CoroutineContext,
    path: String,
    crossinline block: HttpRequestBuilder.() -> Unit
): Result<T> = request(client, context, path) {
    method = HttpMethod.Put
    block()
}