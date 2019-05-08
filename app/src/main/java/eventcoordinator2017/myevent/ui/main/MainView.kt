package eventcoordinator2017.myevent.ui.main

import com.hannesdorfmann.mosby.mvp.MvpView

import eventcoordinator2017.myevent.model.data.Event
import eventcoordinator2017.myevent.model.data.User


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

interface MainView : MvpView {

    fun stopLoading()

    fun startLoading()

    fun displayUserData(user: User)

    fun showAlert(s: String)

    fun setEvents(eventToday: List<Event>, eventUpcoming: List<Event>)

    fun refreshList()
}
