package uz.rakhmonov.passportapp.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import uz.rakhmonov.passportapp.BuildConfig
import uz.rakhmonov.passportapp.MyData.Datam.mienUserim
import uz.rakhmonov.passportapp.myDB.MyUser
import uz.rakhmonov.passportapp.databinding.FragmentAddUserBinding
import uz.rakhmonov.passportapp.myDB.AppDataBase
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class AddUserFragment : Fragment() {
    private val binding: FragmentAddUserBinding by lazy {
        FragmentAddUserBinding.inflate(
            layoutInflater
        )
    }
    lateinit var appDataBase: AppDataBase
    lateinit var photoUri: Uri
    lateinit var currentImagePath: String
    lateinit var userList: ArrayList<MyUser>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        appDataBase = AppDataBase.getInstance(binding.root.context)

        userList = appDataBase.myDao().getAllUsers() as ArrayList<MyUser>

        if (mienUserim != null) {
            binding.image.setImageURI(Uri.parse(mienUserim?.image))
            binding.name.setText(mienUserim?.name)
            binding.surname.setText(mienUserim?.surname)
            mienUserim?.number_passport

        }


        binding.image.setOnClickListener {
            val imageFile = createFile()
            photoUri = FileProvider.getUriForFile(
                binding.root.context,
                BuildConfig.APPLICATION_ID,
                imageFile!!
            )
            getImageContent.launch(photoUri)

        }

        binding.saveBtn.setOnClickListener {

            if (mienUserim == null) {
                if (this::photoUri.isInitialized) {


                    if (binding.name.text.isNotEmpty() && binding.surname.text.isNotEmpty()) {

                        val user = MyUser(
                            binding.name.text.toString(),
                            binding.surname.text.toString(),
                            photoUri.toString(),
                            checking()
                        )
                        appDataBase.myDao().addUser(user)
                        findNavController().popBackStack()
                        Toast.makeText(context, "saqlandi", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            context,
                            "Iltimos barcha qatorlarni to'ldiring",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Iltimos rasm tanlang", Toast.LENGTH_SHORT).show()
                }

            }

            if (mienUserim != null) {


                val user = MyUser()
                user.id = mienUserim?.id
                user.name = binding.name.text.toString()
                user.surname = binding.surname.text.toString()
                user.number_passport = mienUserim?.number_passport
                user.image = mienUserim?.image

                if (this::photoUri.isInitialized) {
                    user.image = photoUri.toString()
                }


                appDataBase.myDao().editUser(user)
                Toast.makeText(context, "O'zgartirildi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                mienUserim = null
            }


        }

        return binding.root
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.image.setImageURI(photoUri)
                val inputStream = requireActivity().contentResolver?.openInputStream(photoUri)
                val file = File(requireActivity().filesDir, "image.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                val absolutePath = file.absolutePath
            }
        }

    private fun createFile(): File? {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        println("createImageFile ishlayapti")
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    fun PassportNumber(): String {
        var L_1 = ('A'..'Z').toMutableList()
//        var L_1= charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        var n_1 = Random.nextInt(9)
        var n_2 = Random.nextInt(9)
        var n_3 = Random.nextInt(9)
        var n_4 = Random.nextInt(9)
        var n_5 = Random.nextInt(9)
        var n_6 = Random.nextInt(9)
        var n_7 = Random.nextInt(9)

        var c = L_1[Random.nextInt(26)]
        var d = L_1[Random.nextInt(26)]

        var pas_num =
            "$c$d " + " ${n_1}" + "${n_2}" + "${n_3}" + "${n_4}" + "${n_5}" + "${n_6}" + "${n_7}"

        return pas_num
    }

    fun checking(): String {
        var temp = PassportNumber()
        userList.forEach {
            if (it.number_passport == temp)
                checking()
        }

        return temp
    }
}