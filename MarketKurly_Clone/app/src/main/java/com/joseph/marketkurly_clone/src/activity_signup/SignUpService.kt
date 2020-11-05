package com.joseph.marketkurly_clone.src.activity_signup

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.joseph.marketkurly_clone.BaseActivity
import com.joseph.marketkurly_clone.Constants.TAG
import com.joseph.marketkurly_clone.NetworkConstants
import com.joseph.marketkurly_clone.RetrofitClient
import com.joseph.marketkurly_clone.src.activity_signup.interfaces.SignUpValidationEvent
import com.joseph.marketkurly_clone.src.activity_signup.models.UserInfo
import com.joseph.marketkurly_clone.src.activity_signup.network.SignUpAPI
import com.joseph.marketkurly_clone.src.util.isJsonObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService(private var validationListener: SignUpValidationEvent) {

    private var mRetrofitClient = RetrofitClient.getClient(NetworkConstants.KURLY_URL).create(SignUpAPI::class.java)

    // 아이디 중복체크
    fun checkDuplicateID(id: String) {
        mRetrofitClient.checkDuplicateID(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val result = response.body()?.get("is_exist")?.asString!!
                validationListener.onCheckIdSuccess(result)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                validationListener.onCheckIdFail(t.message!!)
            }

        })
    }

    fun checkPhoneNumber(phoneNumber: String) {
        mRetrofitClient.checkPhoneNumber(phoneNumber).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val result = response.body()?.get("is_exist")?.asString!!
                validationListener.onCheckPhoneNumSuccess(result)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                validationListener.onCheckPhoneNumFail(t.message!!)
            }

        })
    }

    fun signUp(user: UserInfo) {
        val jsonObject = JsonObject()

        jsonObject.apply {
            addProperty("id", user.id)
            addProperty("password", user.password)
            addProperty("password_check", user.passwordCheck)
            addProperty("phone_number", user.phoneNumber)
            addProperty("post_code", user.postCode)
            addProperty("address", user.address)
            addProperty("address_detail", user.addressDetail)
            addProperty("name", user.name)
            addProperty("email", user.email)
            addProperty("birth", user.birth)
            addProperty("gender", user.gender)
            addProperty("recommend_user_id", user.recommendUserId)
            addProperty("event", user.event)
            addProperty("personal_agree", user.personalAgree)
            addProperty("email_agree", user.emailAgree)
            addProperty("sms_agree", user.smsAgree)
        }

        mRetrofitClient.signUp(jsonObject).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d(TAG, "[SignUpService] - onResponse() : 성공")
                validationListener.onSignUpSuccess()

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d(TAG, "[SignUpService] - onFailure() : 실패")
                validationListener.onSignUpFail()
            }

        })
    }
}