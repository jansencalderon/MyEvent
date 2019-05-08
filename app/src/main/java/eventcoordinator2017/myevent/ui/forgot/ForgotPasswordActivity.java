package eventcoordinator2017.myevent.ui.forgot;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import eventcoordinator2017.myevent.R;
import eventcoordinator2017.myevent.databinding.ActivityForgotPasswordBinding;


/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class ForgotPasswordActivity extends MvpActivity<ForgotView, ForgotPresenter> implements ForgotView {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.setView(getMvpView());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        //add text listener && disable submit button
        binding.email.addTextChangedListener(codeWatcher);
        binding.submit.setEnabled(false);
        binding.submit.setAlpha(.5f);

    }

    private final TextWatcher codeWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                //enable submit button
                binding.submit.setEnabled(true);
                binding.submit.setAlpha(1f);
            } else {
                //disable submit button
                binding.submit.setEnabled(false);
                binding.submit.setAlpha(.5f);
            }
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                //enable submit button
                binding.submit.setEnabled(true);
                binding.submit.setAlpha(1f);
            } else {
                //disable submit button
                binding.submit.setEnabled(false);
                binding.submit.setAlpha(.5f);
            }
        }
    };

    //start of view
    @Override
    public void OnButtonSubmit(){

    }
    //end of view

    @NonNull
    @Override
    public ForgotPresenter createPresenter() {
        return new ForgotPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
