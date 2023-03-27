package com.lifespandh.ireflexions.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SurveyQuestion(

    @SerializedName("question")
    val question: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("id")
    val id: Int
//    @SerializedName("options")
//    val options: JsonObject

)

data class SurveyResponse(

    @SerializedName("survey_question")
    val question: SurveyQuestion,

    @SerializedName("response")
    val response: Float

)