package ru.btpit.zadanie2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.btpit.zadanie2.repository.PostRepositorySQLiteImpl
import ru.btpit.zadanie2.db.AppDb
import ru.btpit.zadanie2.dto.Post
import ru.netology.nmedia.repository.PostRepository

private  val  empty = Post(
    id = 0,
    content = "",
    author = "",
    isLiked = false,
    published = "",
    likecount = 0,
    share = 0


)
class PostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    val selectedPost = MutableLiveData<Post>()
    fun save()
    {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun edit(post: Post)
    {
        edited.value = post
    }
    fun  changeContent(content: String)
    {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text){
                return
            }
            edited.value = it.copy(content = text)
        }
    }
    fun like(id: Long) = repository.like(id)
    fun share(id: Long) = repository.share(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun getPostById(id: Long){
        repository.postID(id).observeForever{
            selectedPost.value = it
        }
    }
}