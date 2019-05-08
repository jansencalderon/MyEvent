package eventcoordinator2017.myevent.ui.login

import android.os.Bundle

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState

import eventcoordinator2017.myevent.app.Constants


class LoginViewState : RestorableViewState<LoginView> {
    private var email: String? = null
    private var password: String? = null

    override fun saveInstanceState(out: Bundle) {
        out.putString(Constants.EMAIL, email)
        out.putString(Constants.PASSWORD, password)
    }

    override fun restoreInstanceState(`in`: Bundle): RestorableViewState<LoginView> {
        email = `in`.getString(Constants.EMAIL, "")
        password = `in`.getString(Constants.PASSWORD, "")
        return this
    }

    override fun apply(view: LoginView, retained: Boolean) {
        view.setEditTextValue(email!!, password!!)
    }

    fun setUsername(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
