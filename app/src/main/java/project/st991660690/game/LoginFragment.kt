package project.st991660690.game


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import org.intellij.lang.annotations.Identifier
import project.st991660690.game.databinding.ActivityLoginBinding

class LoginFragment : Fragment() {

    private var _binding: ActivityLoginBinding? = null

    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.logButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val pass = binding.passwordField.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snackBar = Snackbar.make(view, "E-mail registered !!!", BaseTransientBottomBar.LENGTH_SHORT)
                        snackBar.setTextColor(Color.parseColor("#32a852"))
                        snackBar.show()
                        val directions = LoginFragmentDirections.actionLoginFragmentToFirstFragment(email)
                        findNavController().navigate(directions)
                    }
                    else if(task.exception is FirebaseAuthUserCollisionException){
                        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                val snackBar = Snackbar.make(view, "Login was successfull!", BaseTransientBottomBar.LENGTH_SHORT)
                                snackBar.setTextColor(Color.parseColor("#32a852"))
                                snackBar.show()
                                val directions = LoginFragmentDirections.actionLoginFragmentToFirstFragment(email)
                                findNavController().navigate(directions)
                            } else {
                                if (task2.exception is FirebaseAuthInvalidCredentialsException) {
                                    val snackBar = Snackbar.make(view, "Incorrect credentials entered!!",BaseTransientBottomBar.LENGTH_SHORT)
                                    snackBar.setTextColor(Color.parseColor("#de1d23"))
                                    snackBar.show()
                                }
                            }
                        }
                    }
                    else {
                        val snackBar = Snackbar.make(view, "Login failed!!",BaseTransientBottomBar.LENGTH_SHORT)
                        snackBar.setTextColor(Color.parseColor("#de1d23"))
                        snackBar.show()
                    }
                }

            } else {
                binding.emailField.error = "Email is required!!"
                binding.passwordField.error = "Password is required!!"
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}