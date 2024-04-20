package project.st991660690.game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import project.st991660690.game.databinding.ActivityBonusBinding

class BonusFragment : Fragment() {

    private var _binding: ActivityBonusBinding? = null

    private val binding get() = _binding!!

    private val fireStoreDatabase = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityBonusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fireStoreDatabase.collection("GameData").get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    result.append("User: ")
                        .append(document.data.getValue("username")).append("\nNo of Tries: ")
                        .append(document.data.getValue("noOfTries")).append("\nSuccessful Tries: ")
                        .append(document.data.getValue("successfulTries")).append("\n\n")
                }
                binding?.txtStats?.setText(result)
            }
        }

        binding.btnEndGame.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val snackBar = Snackbar.make(view, "Game Ended!!", BaseTransientBottomBar.LENGTH_SHORT)
            snackBar.setTextColor(Color.parseColor("#d896ff"))
            snackBar.show()
            findNavController().navigate(R.id.action_bonusFragment_to_welcomeFragment)
        }
    }
}