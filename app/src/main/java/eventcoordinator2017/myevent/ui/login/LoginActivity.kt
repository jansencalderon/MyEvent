package eventcoordinator2017.myevent.ui.login

import android.app.ProgressDialog
import android.content.Intent

import androidx.databinding.DataBindingUtil

import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState

import eventcoordinator2017.myevent.R
import eventcoordinator2017.myevent.databinding.ActivityLoginBinding
import eventcoordinator2017.myevent.model.data.User
import eventcoordinator2017.myevent.ui.forgot.ForgotPasswordActivity
import eventcoordinator2017.myevent.ui.main.MainActivity
import eventcoordinator2017.myevent.ui.register.RegisterActivity
import io.realm.Realm

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : MvpViewStateActivity<LoginView, LoginPresenter>(), LoginView {


    // UI references.
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        isRetainInstance = true
        realm = Realm.getDefaultInstance()
        //
        val user = realm.where(User::class.java).findFirst()
        if (user != null) {
            onLoginSuccess()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.view = mvpView


        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Logging in...")

    }


    /***
     * Start of LoginView
     */

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    /***
     * Start of MvpViewStateActivity
     */

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun createViewState(): ViewState<LoginView> {
        isRetainInstance = true
        return LoginViewState()
    }

    /***
     * End of MvpViewStateActivity
     */

    override fun onNewViewStateInstance() {
        saveValues()
    }

    override fun onLoginButtonClicked() {
        presenter.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
        )
    }

    override fun onRegisterButtonClicked() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun showAlert(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setEditTextValue(username: String, password: String) {
        binding.etEmail.setText(username)
        binding.etPassword.setText(password)
    }

    override fun startLoading() {
        progressDialog.show()
    }

    override fun stopLoading() {
        progressDialog.dismiss()
    }

    override fun onLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onForgotPasswordButtonClicked() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    /***
     * End of LoginView
     */

    private fun saveValues() {
        val loginViewState = getViewState() as LoginViewState
        loginViewState.setUsername(binding.etEmail.text.toString())
        loginViewState.setPassword(binding.etPassword.text.toString())
    }
}

