package com.example.luanmelo.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.adapter.MensagensAdapter;
import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.helper.Base64Custom;
import com.example.luanmelo.whatsapp.helper.UsuarioFirebase;
import com.example.luanmelo.whatsapp.model.Conversa;
import com.example.luanmelo.whatsapp.model.Grupo;
import com.example.luanmelo.whatsapp.model.Mensagem;
import com.example.luanmelo.whatsapp.model.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private static final int SELECAO_CAMERA = 100;

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private EditText editMensagem;
    private ImageView imageCamera;

    private Usuario usuarioDestinario;
    private Grupo grupo;

    //identificador usuario remetente e destinatario;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private Usuario usuarioRemetente;

    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;

    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurar Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);

        //Recuperar dados usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdUsuario();
        usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();

        //Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            if(bundle.containsKey("chatGrupo"))
            {
                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                textViewNome.setText(grupo.getName());

                idUsuarioDestinatario = grupo.getId();

                String foto = grupo.getFoto();
                if(foto != null)
                {
                    Uri uri = Uri.parse(foto);
                    Glide.with(ChatActivity.this)
                            .load(uri)
                            .into(circleImageViewFoto);
                }else
                {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }
            }
            else
            {
                /*********/
                usuarioDestinario = (Usuario) bundle.getSerializable("chatContato");
                textViewNome.setText(usuarioDestinario.getNome());

                String foto = usuarioDestinario.getFoto();
                if(foto != null)
                {
                    Uri uri = Uri.parse(foto);
                    Glide.with(ChatActivity.this)
                            .load(uri)
                            .into(circleImageViewFoto);
                }else
                {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                //recuperar dados usuario destinatario
                idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinario.getEmail());

                /*********/
            }
        }

        //configurar Adaptaer
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        database = ConfiguracaoFirebase.getDatabaseReference();
        storageReference = ConfiguracaoFirebase.getStorageReference();



        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if (imagem != null) {
                    //Recuperar dados da imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    //Salvar imagem no Firebase
                    String nomeImagem = UUID.randomUUID().toString();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    Task<Uri> urlTask = uploadTask./*addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this,
                                    "Erro ao fazer uploado da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).*/continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                if(usuarioDestinario != null)
                                {
                                    String url = task.getResult().toString();
                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario(idUsuarioRemetente);
                                    mensagem.setMensagem("imagem.jpeg");
                                    mensagem.setImagem(url);

                                    salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                                    salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                                    salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinario, mensagem, false);

                                    salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);
                                }
                                else
                                {
                                    String url = task.getResult().toString();
                                    for(Usuario membro : grupo.getMembros())
                                    {
                                        String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                                        String idUsuarioLogadoGrupo = UsuarioFirebase.getIdUsuario();

                                        Mensagem mensagem = new Mensagem();
                                        mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                                        mensagem.setMensagem("imagem.jpeg");
                                        mensagem.setImagem(url);
                                        mensagem.setNomeUsuario(usuarioRemetente.getNome());

                                        salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                                        salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinario,  mensagem, true);
                                    }
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMensagem(View view)
    {
        String textoMensagem = editMensagem.getText().toString();
        
        if(!textoMensagem.isEmpty())
        {
            if(usuarioDestinario != null)
            {
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                //salvar mensagem para o remetente
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                //salvar conversa remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinario, mensagem, false);

                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);
            }
            else
            {
                Usuario usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();

                for(Usuario membro : grupo.getMembros())
                {
                    String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNomeUsuario(usuarioRemetente.getNome());

                    salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem);

                    salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinario,  mensagem, true);
                }
            }
        }
        else
        {
            Toast.makeText(ChatActivity.this,
                    "Digite uma mensagem para enviar",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg)
    {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente).child(idDestinatario)
                .push()
                .setValue(msg);

        editMensagem.setText("");
    }

    private void salvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem msg, Boolean isGroup)
    {
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());

        if(isGroup)
        {
            conversaRemetente.setGrupo(grupo);
            conversaRemetente.setIsGroup("true");
        }
        else
        {
            conversaRemetente.setUsuarioExibicao(usuarioExibicao);
        }

        conversaRemetente.salvar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagem()
    {
        mensagens.clear();

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
