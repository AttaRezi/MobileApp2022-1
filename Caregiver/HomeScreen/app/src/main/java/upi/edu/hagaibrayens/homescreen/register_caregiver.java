package upi.edu.hagaibrayens.homescreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class register_caregiver extends AppCompatActivity {

    EditText Nama,Mail,Pass,Telp,Alamat,Tgl;
    Button submit;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cargiver);

        db = FirebaseFirestore.getInstance();

        Nama=findViewById(R.id.Nama);
        Mail=findViewById(R.id.Mail);
        Pass=findViewById(R.id.Pass);
        Alamat=findViewById(R.id.Alamat);
        Telp =findViewById(R.id.Telp);
        Tgl=findViewById(R.id.Tgl);

        submit=findViewById(R.id.submit);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(register_caregiver.this);
        progressDialog.setTitle("loading");
        progressDialog.setMessage("progres sedang berjalan");
        progressDialog.setCancelable(false);



        submit.setOnClickListener( v -> {
//            @Override
//            public void onClick(View view) {
//                final String name = Nama.getText().toString().trim();
                String username= Mail.getText().toString();
                String password= Pass.getText().toString();
//                String hp= Telp.getText().toString();
//                String alamat= Alamat.getText().toString();
//                String tanggal= Tgl.getText().toString();
//
//                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(username)||TextUtils.isEmpty(password)
//                        ||TextUtils.isEmpty(hp)||TextUtils.isEmpty(alamat)||TextUtils.isEmpty(tanggal))
//                    Toast.makeText(register_caregiver.this, "Cek Kembali",Toast.LENGTH_SHORT).show();
//                else{
//                    Boolean checkuser= sqLiteDatabase.checkmail(user);
//                    if(checkuser==true){
//                        Boolean insert = sqLiteDatabase.insertData(user,password,username,hp,alamat,tanggal);
//                        if (insert==true) {
//                            Toast.makeText(register_caregiver.this, "register Berhasil", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), LoginCaregiver.class);
//                            startActivity(intent);
//                        }else {
//                            Toast.makeText(register_caregiver.this, "register gagal", Toast.LENGTH_SHORT).show();
//                        }
//                    }else {
//                        Toast.makeText(register_caregiver.this, "akun sudah ada", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }

            if(username.length()>0&&password.length()>0){
                register();
            }
            else{
                Toast.makeText(register_caregiver.this, "Cek Kembali",Toast.LENGTH_SHORT).show();
            }

        });



    }

    private void register() {

        final String name = Nama.getText().toString().trim();
        String username= Mail.getText().toString();
        String password= Pass.getText().toString();
        String hp= Telp.getText().toString();
        String alamat= Alamat.getText().toString();
        String tanggal= Tgl.getText().toString();


        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if(firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        Map<String,Object> user = new HashMap<>();
                        user.put("nama", name);
                        user.put("username", username);
                        user.put("hp", hp);
                        user.put("alamat", alamat);
                        user.put("tanggal", tanggal);


                        db.collection("user").document(firebaseUser.getUid()).set(user);


                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Register gagal", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void login(View v){
        Intent switchActivityIntent = new Intent(this, LoginCaregiver.class);
        startActivity(switchActivityIntent);
    }

}