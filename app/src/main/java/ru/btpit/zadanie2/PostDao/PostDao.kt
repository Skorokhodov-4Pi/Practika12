package ru.btpit.zadanie2.PostDao

import ru.btpit.zadanie2.dto.Post


interface PostDao {
    fun getAll(): List<Post>
    fun like(id:Long)
    fun share(id:Long)
    fun save(post: Post): Post
    fun removeById(id: Long)
}
