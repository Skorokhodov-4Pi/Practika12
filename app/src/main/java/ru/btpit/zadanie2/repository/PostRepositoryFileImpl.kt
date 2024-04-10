package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.btpit.zadanie2.dto.Post


class PostRepositoryFileImpl(
    private val context: Context,
) : PostRepository{
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 0L
    private var post = emptyList<Post>()
    private val data = MutableLiveData(post)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {

            context.openFileInput(filename).bufferedReader().use {
                post = gson.fromJson(it, type)
                data.value = post
            }
        } else {

            sync()
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

    override fun save(post: Post) {
        var sum = 1L
        for (p in this.post){
            sum += p.id
        }

        if (post.id == nextId && post.id != sum) {
            this.post = listOf(
                post.copy(
                    id = sum,
                    author = "Me",
                    isLiked = false,
                    published = "now"
                )
            ) + this.post
            data.value = this.post
            sync()
            return
        }

        this.post = this.post.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = this.post
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
    override fun postID(id: Long): LiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        postLiveData.value = post.find { it.id == id }

        return postLiveData
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(post))
        }
     }
}
