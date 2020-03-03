package com.example.fajnestudia2020

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

//Learn activity - activity responsible for parsing data in saved data set and displaying questions and checking user answers
class LearnActivity : AppCompatActivity() {
    //Layout component that are changed or require additional setup
    //Only two buttons from layout are programmed because the app supports only true or false questions however i plan to expand it to single choice questions
    private lateinit var button0: Button
    private lateinit var button1: Button
    private lateinit var tvTitle: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var tvExplanation: TextView
    private lateinit var selectedDataSet: String
    //Variable that stores all questions parsed from the data set
    private lateinit var dataSet: ArrayList<QuestionData>
    //Variables that store number of all, current and correct questions
    private var currQuestion: Int=0
    private var correctQuestion: Int=0
    private var numQuestion: Int=0
    //Flag that indicates whether the app is waiting for an answer from a user or is displaying a correct answer
    private var awaitAnswer: Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        //Check if there is info about selected data set
        if(intent.getStringExtra("selectedDataSet")!=null)
        {
            tvTitle=findViewById(R.id.textLearnTitle)
            tvScore=findViewById(R.id.textLearnScore)
            tvQuestion=findViewById(R.id.textLearnQuestion)
            tvExplanation=findViewById(R.id.textLearnExplanation)

            button0=findViewById(R.id.buttonAnswer0)
            button1=findViewById(R.id.buttonAnswer1)

            selectedDataSet=intent.getStringExtra("selectedDataSet")

            //Parse the data from data set to the list
            parseData(selectedDataSet)
            //Set number of all questions
            numQuestion=dataSet.size
            //Update all text components
            updateView()

            button0.setOnClickListener {
                buttonPressed(0)
            }
            button1.setOnClickListener {
                buttonPressed(1)
            }
        }
    }

    //Function that handles button presses, takes one parameter which stores pressed button id
    private fun buttonPressed(id: Int) {
        //If there are still answers left
        if(currQuestion<numQuestion)
        {
            //Check if button press was an answer from user or a prompt to move to the next question
            if (awaitAnswer)
            {
                //If answer is correct set question background to green, if it was wrong to red
                if (id == dataSet[currQuestion].answer)
                {
                    tvQuestion.setBackgroundColor(Color.GREEN)
                    correctQuestion += 1
                }
                else
                    tvQuestion.setBackgroundColor(Color.RED)
                //Update user score
                tvScore.text=getString(R.string.text_learn_score,correctQuestion.toString(),numQuestion.toString())
                //Show explanation
                tvExplanation.isVisible = true

                currQuestion += 1
                //Set flag to false so the next button press (doesn't matter which) will display the next question
                awaitAnswer = false

            }
            else {
                //Set flag to true so next button press will be considered as an answer
                awaitAnswer = true
                //Update the view to display the next question
                updateView()
            }
        }
        else
            Toast.makeText(this,"Test ukończony, poprawnych odpowiedzi $correctQuestion/$numQuestion (zdawalność ${(correctQuestion.toFloat()/numQuestion.toFloat())*100}%)",Toast.LENGTH_SHORT).show()
    }

    //Function that updates all text views on the layout
    private fun updateView()
    {
        if(dataSet.isNotEmpty())
        {
            tvTitle.text=getString(R.string.text_learn_title,(currQuestion+1).toString(),numQuestion.toString())
            tvScore.text=getString(R.string.text_learn_score,correctQuestion.toString(),numQuestion.toString())
            tvQuestion.setBackgroundColor(Color.WHITE)
            tvQuestion.text=dataSet[currQuestion].question
            tvExplanation.isVisible=false
            tvExplanation.text=dataSet[currQuestion].explanation
        }
    }

    //Function that parses data from selected data set to array of questions
    private fun parseData(data: String) {
        //Open and read selected file
        var fileInputStream: FileInputStream? = null
        fileInputStream = openFileInput(selectedDataSet)
        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val splitData: ArrayList<String> = ArrayList()
        var text: String? = null
        //Add every line of text to the list
        while ({ text = bufferedReader.readLine(); text }() != null) {
                splitData.add(text?:"")
        }
        dataSet= ArrayList()
        //Move the data from the list to corresponding fields in question data array
        for(i in 0 until splitData.size step 3)
        {
            val temp= QuestionData(i/3, splitData[i],splitData[i+1].toInt(),splitData[i+2])
            dataSet.add(temp)
        }
        //Shuffle all the questions
        dataSet.shuffle()
    }

    //Data class that stores all info about specific question
    data class QuestionData(val id: Int, val question: String,val answer: Int, val explanation: String)
}
