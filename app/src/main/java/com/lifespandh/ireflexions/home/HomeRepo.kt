package com.lifespandh.ireflexions.home

import com.google.gson.JsonObject
import com.lifespandh.ireflexions.api.ApiClient
import com.lifespandh.ireflexions.models.*
import com.lifespandh.ireflexions.models.howAmIToday.DailyCheckInEntry
import com.lifespandh.ireflexions.utils.network.NetworkResult
import com.lifespandh.ireflexions.utils.network.safeApiCall
import okhttp3.RequestBody
import javax.inject.Inject

class HomeRepo @Inject constructor(private val apiClient: ApiClient) {

    suspend fun getExercises(): NetworkResult<List<Exercise>> {
        var networkResult: NetworkResult<List<Exercise>>? = null

        safeApiCall({
            apiClient.getExercises()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getPrograms(): NetworkResult<List<Program>> {
        var networkResult: NetworkResult<List<Program>>? = null

        safeApiCall({
            apiClient.getPrograms()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }
    suspend fun getRegisteredProgramList(): NetworkResult<List<Program>> {
        var networkResult: NetworkResult<List<Program>>? = null

        safeApiCall({
            apiClient.getRegisteredProgramList()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getUserProgramProgress(): NetworkResult<UserProgramProgress> {
        var networkResult: NetworkResult<UserProgramProgress>? = null

        safeApiCall({
            apiClient.getUserProgramProgress()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun addUserToProgram(requestBody: RequestBody): NetworkResult<Program> {
        var networkResult: NetworkResult<Program>? = null

        safeApiCall({
            apiClient.addUserToProgram(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getCourses(requestBody: RequestBody): NetworkResult<List<Course>> {
        var networkResult: NetworkResult<List<Course>>? = null

        safeApiCall({
            apiClient.getCourses(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getLessons(requestBody: RequestBody): NetworkResult<List<Lesson>> {
        var networkResult: NetworkResult<List<Lesson>>? = null

        safeApiCall({
            apiClient.getLessons(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getLessonQuestions(requestBody: RequestBody): NetworkResult<List<LessonQuestion>> {
        var networkResult: NetworkResult<List<LessonQuestion>>? = null

        safeApiCall({
            apiClient.getLessonQuestions(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun saveProgramProgress(requestBody: RequestBody): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.saveProgramProgress(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getSupportContacts(): NetworkResult<List<SupportContact>> {
        var networkResult: NetworkResult<List<SupportContact>>? = null

        safeApiCall({
            apiClient.getSupportContacts()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun addSupportContact(supportContact: SupportContact): NetworkResult<SupportContact> {
        var networkResult: NetworkResult<SupportContact>? = null

        safeApiCall({
            apiClient.addSupportContact(supportContact)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun editSupportContact(supportContact: SupportContact): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.editSupportContact(supportContact)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun deleteSupportContact(requestBody: RequestBody): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.deleteSupportContact(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getJournalEntries(requestBody: RequestBody): NetworkResult<List<DailyCheckInEntry>> {
        var networkResult: NetworkResult<List<DailyCheckInEntry>>? = null

        safeApiCall({
            apiClient.getJournalEntries(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getCareCenterExercises(requestBody: RequestBody): NetworkResult<List<CareCenterExercise>> {
        var networkResult: NetworkResult<List<CareCenterExercise>>? = null

        safeApiCall({
            apiClient.getCareCenterExercises(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun getResourceContent() : NetworkResult<List<ResourceLibraryItem>> {
        var networkResult: NetworkResult<List<ResourceLibraryItem>>? = null

        safeApiCall({
            apiClient.getResourceContent()
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    // HeyPeers API functions
    suspend fun heyPeersAuthenticate(requestBody: RequestBody): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.heyPeersAuthenticate(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun heyPeersCreateUser(id: Int, token: String, requestBody: RequestBody): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.heyPeersCreateUser(id, token, requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun heyPeersGenerateOTLLink(id: Int, uuid: String, token: String): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.heyPeersGenerateOTLLink(id, uuid, token)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

    suspend fun saveHPUUID(requestBody: RequestBody): NetworkResult<JsonObject> {
        var networkResult: NetworkResult<JsonObject>? = null

        safeApiCall({
            apiClient.saveHPUUID(requestBody)
        }, {
            networkResult = it
        }, {
            networkResult = it
        })

        return networkResult!!
    }

}