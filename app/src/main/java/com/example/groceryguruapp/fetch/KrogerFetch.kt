package com.example.groceryguruapp.fetch

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

import okhttp3.RequestBody.Companion.toRequestBody

class KrogerFetch() {
    companion object {
        const val endpoint = "https://api.kroger.com/v1"
        const val Base64Cli = "Z3JvY2VyeWd1cnUtMmQzOTYxODAxODNhN2U3YWUxNjJmNjQ1ZGE3MTE3ZTAxOTkxMjM3ODk2MzA5MzI3Nzg2OnV0TGFDcVBCZ3FhSDA1VWxSRkw0OG50QmhSY2xUb3IybURUSlRneFo="
        const val grant = "grant_type=client_credentials"
        const val authenc = "application/x-www-form-urlencoded"
        private val client = OkHttpClient()

    }

    private fun oAuth2(scope: String): String {
        var token = ""
        var authend = "$endpoint/connect/oauth2/token"
        var authgrant = grant
        if(scope != "")
            authgrant = "$authgrant&scope=$scope"
        val mediaType = authenc.toMediaTypeOrNull()
        val body = authgrant.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(authend)
            .post(body)
            .addHeader("Content-Type", authenc)
            .addHeader("Authorization", "Basic $Base64Cli")
            .build()
        val response = client.newCall(request).execute()

        return response.body!!.string()
    }

    private fun locGetHttp(zipcode: String, token: String): String {
        var locend = "$endpoint/locations?filter.zipCode.near=$zipcode"
        var request = Request.Builder()
            .url(locend)
            .get()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = client.newCall(request).execute()

        return response.body!!.string()
    }

    private fun prodGetHttp(term: String, locationId: String, token: String): JSONObject {
        var prodend = "$endpoint/products"
        if(term != "")
            prodend = "$prodend/?filter.term=$term"
        if(locationId != null)
            prodend = "$prodend/?filter.locationId=$locationId"
        val request = Request.Builder()
            .url(prodend)
            .get()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = client.newCall(request).execute()

        return JSONObject(response.body!!.string())
    }

    fun getProds(items: Array<String>, zipcode: String, scope: String): String {
        var token = oAuth2(scope)
        /*
        var locationId = locGetHttp(zipcode, token)
        var prices = ArrayList<String>()
        for(item in items) {
            prices.add(prodGetHttp(item, locationId, token))
        }

        return prices
         */
        return token
    }
}