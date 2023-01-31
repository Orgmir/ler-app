package app.luisramos.ler.app.clients

import dev.luisramos.appkit.http.httpGet
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface LerClient {
    suspend fun fetchFeeds(url: String): Result<List<Pair<String, String>>>
}

class LerClientLive(
    private val client: HttpClient,
    private val context: CoroutineContext = Dispatchers.Default
) : LerClient {
    override suspend fun fetchFeeds(url: String): Result<List<Pair<String, String>>> {
        val urlString = if (url.startsWith("http")) url else "http://$url"
        val body =
            httpGet<String>(client, context, urlString) {}.getOrElse { return Result.failure(it) }
        return Result.failure(RuntimeException("Fix this..."))
    }

//    private fun parseHtml(inputStream: InputStream, baseUrl: String): List<Pair<String, String>> =
//        HtmlHeadParser.parse(inputStream, baseUrl)
//            .map {
//                val (title, link) = it
//                title to link
//            }.fold(mutableMapOf<String, Pair<String, String>>()) { acc, value ->
//                val (_, link) = value
//                acc[link] = value
//                acc
//            }.values.toList()

//    withContext(coroutineContext) {
//        Timber.d("Loading page $urlString")
//
//        val url = createURL(urlString)
//            ?: return@withContext Result.failure(IOException("Could not create URL for $urlString"))
//
//        try {
//            api.download(url).use { response ->
//                val body = response.body
//                    ?: return@withContext Result.failure(IOException("Empty feeds"))
//                val feeds = parseHtml(body.byteStream(), url.baseUrl)
//                Result.success(feeds)
//            }
//        } catch (exception: IOException) {
//            Timber.e(exception)
//            Result.failure(exception)
//        }
//    }

}