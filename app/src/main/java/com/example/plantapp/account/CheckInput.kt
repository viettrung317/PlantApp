package com.example.plantapp.account
import java.util.regex.Pattern
class CheckInput {
    fun validateEmail(email: String): Boolean {
        // regular expression pattern để kiểm tra định dạng email
        val emailPattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    fun validatePass(password :String) :Boolean{
        val pass="^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+=!])(?=\\S+\$).{11}$".toRegex()
        return pass.matches(password)
    }
}