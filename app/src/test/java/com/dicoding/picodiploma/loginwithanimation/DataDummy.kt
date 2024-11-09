package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val stories = mutableListOf<ListStoryItem>()
        for (i in 0 until 10) {
            val story = ListStoryItem(
                id = "id_$i",
                name = "Story $i",
                description = "Description of story $i",
                photoUrl = "https://example.com/photo_$i.jpg",
                createdAt = "2023-01-01T00:00:00Z"
            )
            stories.add(story)
        }
        return stories
    }
}