# Fajne-Studia-2020
Simple Android app that can aid you in learning.
Just copy all the questions to your phone and get ready to learn.

Every data set is validated and must abide by those formatting rules
- Question: line of text ended by the new line character
- Answer: 0 or 1 (false or true)
- Explanation: line of text

Multiple questions are divided with the new line character.

Example of correct data:

```
Does 2+2=4?
1
I don't think that this needs an explanation.
Does 2+3=0
0
Sum of two numbers that are bigger than 0 is always bigger than 0.
```
Data validator was updated to be more forgiving. If you separate parts of question with multiple new line characters or add some white spaces before or after the number that corresponds to the question answer, data will still be accepted.

Example of new data that is correct now but was incorrect in later versions:
```

Does 2+2=4?
    1


I don't think that this needs an explanation.


Does 2+3=0

            0        

Sum of two numbers that are bigger than 0 is always bigger than 0.

```

You can add data to the app by copying and pasting it to the edit box inside the app or by copying a .txt file to its */files/text/* directory (*Android/data/com.example.fajnestudia2020/files/text/*).

Data sets from external .txt files will have the same name as the .txt file. If a data set with specific name already exists in internal storage it will be overwritten. 

Currently this app only supports true or false questions and i don't plan to expand it.
