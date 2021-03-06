package com.skdevstudio.encrypto

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.skdevstudio.encrypto.databinding.ActivityMainBinding
import java.util.concurrent.Executor
import java.util.jar.Attributes

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imm : InputMethodManager
    private lateinit var encryption_key : String
    private lateinit var encryption_key_confirm : String

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.input.requestFocus()
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


        var helper = DBHelper(applicationContext)
        var db = helper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM USERDATA",null)
        var x = 1;

        if(rs.moveToNext())
            toast(rs.getString(2))

        binding.next.setOnClickListener {
           if(!binding.input.text.isEmpty()){
               encryption_key = binding.input.text.toString()
               binding.next.visibility = View.GONE
               binding.setKey.visibility = View.VISIBLE
               binding.input.text.clear()
               binding.instruction.text = getString(R.string.confirm_key_text)
           }else{
               toast("Enter key!!!")
           }
        }

        binding.setKey.setOnClickListener {
            encryption_key_confirm = binding.input.text.toString()
            if(encryption_key.equals(encryption_key_confirm)){
                var key_cv = ContentValues()
                key_cv.put("SECRET_SECONDARY_KEY",encryption_key)
                db.insert("KEYS",null,key_cv)
            }else{
                binding.input.error = "Mismatched Password!"
                binding.input.requestFocus()
            }
        }


    }


    public fun toast(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

}