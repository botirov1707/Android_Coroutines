package com.example.android_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.android_coroutines.model.Post
import com.example.android_coroutines.network.RetrofitHttp
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(),CoroutineScope {
    private lateinit var job: Job
    lateinit var textView: TextView
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        job = Job()
    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun initViews() {
        textView = findViewById(R.id.textView)
        launch {
            val userOne = async(Dispatchers.IO){fetchFirstPost()}
            val userTwo = async(Dispatchers.IO){fetchSecondPost()}
            showPosts(userOne.await(),userTwo.await())
        }
    }

//
//    private fun fetchAndShowPost(){
//        GlobalScope.launch (Dispatchers.Main){
//            val post = fetchPost()
//            showPost(post)
//        }
//    }
//
//    suspend fun fetchPost():Post {
//        return GlobalScope.async(Dispatchers.IO) {
//            val response = RetrofitHttp.postService.getPost(1).execute()
//            return@async response.body()!!
//        }.await()
//    }
//
//    private fun showPost(post: Post){
//        textView.text = post.toString()
//    }

    suspend fun fetchFirstPost():Post{
        return GlobalScope.async(Dispatchers.IO){
            val response = RetrofitHttp.postService.getPost(1).execute()
            return@async response.body()!!
        }.await()
    }


    suspend fun fetchSecondPost():Post{
        return GlobalScope.async(Dispatchers.IO){
            val response = RetrofitHttp.postService.getPost(2).execute()
            return@async response.body()!!
        }.await()
    }

    fun loadTwoPosts(){
        GlobalScope.launch(Dispatchers.Main){
            val userOne = withContext(Dispatchers.IO){fetchFirstPost()}
            val userTwo = withContext(Dispatchers.IO){fetchSecondPost()}
            showPosts(userOne,userTwo)
        }
    }

        private fun showPosts(post1: Post,post2:Post){
        textView.text = post1.toString()+post2.toString()
    }


}