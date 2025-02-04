package com.example.passgenii

import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlin.random.Random
import android.widget.TextView
import android.widget.Toast

//CONSTANTs:
private const val STRING_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\\$%^&*()_\"-+=.,<>{}[]:;'?" //List of chars to generate from
private const val NUM_CHARS = "0123456789" //list of digits to use to generate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("PassGeN")

        //Setting the UI icons to dark mode so they can be visible.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val generate = findViewById<Button>(R.id.gen) //Button to generate
        val copy = findViewById<Button>(R.id.copy) //Button to copy krypt message
        val display = findViewById<TextView>(R.id.passShow)

        val genView = findViewById<TextView>(R.id.genView)

        //radio button for passcode and password:
        val authG = findViewById<RadioGroup>(R.id.authCred)

        //editText for number of pass
        val authL = findViewById<EditText>(R.id.authLen)

        //default passlength
        var authLength = 0

        //Hidden B
        copy.visibility = Button.GONE
        generate.visibility = Button.GONE
        genView.visibility = TextView.GONE

        //Variable that will hold the selected auth credential (Radio Button)
        var aCred: String = ""
        //Tracking the selected radio option
        authG.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            // Converting the chosen option to string and then to int
            aCred = radio.text.toString()
        }

        generate.visibility = Button.VISIBLE //Showing the generate button

        //Generate Credential
        generate.setOnClickListener {
            val authText = authL.text.toString()
            if (authText.isEmpty()) {
                //Toast error
                Toast.makeText(applicationContext, "No number entered", Toast.LENGTH_SHORT).show()

                display.text = "" //Clear existing generated texts

                genView.visibility = TextView.GONE //Hide gen view
                copy.visibility = Button.GONE //Hiding the copy button

            } else {
                //convert entered number to int
                authLength = authText.toInt()

                //Switch case for radio button options
                when (aCred) {
                    "Password" -> { //when password is selected
                        val randomString = generatePassW(authLength)
                        genView.visibility = TextView.VISIBLE
                        display.text = randomString
                        copy.visibility = Button.VISIBLE //Making the copy button visible
                    }
                    "Passcode" -> { //when passcode is selected
                        val randomString = generatePassC(authLength)
                        genView.visibility = TextView.VISIBLE
                        display.text = randomString //displaying the generated passcode on the display viewer
                        copy.visibility = Button.VISIBLE //Making the copy button visible
                    }
                    "" -> {
                        Toast.makeText(applicationContext, "Please select credential type", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        //Copy button Method
        copy.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = android.content.ClipData.newPlainText("Copied Text", display.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(applicationContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    //Gen PassWord
    fun generatePassW(length: Int): String { //Password generator

        //Randomiser
        val random = Random(System.currentTimeMillis())

        return if(!checkLength(length)) {
            Toast.makeText(applicationContext, "ERRoR: Please enter a length less than 200", Toast.LENGTH_SHORT).show() //toast if check is false
            "" //return empty string if check is false
        } else {
            (1..length)
                .map { random.nextInt(STRING_CHARS.length) }
                .map(STRING_CHARS::get)
                .joinToString("")
        }
    }

    //Gen PassCode
    fun generatePassC(length: Int): String { //Passcode generator
        val random = Random(System.currentTimeMillis())

        return if(!checkLength(length)) {
            Toast.makeText(applicationContext, "ERRoR: Please enter a length less than 200", Toast.LENGTH_SHORT).show() //toast if check is false
            "" //return empty string if check is false
        } else {
            (1..length)
                .map { random.nextInt(NUM_CHARS.length) }
                .map(NUM_CHARS::get)
                .joinToString("")
        }
    }

    //credentials length checkeR
    private fun checkLength(passLength: Int): Boolean {
        return passLength <= 200 //check the pass length, if less or equal to 200
    }

}
