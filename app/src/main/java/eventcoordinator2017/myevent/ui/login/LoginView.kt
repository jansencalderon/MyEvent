package eventcoordinator2017.myevent.ui.login

import com.hannesdorfmann.mosby.mvp.MvpView

/**
 * Created by Cholo Mia on 12/3/2016.
 */

interface LoginView : MvpView {

    fun onLoginButtonClicked()

    fun onRegisterButtonClicked()

    fun showAlert(message: String)

    fun setEditTextValue(username: String, password: String)

    fun startLoading()

    fun stopLoading()

    fun onLoginSuccess()


    fun onForgotPasswordButtonClicked()
}
