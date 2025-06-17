package com.telsjinn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var vehicleIdInput: EditText
    private lateinit var tokenInput: EditText
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vehicleIdInput = findViewById(R.id.vehicle_id_input)
        tokenInput = findViewById(R.id.token_input)
        statusText = findViewById(R.id.status_text)

        findViewById<Button>(R.id.load_button).setOnClickListener {
            loadVehicles(tokenInput.text.toString())
        }

        findViewById<Button>(R.id.after_blow_button).setOnClickListener {
            sendAfterBlow(vehicleIdInput.text.toString(), tokenInput.text.toString())
        }
    }

    private fun loadVehicles(token: String) {
        statusText.text = "Loading vehicles..."
        val request = Request.Builder()
            .url("https://owner-api.teslamotors.com/api/1/vehicles")
            .header("Authorization", "Bearer $token")
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { statusText.text = "Error: ${'$'}{e.message}" }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = it.body?.string()
                    if (body != null) {
                        val data = JSONObject(body)
                        val arr = data.getJSONArray("response")
                        if (arr.length() > 0) {
                            val vehicle = arr.getJSONObject(0)
                            val vid = vehicle.getLong("id")
                            runOnUiThread {
                                vehicleIdInput.setText(vid.toString())
                                statusText.text = "Loaded vehicle: ${'$'}{vehicle.getString("display_name")}"
                            }
                        } else {
                            runOnUiThread { statusText.text = "No vehicles found" }
                        }
                    }
                }
            }
        })
    }

    private fun sendAfterBlow(vehicleId: String, token: String) {
        statusText.text = "Sending command..."
        val request = Request.Builder()
            .url("https://owner-api.teslamotors.com/api/1/vehicles/${'$'}vehicleId/command/after_blow")
            .post(RequestBody.create(null, ByteArray(0)))
            .header("Authorization", "Bearer $token")
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { statusText.text = "Error: ${'$'}{e.message}" }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread { statusText.text = "After blow triggered" }
                } else {
                    runOnUiThread { statusText.text = "Command failed" }
                }
            }
        })
    }
}

