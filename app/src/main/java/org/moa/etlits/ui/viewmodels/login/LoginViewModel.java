package org.moa.etlits.ui.viewmodels.login;

import android.util.Log;

import org.moa.etlits.R;
import org.moa.etlits.api.RetrofitUtil;
import org.moa.etlits.api.response.AuthResponse;
import org.moa.etlits.api.services.AuthService;
import org.moa.etlits.data.repositories.LoginRepository;
import org.moa.etlits.ui.validation.LoginFormState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;


    private AuthService authService;
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        RetrofitUtil.clearCookies();
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        if (authService == null) {
            authService = RetrofitUtil.createAuthService();
        }

        RetrofitUtil.clearCookies();
        Call<AuthResponse> call = authService.login(Credentials.basic(username, password));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = (AuthResponse) response.body();
                    loginResult.postValue(new LoginResult(username, password, authResponse.getRoles(), LoginResult.LoginStatus.SUCCESS));
                } else {
                    loginResult.postValue(new LoginResult(response.code(),LoginResult.LoginStatus.FAIL));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                loginResult.postValue(new LoginResult(500, LoginResult.LoginStatus.FAIL));
            }
        });
    }

    public void logout() {
        loginResult.setValue(null);
        RetrofitUtil.clearCookies();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        //return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        return true;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 0;
    }
}