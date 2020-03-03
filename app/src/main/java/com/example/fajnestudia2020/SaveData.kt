package com.example.fajnestudia2020

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.io.IOException


//SavData - activity that is responsible for creating new data sets.
//Every data set is saved to internal storage (no permissions needed).
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
            if(DataManager.validateData(data))
            {
                //If valid open and overwrite existing file or create a new one
                val fos: FileOutputStream
                try {
                    fos = openFileOutput(fileName, Context.MODE_PRIVATE)
                    fos.write(data.toByteArray())
                    Toast.makeText(this, "Zapisano do $filesDir/$fileName", Toast.LENGTH_LONG).show()
                    //Return to main menu
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: IOException) {
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
}
