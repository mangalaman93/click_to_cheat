click to cheat
==============
User clicks image of a MCQ (Multiple Choice Question) using his mobile camera or opens existing image of such genre. Then he can crop that part of the image which corresponds to the MCQ ( better the cropping better the probablity of answer detection #we are not implementing pattern recognition at present for "hacku").

Now our application detects the question and options and searches for question using some Natural Language Search engines and matches the search result with options and return the option which matches the most. [coz more than one option can match for some questions ,so we also calculate the % of match]

How
===
The image of the MCQ is taken from the camera of mobile. The text from the image is detected using Tesseract-OCR Library, Which is a c++ library. To run it on android we need a JNI Wrapper. Then we use Google Speling suggestion to rectify any erros in the spellings for the obtained text. We, then, use Wolfrom Alpha APIs to find the final answer to the question.
