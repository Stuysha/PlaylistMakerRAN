package com.example.sprint8.UI.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class CreatingNewPlaylist : Fragment() {

    companion object {

        fun newInstance() = CreatingNewPlaylist()
    }

    private val viewModel: PlaylistsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.creating_new_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val namePlayList = view.findViewById<TextInputEditText>(R.id.namePlayList)
        val createButton = view.findViewById<Button>(R.id.button_apd)

        namePlayList.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {

                createButton.isEnabled = false
            } else {

                createButton.isEnabled = true
            }
        }

        val picture = view.findViewById<ImageView>(R.id.picture)
        picture.setOnClickListener { accessingRepository() }
    }
lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->


                // Callback вызовется, когда пользователь выберет картинку
                if (uri != null) {
                    val picture = view?.findViewById<ImageView>(R.id.picture)
                    picture?.setImageURI(uri)
//                    mediaAdd(uri)
                    Log.d("PhotoPicker", "Выбранный URI: $uri")
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }
    }

    fun accessingRepository() {


//        val createDocumentResult =
//            registerForActivityResult(ActivityResultContracts.OpenDocument()) {
//                // тут мы обрабатываем результат запроса
//                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
//            }
//        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        context.contentResolver.takePersistableUriPermission(uri, flag)
        // создаём событие с результатом и передаём в него PickVisualMedia()

// Вызываем метод launch и передаём параметр, чтобы предлагались только картинки
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))    }

    fun mediaAdd (uri: Uri)
    {
        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
  context?.contentResolver?.takePersistableUriPermission(uri, flag)
    }
//    private fun createFile() {
//                val createDocumentResult =
//            registerForActivityResult(ActivityResultContracts.OpenDocument()) {
//                // тут мы обрабатываем результат запроса
//                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
//            }
//        // запускаем Activity, которая вернёт нам результат
//        createDocumentResult.launch("invoice.pdf")
//    }

}

