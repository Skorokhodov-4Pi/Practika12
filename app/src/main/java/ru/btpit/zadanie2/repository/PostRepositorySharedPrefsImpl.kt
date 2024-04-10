package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.btpit.zadanie2.dto.Post


class PostRepositorySharedPrefsImpl(
    context:Context,
) : PostRepository{
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var post = emptyList<Post>()
    private val data = MutableLiveData(post)

    init {
        prefs.getString(key, null)?.let {
            post = gson.fromJson(it, type)
            data.value = post
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun like(id:Long) {

        post = post.map {
            if (it.id == id && it.isLiked) {
                it.copy(likecount = it.likecount - 1, isLiked = false)
            } else if (it.id == id && !it.isLiked) {
                it.copy(likecount = it.likecount + 1, isLiked = true)
            } else {
                it
            }
        }

        data.value = post
        sync()
    }

    override fun postID(id: Long): LiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        postLiveData.value = post.find { it.id == id }
        return postLiveData
    }
    override fun save(posts: Post) {
        if (posts.id == 0L) {
            // TODO: remove hardcoded author & published
            post = listOf(
                posts.copy(
                    id = nextId++,
                    author = "Me",
                    isLiked = false,
                    published = "now"
                )
            ) + post
            data.value = post
            sync()
            return
        }
        post = post.map {
            if (it.id != posts.id) it else it.copy(content = posts.content)
        }
        data.value = post
        sync()
    }

    override fun share(id: Long) {
        post = post.map {
            if (it.id != id) {
                it
            } else {
                it.copy(share = it.share + 1)
            }
        }
        data.value = post
        sync()
    }
    override fun removeById(id: Long)
    {
        post = post.filter { it.id != id }
        data.value =post
        sync()
    }
    private fun sync(){
        with(prefs.edit()){
            putString(key, gson.toJson(post))
            apply()
        }
    }
}