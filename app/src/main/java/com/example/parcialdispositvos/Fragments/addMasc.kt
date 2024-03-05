
package com.example.parcialdispositvos.Fragments
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.parcialdispositvos.Apis.check_empty
import com.example.parcialdispositvos.Apis.send_message
import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.parcialdispositvos.R

class AddMascFragment : Fragment() {
    lateinit var addMascview : View
    lateinit var storageRef: StorageReference

    val db = Firebase.firestore
    lateinit var edt_nombre : EditText
    lateinit var edt_raza : EditText
    lateinit var edt_edad : EditText
    lateinit var btn_Agregar : Button
    lateinit var btn_Regresar : Button
    lateinit var imageButton: ImageButton

    var actualUser: String? = null
    var selectedImageUri: Uri? = null

    companion object {
        const val REQUEST_IMAGE_PICK = "image/*"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addMascview = inflater.inflate(R.layout.fragment_add_masc, container, false)
        storageRef = FirebaseStorage.getInstance().reference

        edt_nombre = addMascview.findViewById(R.id.edt_nombre)
        edt_raza = addMascview.findViewById(R.id.edt_raza)
        edt_edad = addMascview.findViewById(R.id.edt_edad)
        btn_Agregar = addMascview.findViewById(R.id.btn_det_agregar)
        btn_Regresar = addMascview.findViewById(R.id.btn_Regresar)
        imageButton = addMascview.findViewById(R.id.imageButton)

        imageButton.setOnClickListener { selectImage() }

        return addMascview
    }

    override fun onStart() {
        super.onStart()
        actualUser = AddMascFragmentArgs.fromBundle(requireArguments()).userName

        btn_Regresar.setOnClickListener {
            val directions = AddMascFragmentDirections.actionAddMascToListFragment()
            addMascview.findNavController().navigate(directions)
        }

        btn_Agregar.setOnClickListener {
            var flag = check_empty(this.addMascview, "el nombre", edt_nombre)
            if (!flag)
                flag = check_empty(this.addMascview, "la raza", edt_raza)
            if (!flag)
                flag = check_empty(this.addMascview, "la edad", edt_edad)
            if (!flag) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveMascota()
                } else {
                    Toast.makeText(requireContext(), "Select an image first", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
        // Update UI or show a preview of the selected image if needed
        Toast.makeText(requireContext(), "Image selected: $uri", Toast.LENGTH_SHORT).show()
    }

    private fun selectImage() {
        imagePickerLauncher.launch(REQUEST_IMAGE_PICK)
    }

    private fun uploadImageAndSaveMascota() {
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    val newMascota = Mascota(
                        edt_nombre.text.toString() + actualUser,
                        edt_edad.text.toString().toInt(),
                        edt_nombre.text.toString(),
                        edt_raza.text.toString(),
                        actualUser!!,
                        imageUrl.toString() // Set the image URL
                    )
                    db.collection("prueba_img").document(newMascota.nombre).set(newMascota)
                        .addOnSuccessListener {
                            send_message(this.addMascview, "Mascota agregada satisfactoriamente")
                            val directions = AddMascFragmentDirections.actionAddMascToListFragment()
                            addMascview.findNavController().navigate(directions)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error adding mascota: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
