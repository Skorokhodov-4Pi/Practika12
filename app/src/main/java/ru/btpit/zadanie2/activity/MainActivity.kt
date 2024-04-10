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
import ru.btpit.zadanie2.activity.NewPostActivity.Companion.textArg


import ru.btpit.zadanie2.viewmodel.PostViewModel
import ru.btpit.zadanie2.R
import ru.btpit.zadanie2.databinding.FragmentFeedBinding
import ru.btpit.zadanie2.dto.Post


class MainActivity : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = Posts(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val bundle = Bundle()
                bundle.textArg = post.content
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment, bundle)

            }
            override fun onVideo(post: Post)
            {
                val url = "https://www.youtube.com/watch?v=qeQnMgega0k"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onShare(post: Post) {

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)

                    viewModel.share(post.id)

            }

            override fun onAuthorClicked(post: Post) {
                val bundle = Bundle()
                bundle.putLong("postId", post.id)
                findNavController().navigate(R.id.action_feedFragment_to_onePostFragment, bundle)

            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })
    binding.list.adapter = adapter
    viewModel.data.observe(viewLifecycleOwner) { posts ->
        adapter.submitList(posts)
    }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}



