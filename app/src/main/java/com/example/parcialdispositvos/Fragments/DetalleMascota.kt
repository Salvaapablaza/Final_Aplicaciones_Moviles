
package com.example.parcialdispositvos.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.parcialdispositvos.Apis.send_message
import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.example.parcialdispositvos.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class DetalleMascota : Fragment() {

    private lateinit var uploadedImageUrl: String

    private lateinit var MascotaDetalleView: View
    private lateinit var img_raza_actual: ImageView
    private lateinit var edt_nombre: EditText
    private lateinit var edt_raza: EditText
    private lateinit var edt_edad: EditText
    private lateinit var btn_Modificar: Button
    private lateinit var btn_Regresar: Button
    lateinit var imageButton: ImageButton
    private lateinit var btn_Eliminar: Button

    private var mascotactual = Mascota()
    private var identifier: String? = null
    var selectedImageUri: Uri? = null
    lateinit var storageRef: StorageReference
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MascotaDetalleView = inflater.inflate(R.layout.fragment_detalle_mascota, container, false)

        edt_nombre = MascotaDetalleView.findViewById(R.id.edt_nombredet)
        edt_raza = MascotaDetalleView.findViewById(R.id.edt_razadet)
        edt_edad = MascotaDetalleView.findViewById(R.id.edt_edaddet)
        btn_Modificar = MascotaDetalleView.findViewById(R.id.btn_det_Modificar)
        btn_Regresar = MascotaDetalleView.findViewById(R.id.btn_cat_Regresar)
        img_raza_actual = MascotaDetalleView.findViewById(R.id.img_CatActual)
        btn_Eliminar = MascotaDetalleView.findViewById(R.id.btn_Eliminar)
        imageButton = MascotaDetalleView.findViewById(R.id.imageButton2)
        storageRef = FirebaseStorage.getInstance().reference

        btn_Regresar.visibility = View.INVISIBLE
        btn_Eliminar.visibility = View.INVISIBLE
        imageButton.visibility = View.INVISIBLE

        return MascotaDetalleView
    }

    override fun onStart() {
        super.onStart()
        identifier = DetalleMascotaArgs.fromBundle(requireArguments()).identifier

        val docRef = db.collection("prueba_img").document(identifier!!)
        docRef.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot != null) {
                    mascotactual = dataSnapshot.toObject(Mascota::class.java)!!
                    edt_nombre.setText(mascotactual.nombre)
                    edt_nombre.isEnabled = false
                    edt_raza.setText(mascotactual.raza)
                    edt_raza.isEnabled = false
                    edt_edad.setText(mascotactual.edad.toString())
                    edt_edad.isEnabled = false
                    btn_Modificar.visibility = View.VISIBLE

                    loadMascotaImage(mascotactual.imageUrl)
                } else {
                    Log.d("Test", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Test", "get failed with ", exception)
            }
        btn_Modificar.setOnClickListener {
            enableEditingFields()
            btn_Regresar.visibility = View.VISIBLE
            btn_Eliminar.visibility = View.VISIBLE
            imageButton.visibility = View.VISIBLE
        }
        btn_Eliminar.setOnClickListener {
            deleteMascota()
        }

        imageButton.setOnClickListener {
            modifyMascotaImage()
        }
        btn_Regresar.setOnClickListener {
            storeModifiedValues()
        }
    }

    private fun enableEditingFields() {
        identifier = DetalleMascotaArgs.fromBundle(requireArguments()).identifier

        // Habilito la modificacion de los campos
        edt_raza.isEnabled = true
        edt_edad.isEnabled = true
        edt_nombre.isEnabled = true

        // Muestro el boton de modificar
        btn_Modificar.visibility = View.VISIBLE
    }

    private fun storeModifiedValues() {
        // Hago update de la mascota con los datos nuevos

        val edad = edt_edad.text.toString().toIntOrNull() ?: 0  // Si la conversion falla el valor es 0
        val updates = hashMapOf<String, Any>(
            "nombre" to edt_nombre.text.toString(),
            "raza" to edt_raza.text.toString(),
            "edad" to edad,
       )


        db.collection("prueba_img").document(mascotactual.nombre)
            .update(updates)
            .addOnSuccessListener {
                // Update successful
                send_message(this.MascotaDetalleView, "Mascota modificada")
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Log.e(TAG, "Error updating document", e)
                Toast.makeText(requireContext(), "Failed to update Mascota: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadMascotaImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            // Cargo la imageview con Glide
            Glide.with(this)
                .load(imageUrl)
                .into(img_raza_actual)
        }
    }

    companion object {
        private const val TAG = "DetalleMascotaFragment"
    }

    private fun deleteMascota() {
        db.collection("prueba_img").document(mascotactual.nombre)
            .delete()
            .addOnSuccessListener {
                send_message(this.MascotaDetalleView, "Mascota eliminada")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error deleting document", e)
                Toast.makeText(requireContext(), "Failed to delete Mascota: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun modifyMascotaImage() {

        selectImage()


    }
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
        // Update UI or show a preview of the selected image if needed
        Toast.makeText(requireContext(), "Image selected: $uri", Toast.LENGTH_SHORT).show()
        if (selectedImageUri != null) {
            uploadImageAndSaveMascota() // Llamo la funcion de store solo si selecciono una foto
        } else {
            // Si no selecciono foto, mantengo la actual
            uploadedImageUrl = mascotactual.imageUrl.toString()

        }
        uploadedImageUrl = if (selectedImageUri != null) uri.toString() else mascotactual.imageUrl.toString()
        val updates = hashMapOf<String, Any>(
            "imageUrl" to uploadedImageUrl
        )
        db.collection("prueba_img").document(mascotactual.nombre)
            .update(updates)
            .addOnSuccessListener {
                // Update successful
                send_message(this.MascotaDetalleView, "Mascota modificada")
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Log.e(TAG, "Error updating document", e)
                Toast.makeText(requireContext(), "Failed to update Mascota: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun selectImage() {
        imagePickerLauncher.launch(AddMascFragment.REQUEST_IMAGE_PICK)
    }

    private fun uploadImageAndSaveMascota() {
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    uploadedImageUrl = imageUrl.toString() // Store the image URL
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
