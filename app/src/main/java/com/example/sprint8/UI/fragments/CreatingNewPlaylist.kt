package com.example.sprint8.UI.fragments

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
import androidx.navigation.fragment.findNavController
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.arrow)
        val description = view.findViewById<TextInputEditText>(R.id.description)
        toolbar.setNavigationOnClickListener {

             if (!namePlayList.text.isNullOrEmpty() || !description.text.isNullOrEmpty() || uRi!=null ){
                 dialogue()
             } else {
                 findNavController().popBackStack()
             }

        }

        namePlayList.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {

                createButton.isEnabled = false
            } else {

                createButton.isEnabled = true
            }
        }

        val picture = view.findViewById<ImageView>(R.id.image)
        picture.setOnClickListener { accessingRepository() }
    }
     var uRi: Uri? = null
     lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->


                // Callback вызовется, когда пользователь выберет картинку
                if (uri != null) {
                    uRi = uri
                    val picture = view?.findViewById<ImageView>(R.id.image)
                    picture?.setImageURI(uri)
                    picture?.scaleType=ImageView.ScaleType.FIT_XY
//                    mediaAdd(uri)
                    Log.d("PhotoPicker", "Выбранный URI: $uri")
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }
    }

    fun accessingRepository() {

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

     fun dialogue() {

         context?.let {
             MaterialAlertDialogBuilder(it)
                 .setTitle("Завершить создание плейлиста?") // Заголовок диалога
                 .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
                 .setNeutralButton("Отмена") { dialog, which -> // Добавляет кнопку «Отмена»
                     // Действия, выполняемые при нажатии на кнопку «Отмена»
                 }
                 .setPositiveButton("Завершить") { dialog, which -> findNavController().popBackStack()// Добавляет кнопку «Да»
                     // Действия, выполняемые при нажатии на кнопку «Да»
                 }
                 .show()
         }
    }



}

