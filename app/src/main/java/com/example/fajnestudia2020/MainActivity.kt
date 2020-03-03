package com.example.fajnestudia2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


 //Main activity - responsible for app menu
class MainActivity : AppCompatActivity() {

    //Components from layout that are changed or require additional setup
    private lateinit var buttonLearn: Button
    private lateinit var buttonLoad: Button
    private lateinit var  buttonSave: Button
    private lateinit var textLoadedData: TextView

    //Variable that stores name of currently loaded data file
    private  var loadedData: String? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check if intent that created this activity has info about currently loaded data file
        loadedData=intent.getStringExtra("selectedDataSet")


        //Setting on click listeners for buttons
        buttonLearn=findViewById(R.id.buttonMenuLearn)
        buttonLearn.setOnClickListener {
            startLearnActivity()
        }
        buttonLoad=findViewById(R.id.buttonMenuLoad)
        buttonLoad.setOnClickListener {
            startLoadActivity()
        }
        buttonSave=findViewById(R.id.buttonMenuSave)
        buttonSave.setOnClickListener {
            startSaveActivity()
        }
        textLoadedData=findViewById(R.id.textLoadedData)
        //Displaying currently loaded data set or showing no data message if no data loaded
        textLoadedData.text=getString(R.string.text_main_loaded_data, loadedData?:getString(R.string.text_no_data))
    }

    override fun onResume() {
        super.onResume()
        //Check if currently loaded data was not deleted
        if(loadedData!=null && !this.fileList().contains(loadedData))
        {
            loadedData=null;
            textLoadedData.text=getString(R.string.text_main_loaded_data, getString(R.string.text_no_data))
            Toast.makeText(this,"Poprzednio wybrane dane zostały usunięte",Toast.LENGTH_SHORT).show()
        }


    }

    //function that starts LearnActivity activity
    private fun startLearnActivity() {
        if(loadedData!=null)
        {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("selectedDataSet", loadedData)
            startActivity(intent)
        }
        else
            Toast.makeText(this, "Przed rozpoczęciem wybierz zestaw danych",Toast.LENGTH_SHORT).show()
    }

     //function that starts SaveData activity
    private fun startSaveActivity() {
        val intent= Intent(this,SaveData::class.java)
        startActivity(intent)
    }

    //function that starts LoadData activity
    private fun startLoadActivity() {
        if(fileList().isNotEmpty()) {
            val intent = Intent(this, LoadData::class.java)
            startActivity(intent)
        }
        else
            Toast.makeText(this,"Nie znaleziono żadnych zapisanych danych na urządzeniu, dodaj jakieś dane",Toast.LENGTH_SHORT).show()
    }

}
