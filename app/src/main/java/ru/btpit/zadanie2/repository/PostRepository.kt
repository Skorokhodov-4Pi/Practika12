package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.btpit.zadanie2.dto.Post


interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id:Long)
    fun share(id:Long)
    fun save(post: Post)
    fun postID(id: Long): LiveData<Post>
    fun removeById(id: Long)
}