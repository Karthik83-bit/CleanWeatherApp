package com.example.weatherapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * returnResponseBodyFlow handle the API response,
 * convert the dto response to domain response
 * extracting the error according to the error code
 * **/
@SuppressLint("LogNotTimber")


fun <T, O> handleFlowResponse(
    call: suspend () -> Response<T>, mapFun: (it: T) -> O
): Flow<NetworkResponse<O>> {

    return flow {
        emit(NetworkResponse.isLoading())
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val data = response.body()?.let { mapFun(it) }
                if(data!=null){
                    emit(NetworkResponse.isSuccess(data))
                }
            } else {
                val errorBody = response.errorBody()!!.string()
                var errorMsg = ""
                try {
                    val jObjError = JSONObject(errorBody)
                    when {
                        jObjError.has("apiComment") -> {
                            errorMsg = jObjError.getString("apiComment")
                        }
                        jObjError.has("message") -> {
                            errorMsg = jObjError.getString("message")
                        }
                        jObjError.has("data") -> {
                            try {
                                val dataObj = jObjError.getJSONObject("data")
                                if (dataObj.has("statusDesc")) {
                                    errorMsg = dataObj.getString("statusDesc")
                                }
                            } catch (e: Exception) {
                                if (jObjError.has("data")) {
                                    errorMsg = jObjError.getString("data")
                                } else {
                                    if (errorBody.isNotEmpty()) {
                                        // Display or handle the error message
                                        errorMsg = errorBody
                                    }
                                }
                            }
                        }
                        jObjError.has("fault") -> {
                            val faultError = jObjError.getJSONObject("fault")
                            if (faultError.has("faultstring")) {
                                errorMsg = faultError.getString("faultstring")
                            }
                        }
                        jObjError.has("transactionStatus") -> {
                            errorMsg = jObjError.getString("transactionStatus")
                        }
                        jObjError.has("statusDesc") -> {
                            errorMsg = jObjError.getString("statusDesc")
                        }
                        else -> {
                            if (errorBody.isNotEmpty()) {
                                // Display or handle the error message
                                errorMsg = errorBody
                            }
                        }
                    }
                    if (errorMsg.isNotEmpty()) {
                        emit(NetworkResponse.Failure(errorMsg))
                    } else if (errorBody.isNotEmpty()) {
                        // Display or handle the error message
                        emit(NetworkResponse.Failure(errorBody))
                    } else {
                        emit(NetworkResponse.Failure("Something went wrong"))
                    }
                } catch (e: Exception) {
                    if (errorBody.isNotEmpty()) {
                        // Display or handle the error message
                        emit(NetworkResponse.Failure(errorBody))
                        return@flow
                    }
                    emit(NetworkResponse.Failure("Something went wrong"))
                }
            }
        } catch (e: IOException) {
            e.message?.let { emit(NetworkResponse.Failure(it)) }
        } catch (e: HttpException) {
            e.message?.let { emit(NetworkResponse.Failure(it)) }
        } catch (e: IllegalStateException) {
            e.message?.let { emit(NetworkResponse.Failure(it)) }
        } catch (e: Exception) {
            e.message?.let { emit(NetworkResponse.Failure(it)) }
        }

    }
}


/**
 * handle() takes the response from use case function as Resource<> with in Main Coroutine Scope
 * return the extracted response with in onLoading(),onFailure(),onSuccess()
 * **/
fun<T> handleFlow(response: Flow<NetworkResponse<T>>, loading:MutableState<Boolean>, error: MutableState<Boolean>, errormsg:MutableState<String>, success:(T)->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        response.collectLatest {
            when (it) {
                is NetworkResponse.Failure -> {
                    error.value=true
                    errormsg.value=it.msg?:"Something Went wrong"
                    loading.value=false
                }

                is NetworkResponse.isLoading -> {
                   loading.value=true
                }

                is NetworkResponse.isSuccess -> {
                    loading.value=false

                    if(it.data!=null){
                        success(it.data)
                    }
                    else{
                        error.value=true
                        errormsg.value="Data couldnt be fetched"
                    }




                }

                else -> {}
            }
        }
    }
}
//fun <T> handleFlow(
//    response: Flow<NetworkResponse<T>>,
//    onLoading: () -> Unit,
//    onFailure: (it: String?) -> Unit,
//    onSuccess: (it: T?) -> Unit
//) {
//    CoroutineScope(Dispatchers.Main).launch {
//        response.collectLatest {
//            when (it) {
//                is NetworkResponse.Failure -> {
//                    onFailure.invoke(it.msg ?: "")
//                }
//
//                is NetworkResponse.isLoading -> {
//                    onLoading.invoke()
//                }
//
//                is NetworkResponse.isSuccess -> {
//
//
//                    onSuccess.invoke(it.data)
//
//                }
//
//                else -> {}
//            }
//        }
//    }
//}
