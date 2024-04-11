package com.lesa.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.Screens
import com.lesa.app.databinding.FragmentAnotherProfileBinding
import com.lesa.app.stubPeople
import com.squareup.picasso.Picasso

class AnotherProfileFragment : Fragment() {
    private val binding: FragmentAnotherProfileBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserView(requireArguments().getInt(USER_ID_KEY))
        binding.backButton.setOnClickListener {
            INSTANCE.router.backTo(Screens.People())
        }
    }

    private fun setUpUserView(id: Int) {
        val person = stubPeople.first {
            it.id == id
        }
        binding.include.apply {
            userNameTextView.text = person.name
            userChatStatusTextView.text = person.chatStatus
            userNetStatusTextView.text = resources.getString(person.netStatus.text)
            userNetStatusTextView.setTextColor(resources.getColor(person.netStatus.color))
            Picasso.get().load(person.avatar).into(userLogoImage)
        }
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"

        fun getNewInstance(userId: Int): AnotherProfileFragment {
            return AnotherProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID_KEY, userId)
                }
            }
        }
    }
}