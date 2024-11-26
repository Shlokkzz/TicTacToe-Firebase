package androidsamples.java.tictactoe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth auth;
    private EditText email,password;
    private DatabaseReference userRef;
    private ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO if a user is logged in, go to Dashboard
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance("https://tictactoe-e88dd-default-rtdb.firebaseio.com/").getReference("users");

        if(auth.getCurrentUser()!=null){
            NavHostFragment.findNavController(this).navigate(R.id.action_login_successful);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.edit_email);
        password = view.findViewById(R.id.edit_password);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setTitle("Authentication");

        view.findViewById(R.id.btn_log_in)
                .setOnClickListener(v -> {
                    // TODO implement sign in logic
                    pd.show();

                    // empty details
                    if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), "Please enter email and password to register/login", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        return;
                    }
                    Log.i(TAG,"Trying to enter");
                    auth.fetchSignInMethodsForEmail(email.getText().toString())
                                    .addOnCompleteListener(task->{
                                        if(task.isSuccessful()){
                                            List<String> signInMethods = task.getResult().getSignInMethods();
                                            // If the email already exists (i.e., there's at least one sign-in method)
                                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                                // Proceed with login
                                                Log.i(TAG, signInMethods.get(0));
                                                Log.i(TAG,"Entered");
                                                login(email.getText().toString(), password.getText().toString());
                                            }
                                            else{
                                                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                                        .addOnCompleteListener(task2->{
                                                            if(!task2.isSuccessful()){

                                                                Log.i(TAG,"Entered2");
                                                                login(email.getText().toString(), password.getText().toString());

                                                            }
                                                            else{
                                                                Toast.makeText(getContext(), "User Registered", Toast.LENGTH_SHORT).show();
                                                                NavHostFragment.findNavController(this).navigate(R.id.action_login_successful);
                                                                userRef.child(task2.getResult().getUser().getUid()).child("won").setValue(0);
                                                                userRef.child(task2.getResult().getUser().getUid()).child("draw").setValue(0);
                                                                userRef.child(task2.getResult().getUser().getUid()).child("lost").setValue(0);
                                                            }
                                                            pd.dismiss();
                                                        });
                                            }
                                        }
                                        else {
                                            // Handle error if fetchSignInMethodsForEmail fails
                                            Toast.makeText(getContext(), "Error checking email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                });

        return view;
    }
    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("LOGIN", "SUCCESS");
                        Log.i("User logged in", Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());

                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_login_successful);
                    } else {
                        Log.i("LOGIN", "FAIL");
                        Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                });
    }

    // No options menu in login fragment.
}