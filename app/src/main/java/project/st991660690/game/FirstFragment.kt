package project.st991660690.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Visibility
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import project.st991660690.game.databinding.FragmentFirstBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private var noOfTries: Int = 0
    private var successfulTries: Int = 0
    private var username: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        if (bundle == null) {
            Log.d("Confirmation", "Fragment 1 didn't receive info")
            return
        }

        val args = FirstFragmentArgs.fromBundle(bundle)

        val index = args.email.toString().indexOf("@")
        username = "" + args.email.toString().substring(0, index)
        binding.txtUserMsg.text = "Hello, " + username + "!"


        binding.txtNum1.visibility = View.INVISIBLE
        binding.txtNum2.visibility = View.INVISIBLE

        fun generateRandomNumber(): Int {
            return Random.nextInt(1, 11)
        }

        binding.btnLess.setOnClickListener {
            val num1 = generateRandomNumber()
            val num2 = generateRandomNumber()
            binding.txtNum1.text = num1.toString()
            binding.txtNum2.text = num2.toString()
            binding.txtNum1.visibility = View.VISIBLE
            binding.txtNum2.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.txtNum1.visibility = View.INVISIBLE
                binding.txtNum2.visibility = View.INVISIBLE
            }, 2000)
            if (num1 < num2) {
                noOfTries++
                successfulTries++
            } else {
                noOfTries++
            }
        }

        binding.btnMore.setOnClickListener {
            val num1 = generateRandomNumber()
            val num2 = generateRandomNumber()
            binding.txtNum1.text = num1.toString()
            binding.txtNum2.text = num2.toString()
            binding.txtNum1.visibility = View.VISIBLE
            binding.txtNum2.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.txtNum1.visibility = View.INVISIBLE
                binding.txtNum2.visibility = View.INVISIBLE
            }, 2000)
            if (num1 > num2) {
                noOfTries++
                successfulTries++
            } else {
                noOfTries++
            }
        }

        binding.btnEqual.setOnClickListener {
            val num1 = generateRandomNumber()
            val num2 = generateRandomNumber()
            binding.txtNum1.text = num1.toString()
            binding.txtNum2.text = num2.toString()
            binding.txtNum1.visibility = View.VISIBLE
            binding.txtNum2.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.txtNum1.visibility = View.INVISIBLE
                binding.txtNum2.visibility = View.INVISIBLE
            }, 2000)
            if (num1 == num2) {
                noOfTries++
                successfulTries++
            } else {
                noOfTries++
            }
        }

        binding.btnEnd.setOnClickListener {
            if (noOfTries.toString().isDigitsOnly() && successfulTries.toString().isDigitsOnly()) {
                val directions = FirstFragmentDirections.actionFirstFragmentToSecondFragment(noOfTries,successfulTries,username)
                findNavController().navigate(directions)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}