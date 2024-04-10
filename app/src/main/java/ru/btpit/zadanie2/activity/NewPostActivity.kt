package ru.btpit.zadanie2.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController


import ru.btpit.zadanie2.util.StringArg
import ru.btpit.zadanie2.viewmodel.PostViewModel
import ru.btpit.zadanie2.databinding.FragmentNewPostBinding
import ru.btpit.zadanie2.util.AndroidUtils

class NewPostActivity : Fragment() {

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
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        if (arguments?.textArg == null )
        {
            binding.Name.text ="Добавить пост"
        }
     else
        {
            binding.Name.text ="Изменить пост"
        }

        arguments?.textArg
            ?.let(binding.edit::setText)

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}