package project.st991660690.game

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import project.st991660690.game.databinding.FragmentSecondBinding
import kotlin.system.exitProcess

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

const val TAG = "FIRESTORE"

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val fireStoreDatabase = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        if (bundle == null) {
            Log.d("Confirmation", "Fragment 2 didn't receive info")
            return
        }

        val args = SecondFragmentArgs.fromBundle(bundle)

        val username = args.username.toString()

        if (username.isNotEmpty())
            binding.txtTitleUser.text = "Statistics for " + args.username.toString()
        else
            binding.txtTitleUser.text = "Statistics for Default"

        if (!args.noOfTries.toString().isDigitsOnly()) {
            binding.txtNoOfTries.text = "Number of Tries: 0"
        } else {
            binding.txtNoOfTries.text = "Number of Tries: " + args.noOfTries.toString()
        }

        if (!args.successfulTries.toString().isDigitsOnly()) {
            binding.txtSuccessfulTries.text = "Successful Tries: 0"
        } else {
            binding.txtSuccessfulTries.text = "Successful Tries: " + args.successfulTries.toString()
        }

        binding.imgBtnSave.setOnClickListener {
            val username = args.username.toString()
            val noOfTries = args.noOfTries.toString().toInt()
            val successfulTries = args.successfulTries.toString().toInt()
            val user: MutableMap<String, Any> = HashMap()
            user["username"] = username
            user["noOfTries"] = noOfTries
            user["successfulTries"] = successfulTries
            val data = fireStoreDatabase.collection("GameData")
            data.whereEqualTo("username", username).get().addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    fireStoreDatabase.collection("GameData").add(user)
                        .addOnSuccessListener {
                            Log.d(TAG, "Added document with ID ${it.id}")
                            val snackBar = Snackbar.make(view, "Data added to firestore database!!", BaseTransientBottomBar.LENGTH_SHORT)
                            snackBar.setTextColor(Color.parseColor("#32a852"))
                            snackBar.show()
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error adding document $exception")
                            val snackBar = Snackbar.make(view, "Error adding Data to firestore database!!",BaseTransientBottomBar.LENGTH_SHORT)
                            snackBar.setTextColor(Color.parseColor("#de1d23"))
                            snackBar.show()
                        }
                } else {
                    for (document in documents) {
                        data.document(document.id).update(
                            mapOf(
                                "noOfTries" to noOfTries,
                                "successfulTries" to successfulTries
                            )
                        ).addOnSuccessListener {
                            Log.d(TAG, "Data updated successfully!")
                            val snackBar = Snackbar.make(view, "Data updated in firestore database!!", BaseTransientBottomBar.LENGTH_SHORT)
                            snackBar.setTextColor(Color.parseColor("#32a852"))
                            snackBar.show()
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error updating document $exception")
                            val snackBar = Snackbar.make(view, "Error updating Data to firestore database!!",BaseTransientBottomBar.LENGTH_SHORT)
                            snackBar.setTextColor(Color.parseColor("#de1d23"))
                            snackBar.show()
                        }
                    }
                }
            }
        }

        binding.btnStats.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_bonusFragment)
        }

        binding.btnClose.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val snackBar = Snackbar.make(view, "Game Ended!!",BaseTransientBottomBar.LENGTH_SHORT)
            snackBar.setTextColor(Color.parseColor("#d896ff"))
            snackBar.show()
            findNavController().navigate(R.id.action_SecondFragment_to_WelcomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}