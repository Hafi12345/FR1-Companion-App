package com.fr1.companion.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OllamaApiService {

    @GET("api/tags")
    suspend fun listModels(): Response<OllamaTagsResponse>

    @POST("api/chat")
    suspend fun chat(@Body request: OllamaChatRequest): OllamaChatResponse
}

data class OllamaTagsResponse(val models: List<OllamaModelInfo> = emptyList())
data class OllamaModelInfo(val name: String = "")

data class OllamaMessage(val role: String, val content: String)

data class OllamaChatRequest(
    val model: String,
    val messages: List<OllamaMessage>,
    val stream: Boolean = false,
)

data class OllamaChatResponse(val message: OllamaMessage? = null)
