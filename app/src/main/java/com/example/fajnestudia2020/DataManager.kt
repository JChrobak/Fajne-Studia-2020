package com.example.fajnestudia2020

import java.util.regex.Pattern

/**Class that provides data validation function
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

Data validator was updated to be more forgiving. If you separate parts of question with multiple new line characters or add some white spaces before or after
the number that corresponds to the question answer, data will still be accepted.
Example of new data that is correct now but was incorrect in later versions:
Does 2+2=4?
1


I don't think that this needs an explanation.
Does 2+3=0

0

Sum of two numbers that are bigger than 0 is always bigger than 0.
*/
class DataManager {
    //Function that validates the data using regular expressions
    companion object
    {
        fun validateData(data: String):Boolean
        {
            val regex="(.+?\\n+?\\p{Blank}*?[01]\\p{Blank}*?\\n+?.+?\\n*?)+?"
            return Pattern.matches(regex,data)
        }
    }

}