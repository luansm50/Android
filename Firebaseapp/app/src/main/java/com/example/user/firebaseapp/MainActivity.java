package com.example.user.firebaseapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    /*private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();*/


    private ImageView imageFoto;
    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFoto = findViewById(R.id.imageFoto);
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Configura para imagem ser salva em memoria
                imageFoto.setDrawingCacheEnabled(true);
                imageFoto.buildDrawingCache();

                //Recupera bitmap da imagem (image a ser carregada
                Bitmap bitmap = imageFoto.getDrawingCache();

                //Comprimo bitmpap para um formato png/jpg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //converte o baos para pixe burtos em uma matrziz de bytes
                //dados da imagem
                byte[] dadosImagem = baos.toByteArray();

                //Define nos para o storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");
                final StorageReference imagemRef = imagens.child("celular.jpeg");

               // G
                    GlideApp.with(MainActivity.this).load(imagemRef).into(imageFoto);
                /*imagemRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "erro", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                    }
                });*/

                //Retonra objeto que ira controlar o upload
                 //UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                /*Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imagemRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Toast.makeText(MainActivity.this, downloadUri.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });*/




            }
        });









        //DatabaseReference usuarios = databaseReference.child("usuarios");
        //DatabaseReference usuarioPesquisa = usuarios.child("-LYld90Knzb4eFEfoFTI");

        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Luan");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(3);
        //Query usuarioPesquisa = usuarios.orderByKey().limitToLast(3);

        /*Maior ou igual*/
        //]==Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(35);

        /*menor ou igual*/
       // Query usuarioPesquisa = usuarios.orderByChild("idade").endAt(35);

        /*Entre dois valores*/
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(20).endAt(35);

        /*Filtrar palavras*/
        /*Query usuarioPesquisa = usuarios.orderByChild("nome").startAt("m").endAt("m" + "\uf8ff");

        usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                //Log.i("DADOS usuario", "nome: " + dadosUsuario.getNome());

                Log.i("DADOS usuario", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




        /*Usuario usuario = new Usuario();
        usuario.setIdade(35);
        usuario.setNome("Pedro");
        usuario.setSobrenome("Alves");

        usuarios.push().setValue(usuario);*/




        //deslogar
        //usuario.signOut();

        //logar usuario
       /* usuario.signInWithEmailAndPassword("luan.sm50@gmail.com", "luan1995")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("signIn", "Sucesso ao logar usuário");
                }else{
                    Log.i("signIn", "Erro ao logar usuário");
                }
            }
        });*/

        //verifica usuario logado
        /*if (usuario.getCurrentUser() != null) {
            Log.i("CreateUser", "Usuário Logado");
        }else{
            Log.i("CreateUser", "Usuário não está Logado");
        }*/

        /*CADASTRO DE USUARIO
        usuario.createUserWithEmailAndPassword("luan.sm502@gmail.com", "luan1995")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("CreateUser", "Sucesso ao cadastrar usuário");
                        }else{
                            Log.i("CreateUser", "Erro ao cadastrar usuário");
                        }
                    }
                });*/





         /*
        DatabaseReference usuarios = databaseReference.child("usuarios");

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("FIREBASE", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       DatabaseReference produtos = databaseReference.child("produtos");

        Usuario usuario = new Usuario();
        usuario.setIdade(20);
        usuario.setNome("Luan");
        usuario.setSobrenome("Melo");

        usuarios.child("001").setValue(usuario);

        Produto produto = new Produto();
        produto.setDescricao("Tables Samsung 32GB 9.5 Modelo 25AKSLO");
        produto.setMarca("Samsung");
        produto.setPreco(new Float(2.5));

        produtos.child("001").setValue(produto);*/


    }
}
