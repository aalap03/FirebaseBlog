package com.example.aalap.blogs.TabFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aalap.blogs.Models.MainHitsObject
import com.example.aalap.blogs.R
import com.example.aalap.blogs.RetrofitUtils.RetrofitCreator
import com.example.aalap.blogs.RetrofitUtils.RetrofitElasticSearch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class FragmentSearch : Fragment(), AnkoLogger {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = RetrofitCreator().retrofit().create(RetrofitElasticSearch::class.java)

        info { "getting list" }
        retrofit.getMainHits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> handleResponse(t) }, { error -> handleError(error) })

    }

    private fun handleError(error: Throwable?) {
        info { "fuck its error" }
        info { error?.localizedMessage }
    }

    private fun handleResponse(response: Response<MainHitsObject>) {
        info { "got response" }
        if(response.isSuccessful) {
            info { response.body()?.hits }
            info { response.body()?.hits?.post }
        }else {
           info { response.errorBody()?.string()}
        }
    }
}