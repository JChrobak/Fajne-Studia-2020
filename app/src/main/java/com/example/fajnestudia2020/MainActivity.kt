package com.example.fajnestudia2020

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


//MainActivity - activity responsible for app menu
class MainActivity : AppCompatActivity() {

    //Components from layout that are changed or require additional setup
     private lateinit var buttonLearn: Button
     private lateinit var buttonLoad: Button
     private lateinit var  buttonSave: Button
     private lateinit var  buttonSaveExt: Button
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
        buttonSaveExt=findViewById(R.id.buttonMenuSaveExt)
        buttonSaveExt.setOnClickListener {
            startSaveExtActivity()
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
            loadedData=null
            textLoadedData.text=getString(R.string.text_main_loaded_data, getString(R.string.text_no_data))
            Toast.makeText(this,"Poprzednio wybrane dane zostały usunięte",Toast.LENGTH_SHORT).show()
        }


    }

    //Function that starts LearnActivity activity
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

     //Function that starts SaveData activity
    private fun startSaveActivity() {
        val intent= Intent(this,SaveData::class.java)
        startActivity(intent)
    }

     //Function that starts SaveDataExternal activity
     private fun startSaveExtActivity() {
         if(checkReadPermissions()) {
             val intent = Intent(this, SaveDataExternal::class.java)
             startActivity(intent)
         }
     }

    //Function that starts LoadData activity
    private fun startLoadActivity() {
        if(fileList().isNotEmpty()) {
            val intent = Intent(this, LoadData::class.java)
            startActivity(intent)
        }
        else
            Toast.makeText(this,"Nie znaleziono żadnych zapisanych danych na urządzeniu, dodaj jakieś dane",Toast.LENGTH_SHORT).show()
    }

    //Function that checks if user has given read external storage permission and requests them if they were not given
    private fun checkReadPermissions():Boolean{
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    //Function that checks results of permission check
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            1->{
                if(grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Nie udzielono pozwolenia na odczyt pamięci urządzenia.",Toast.LENGTH_SHORT).show()
                else
                    startSaveExtActivity()
            }
        }
    }
}
