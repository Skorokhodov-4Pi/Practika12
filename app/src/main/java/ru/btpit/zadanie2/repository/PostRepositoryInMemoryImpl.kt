package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.btpit.zadanie2.dto.Post


class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var post = listOf(
        Post(id = nextId++,
        author = "Борисоглебский техникум промышленных и информационных техологий",
        content = "ГБПОУ ВО «БТПИТ» образовано в соответствии с постановлением правительства Воронежской области от 20 мая 2015 года № 401 в результате реорганизации в форме слияния ГОБУ СПО ВО «БИТ», ГОБУ СПО ВО «БТИВТ» и ГОБУ НПО ВО «ПУ № 34 г. Борисоглебска»\\nОбразовательно-производственный центр (кластер) федерального проекта\\n\\\"Профессионалитет\\\" по отраслям «Туризм и сфера услуг» на базе ГБПОУ ВО \\\"ХШН\\\" и «Педагогика» на базе ГБПОУ ВО \\\"ГПК\\\" .\\nКолледжи-партнеры: Базовая ОО - ГБПОУ ВО \\\"ХШН\\\"; сетевые ОО - ГБПОУ ВО \\\"БАИК\\\", ГБПОУ ВО \\\"ВГПГК\\\", ГБПОУ ВО \\\"ВТППП\\\", ГБПОУ ВО \\\"ВГПТК\\\", ГБПОУ ВО \\\"БТПИТ\\\".\\nКолледжи-партнеры: Базовая ОО - ГБПОУ ВО \\\"ГПК\\\"; сетевые ОО - ГБПОУ ВО \\\"ВГПГК имени В.М. Пескова“, ГБПОУ ВО \\\"БТПИТ\\\".\\nПодробнее о федеральном проекте «Профессионалитет» на сайте\"",
        published = "21 мая в 18:36",
        likecount = 999,
        share = 5,
        isLiked = false
        ),
        Post(
            id = nextId++,
            author = "Борисоглебский техникум промышленных и информационных техологий",
            content = "В Борисоглебском техникуме промышленных и информационных технологий содержательно прошёл Всероссийский урок памяти «Возвращение в родную гавань». Урок наглядно показал, что история полуострова тесно связана с историей России." ,
            published = "30 сентября в 22:12",
            isLiked = false,
            likecount = 999,
            share = 5
        ),
        Post(
            id = nextId++,
            author = "Борисоглебский техникум промышленных и информационных техологий",
            content = "Идет регистрация на новый сезон проекта «Флагманы образования»" ,
            published = "8 апреля в 15:06",
            isLiked = false,
            likecount = 999,
            share = 5
        ),
    )
    private val data = MutableLiveData(post)
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
            return
        }
        post = post.map {
            if (it.id != posts.id) it else it.copy(content = posts.content)
        }
        data.value = post
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
    }
    override fun removeById(id: Long)
    {
        post = post.filter { it.id != id }
        data.value =post
    }
}