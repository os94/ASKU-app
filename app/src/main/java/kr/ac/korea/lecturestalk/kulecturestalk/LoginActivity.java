package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText edittext_login_id = (EditText) findViewById(R.id.edittext_login_id);
        final EditText edittext_password = (EditText) findViewById(R.id.edittext_password);

        mFirebaseAuth = FirebaseAuth.getInstance();

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = edittext_login_id.getText().toString().trim();
                String passwordText = edittext_password.getText().toString().trim();
                mFirebaseAuth.signInWithEmailAndPassword(idText, passwordText)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        String msg = e.getMessage().trim();
                                        if ("The email address is badly formatted.".equals(msg)) {
                                            Toast.makeText(LoginActivity.this, R.string.fail_login_email_format, Toast.LENGTH_SHORT).show();
                                        } else if ("The password is invalid or the user does not have a password.".equals(msg)) {
                                            Toast.makeText(LoginActivity.this, R.string.fail_login_invalid, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e(TAG, "FirebaseAuthInvalidCredentialsException occured");
                                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        //There is no user record corresponding to this identifier. The user may have been deleted.
                                        Toast.makeText(LoginActivity.this, R.string.fail_login_id_does_not_exist, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }


                        });
            }
        });

        Button signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }
}
