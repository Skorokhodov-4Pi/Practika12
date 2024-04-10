package ru.btpit.zadanie2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.btpit.zadanie2.PostDao.PostDao
import ru.btpit.zadanie2.dto.Post

import ru.netology.nmedia.repository.PostRepository

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun like(id: Long)
        {
            dao.like(id)
            posts = posts.map {
                if (it.id != id) it else it.copy(
                    isLiked = !it.isLiked,
                    likecount = if (it.isLiked) it.likecount - 1 else it.likecount + 1
                )
            }
            data.value = posts
        }

    override fun share(id: Long) {
        dao.like(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                share =  it.share + 1
            )
        }
        data.value = posts
    }

    override fun postID(id: Long): LiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        postLiveData.value = posts.find { it.id == id }

        return postLiveData
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}

