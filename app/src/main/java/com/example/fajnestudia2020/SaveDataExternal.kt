package com.example.fajnestudia2020

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.*

//SaveDataExternal - activity that is responsible for creating data sets from external .txt files.
//All files must be in com.example.fajnestudia2020/files/text directory for the app to be detected.
class SaveDataExternal : AppCompatActivity() {

    //ListView from layout that will store all .txt files in /text directory
    private lateinit var  listViewExtFiles: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        listViewExtFiles=findViewById(R.id.listViewData)
        //Update list view
        updateListView()
        //On normal click select file
        listViewExtFiles.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                handleItemOnClick(position)
            }
    }

    //On resume refresh the ListView component, to prevent undetected changes in /text folder while the app is running
    override fun onResume() {
        super.onResume()
        listViewExtFiles.adapter=null
        updateListView()
    }

    //Function that handles clicking on item in the ListView
    private fun handleItemOnClick(position: Int) {
        val selectedDataSet = listViewExtFiles.getItemAtPosition(position) as String
        val selectedFile=File(this.getExternalFilesDir("text"),selectedDataSet)
        //Open selected file
        val fileInputStream= FileInputStream(selectedFile)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader= BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        //Add every line of text file to the StringBuilder
        while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text+"\n")
        }
        //Validate data in text file
        if(DataManager.validateData(stringBuilder.toString()))
        {
            //Create a dialog box to confirm user input
            val builder= AlertDialog.Builder(this@SaveDataExternal)
            builder.setTitle("Zapisz zbiór danych")
            builder.setMessage("Czy na pewno chcesz zapisać plik '$selectedDataSet' jako zbiór danych?")
            //On confirmation proceed to saving selected file as data set
            builder.setPositiveButton("Tak"){ _, _ ->
                saveSelectedFile(selectedDataSet.removeSuffix(".txt"),stringBuilder.toString())
            }
            builder.setNegativeButton("Nie",null)
            builder.show()
        }
        else
           Toast.makeText(this,"Wybrany plik tekstowy ma niepoprawny format. Wybierz plik z poprawnym formatem.",Toast.LENGTH_SHORT).show()
    }

    //Function that saves selected file, receives two parameters, name of new data set and contents of new data set
    private fun saveSelectedFile(fileName:String, data:String) {
        val fos: FileOutputStream
        try {
            //Create new file in internal storage or overwrite the old one
            fos = openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            Toast.makeText(this, "Zapisano nowy zbiór danych '$fileName'", Toast.LENGTH_LONG).show()
            //Return to main menu
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        } catch (e: IOException) {
            //Error control
            Toast.makeText(this, "Wystąpił wyjątek: ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    //Function that updates the ListView with names of all .txt files from /text directory
    private fun updateListView(){
            val textFiles: ArrayList<String> = ArrayList()
            val textDir=this.getExternalFilesDir("text")
            if(textDir!=null)
            {
                //Add all names of text files to the list
                for(file in textDir.listFiles())
                {
                    if(file.name.endsWith(".txt"))
                        textFiles.add(file.name)
                }
            }
            //Check if any .txt files are in the /text directory and if there are any create an adapter for the list view
            if (textFiles.isNotEmpty())
                listViewExtFiles.adapter =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textFiles)
            else {
                //If there are none return to main menu
                Toast.makeText(this, "Nie znaleziono żadnych zapisanych danych o rozszerzeniu .txt na urządzeniu.\nDodaj pliki wejściowe do folderu /com.example.fajnestudia2020/files/text/", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }
}
