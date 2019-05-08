package eventcoordinator2017.myevent.ui.login

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import eventcoordinator2017.myevent.R
import eventcoordinator2017.myevent.app.App
import eventcoordinator2017.myevent.app.Constants
import eventcoordinator2017.myevent.model.data.User
import eventcoordinator2017.myevent.model.response.LoginResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class LoginPresenter : MvpNullObjectBasePresenter<LoginView>() {

    fun login(email: String, password: String) {
        if (email.isEmpty() || email == "") {
            view.showAlert("Please enter email")
        } else if (password.isEmpty() || password == "") {
            view.showAlert("Please enter Password")
        } else {
            view.startLoading()
            App.getInstance().apiInterface.login(email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.startLoading() }
                    .doFinally { view.stopLoading() }
                    .subscribe(object : DisposableSingleObserver<LoginResponse>() {
                        override fun onSuccess(loginResponse: LoginResponse) {
                            when (loginResponse.result) {
                                Constants.SUCCESS -> {
                                    val realm = Realm.getDefaultInstance()
                                    realm.executeTransactionAsync({ realm ->
                                        val user = loginResponse.user
                                        realm.copyToRealmOrUpdate(user)
                                    }, {
                                        realm.close()
                                        view.onLoginSuccess()
                                    }, { error ->
                                        realm.close()
                                        Log.e(TAG, "onError: Unable to save USER", error)
                                        view.showAlert("Error Saving API Response")
                                    })
                                }
                                Constants.NOT_EXIST -> {
                                    view.showAlert("Email does not exist")
                                    view.showAlert("Wrong Password")
                                }
                                Constants.WRONG_PASSWORD -> view.showAlert("Wrong Password")

                                else -> view.showAlert(R.string.oops.toString())
                            }
                        }

                        override fun onError(e: Throwable) {
                            Log.d(TAG, e.localizedMessage)
                        }

                    })
        }
    }


/*
    try {
        when (response.body()!!.result) {
            Constants.SUCCESS -> {
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync({ realm ->
                    val user = response.body()!!.user
                    realm.copyToRealmOrUpdate(user)
                }, {
                    realm.close()
                    view.onLoginSuccess()
                }, { error ->
                    realm.close()
                    Log.e(TAG, "onError: Unable to save USER", error)
                    view.showAlert("Error Saving API Response")
                })
            }
            Constants.NOT_EXIST -> {
                view.showAlert("Email does not exist")
                view.showAlert("Wrong Password")
            }
            Constants.WRONG_PASSWORD -> view.showAlert("Wrong Password")
            else -> view.showAlert(R.string.oops.toString())
        }
    } catch (e: NullPointerException) {
        e.printStackTrace()
        view.showAlert("Oops")
    }*/

    companion object {
        private val TAG = LoginPresenter::class.java.simpleName
    }
}
