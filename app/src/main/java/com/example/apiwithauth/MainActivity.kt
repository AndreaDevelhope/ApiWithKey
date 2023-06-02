package com.example.apiwithauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiwithauth.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface JokesInterface {
    @GET("images/search?limit=25")
    suspend fun getAPIcall(): List<ApiClass>
}

const val AUTHORIZATION_HEADER = "x-api-key"
const val CORRECT_KEY = "live_7tO6TXwW8b0JbMQF7bJhlOJ9Dn9zzuI4dFdzVu8kS97vcp59vbsP0PyVhO3ofHOs"
private var key =""

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(AUTHORIZATION_HEADER, key).build()
        return chain.proceed(newRequest)
    }
}

private const val BASE_URL =
    "https://api.thedogapi.com/v1/"

class MainActivity : AppCompatActivity() {

    private val logging = HttpLoggingInterceptor()
    private val authorization = AuthorizationInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorization)
        .build()

    private lateinit var binding: ActivityMainBinding

    private val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    private val retrofitCreateClassAPI = retrofit.create(JokesInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.buttonWrong.setOnClickListener {
            key = "not-gonna-work"
            Toast.makeText(
                this,
                "Without a valid key, the limit of dogs called is 10",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonCorrect.setOnClickListener {
            key = CORRECT_KEY
            Toast.makeText(
                this,
                "With a valid key, the limit of dogs called is 25",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.retry.setOnClickListener {
            callAPI()
        }
    }

    private fun callAPI() {
        lifecycleScope.launch {
            try {
                val apiResult = retrofitCreateClassAPI.getAPIcall()
                binding.recyclerView.adapter = ListAdapter(apiResult)
            } catch (e: java.lang.Exception) {
                Log.e("CallAPI", "$e")
                Snackbar.make(
                    findViewById(R.id.main_view),
                    getString(R.string.error_message),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.error_message)) { callAPI() }.show()
            }
        }
    }

}