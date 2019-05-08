package eventcoordinator2017.myevent.ui.main

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import eventcoordinator2017.myevent.app.App
import eventcoordinator2017.myevent.model.data.Event
import eventcoordinator2017.myevent.model.data.User
import eventcoordinator2017.myevent.utils.DateTimeUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Case
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class MainPresenter : MvpNullObjectBasePresenter<MainView>() {
    private lateinit var realm: Realm
    private val TAG = MainPresenter::class.java.simpleName
    private lateinit var eventRealmResults: RealmResults<Event>
    fun onStart() {
        realm = Realm.getDefaultInstance()

        eventRealmResults = realm.where(Event::class.java).findAllAsync()
        eventRealmResults.addChangeListener(RealmChangeListener { view.refreshList() })

        getEvents()

    }

    /*private void manageList() {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            List<Event> eventUpcoming;
            List<Event> eventToday;
            eventUpcoming = realm.copyFromRealm(eventRealmResults.where().equalTo("eventDateFrom", DateTimeUtils.dateToday()).findAll());
            eventToday = realm.copyFromRealm(eventRealmResults.where().notEqualTo("eventDateFrom", DateTimeUtils.dateToday()).findAll());
            getView().setEvents(eventToday,eventUpcoming);
        }
    }*/

    fun getEvents() {
        App.getInstance().apiInterface.getAllEvents("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<List<Event>>() {
                    override fun onError(e: Throwable) {
                        Timber.e(e.localizedMessage)
                        view.showAlert("Error Connecting to Server")
                    }

                    override fun onSuccess(eventList: List<Event>) {
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransactionAsync({ realm -> realm.copyToRealmOrUpdate(eventList) }, { realm.close() }, { error ->
                            realm.close()
                            Timber.d("onError: Unable to save USER")
                        })
                    }

                })
        /*App.getInstance().apiInterface.getAllEvents("").enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                view.stopLoading()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync({ realm -> realm.copyToRealmOrUpdate(response.body()) }, { realm.close() }, { error ->
                    realm.close()
                    Log.e(TAG, "onError: Unable to save USER", error)
                })
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e(TAG, "onFailure: Error calling login api", t)
                view.stopLoading()
                view.showAlert("Error Connecting to Server")

            }
        })*/
    }


    fun onStop() {
        if (eventRealmResults.isValid) {
            eventRealmResults.removeAllChangeListeners()
        }
        realm.close()
    }


}
