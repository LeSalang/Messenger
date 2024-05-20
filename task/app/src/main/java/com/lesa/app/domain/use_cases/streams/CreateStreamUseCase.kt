package com.lesa.app.domain.use_cases.streams

import com.lesa.app.data.repositories.StreamsRepository
import com.lesa.app.domain.model.Stream
import javax.inject.Inject

class CreateStreamUseCase @Inject constructor(
    private val streamsRepository: StreamsRepository
) {
    suspend fun invoke(streamName: String) : List<Stream> {
        return streamsRepository.createStream(streamName = streamName)
    }
}