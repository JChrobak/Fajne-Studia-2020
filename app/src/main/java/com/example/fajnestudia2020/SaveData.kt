package com.example.fajnestudia2020

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.lang.Exception
import java.util.regex.Pattern

/**
Activity that is responsible for creating new data sets.
Every data set is saved to internal storage (no permissions needed).
Every new data set is validated and must abide by those formatting rules
- Question: line of text ended by the new line character
- Answer: 0 or 1 (false or true)
- Explanation: line of text
Multiple questions are divided with the new line character
Example of correct data:
Does 2+2=4?
1
I don't think that this needs an explanation.
Does 2+3=0
0
Sum of two numbers that are bigger than 0 is always bigger than 0.
 */


class SaveData : AppCompatActivity() {
    //Components that store user input or require additional setup
    private lateinit var etTitle: EditText
    private lateinit var etData: EditText
    private lateinit var buttonAdd: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_data)
        etTitle=findViewById(R.id.editTextTitle)
        etData=findViewById(R.id.editTextData)
        buttonAdd=findViewById(R.id.buttonSave)
        //Set button onClickListener
        buttonAdd.setOnClickListener {
            addNewDataSet()
        }
    }

    //Function that validates user input and saves it as a new data set if it is correct
    private fun addNewDataSet() {
        //Check if both fields were filled
        if(etTitle.text.isNotBlank()&& etData.text.isNotBlank())
        {
            val fileName:String=etTitle.text.toString()
            val data:String=etData.text.toString()
            //Validate data
            if(validateData(data))
            {
                //If valid open and overwrite existing file or create a new one
                var fos: FileOutputStream
                try {
                    fos = openFileOutput(fileName, Context.MODE_PRIVATE)
                    fos.write(data.toByteArray())
                    Toast.makeText(this, "Zapisano do $filesDir/$fileName", Toast.LENGTH_LONG).show()
                    //Return to main menu
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    //Error control
                    Toast.makeText(this, "Wystąpił wyjątek: ${e.toString()}", Toast.LENGTH_LONG).show()
                }
            }
            else
                Toast.makeText(this,"Niepoprawny format danych. Zestaw nie został dodany.",Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show()
    }

    //Function that validates the data using regular expressions
    private fun validateData(data: String):Boolean
    {
        val regex="(.+?\\n[01]\\n.+?\\n?)+?"
        return Pattern.matches(regex,data);
    }
}
