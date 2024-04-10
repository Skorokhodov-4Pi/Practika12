package ru.btpit.zadanie2.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.btpit.zadanie2.util.StringArg
import ru.btpit.zadanie2.viewmodel.PostViewModel
import ru.btpit.zadanie2.R
import ru.btpit.zadanie2.databinding.ActivityOnePostFragmentBinding

class OnePostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityOnePostFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val postId = arguments?.getLong("postId")
        postId?.let { id ->
            viewModel.getPostById(id)
            viewModel.selectedPost.observe(viewLifecycleOwner) { post ->
                binding.textView.text = post.author
                binding.textView2.text = post.published
                binding.textView5.text = post.content
                binding.like.text = post.likecount.toString()
                binding.share.text = post.share.toString()
                binding.like.isChecked = post.isLiked
                binding.like.text = formatNumber(post.likecount)
                binding.share.text = formatNumber1(post.share)
                binding.Video.setOnClickListener {
                    val url = "https://www.youtube.com/watch?v=qeQnMgega0k"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
                binding.back.setOnClickListener {
                    findNavController().navigateUp()
                }
                binding.editorPost.setOnClickListener {
                    viewModel.edit(post)
                    val bundle = Bundle()
                    bundle.textArg = post.content
                    findNavController().navigate(R.id.action_onePostFragment_to_newPostFragment3, bundle)
                }
                binding.like.setOnClickListener {
                    viewModel.like(postId)

                }
                binding.share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)

                    viewModel.share(postId)
                }

                viewModel.data.observe(viewLifecycleOwner) { posts ->
                    val updatedPost = posts.find { it.id == postId }
                    updatedPost?.let {
                        binding.like.text = it.likecount.toString()
                        binding.share.text = it.share.toString()
                    }
                }
            }
                binding.del.setOnClickListener{
                    viewModel.removeById(postId)
                    findNavController().navigateUp()
                }
            }
        return binding.root
        }
    }
private fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> {
            val value = number / 1000000
            val remainder = number % 1000000
            if (remainder > 0) {
                if (remainder >= 100000) {
                    String.format("%.1f M", value + remainder / 1000000.0)
                } else {
                    String.format("%d.%d M", value, remainder / 100000)
                }
            } else {
                "$value M"
            }
        }
        number in 1000..9999 -> {
            String.format("%.1fK", number / 1000.0)
        }
        number >= 10000 -> {
            String.format("%dK", number / 1000)
        }
        else -> number.toString()
    }
}
private fun formatNumber1(number: Int): String {
    return when {
        number >= 1000000 -> {
            val value = number / 1000000
            val remainder = number % 1000000
            if (remainder > 0) {
                if (remainder >= 100000) {
                    String.format("%.1f M", value + remainder / 1000000.0)
                } else {
                    String.format("%d.%d M", value, remainder / 100000)
                }
            } else {
                "$value M"
            }
        }
        number in 1000..9999 -> {
            String.format("%.1fK", number / 1000.0)
        }
        number >= 10000 -> {
            String.format("%dK", number / 1000)
        }
        else -> number.toString()
    }
}


