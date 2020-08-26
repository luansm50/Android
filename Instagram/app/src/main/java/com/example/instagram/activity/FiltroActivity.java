package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.adapter.AdapterMiniaturas;
import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.helper.RecyclerItemClickListener;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FiltroActivity extends AppCompatActivity {

    private ImageView imageFotoScolhida;
    private RecyclerView recyclerFiltros;
    private TextInputEditText textDescricaoFiltro;

    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private List<ThumbnailItem> listaFiltros;

    private AdapterMiniaturas adapterMiniaturas;

    private String idUsuarioLogado;
    private Usuario usuarioLogado;

    private AlertDialog dialog;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;

    private DataSnapshot seguidoresSnapshot;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        //configuracoes inciciais
        listaFiltros = new ArrayList<>();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();


        imageFotoScolhida = findViewById(R.id.imageFotoEscolhida);
        textDescricaoFiltro = findViewById(R.id.textDescricaoFiltro);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);

        firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        usuariosRef = firebaseRef.child("usuarios");

        recuperarDadosPostagem();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imagemFiltro = imagem.copy(imagem.getConfig(), true);
            imageFotoScolhida.setImageBitmap(imagem);

            //Configura recyclerView de filtros
            adapterMiniaturas = new AdapterMiniaturas(listaFiltros, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            recyclerFiltros.setLayoutManager(layoutManager);
            recyclerFiltros.setAdapter(adapterMiniaturas);

            //Adiciona evento de clique no recycler
            recyclerFiltros.addOnItemTouchListener(
                    new RecyclerItemClickListener(
                            getApplicationContext(),
                            recyclerFiltros,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    ThumbnailItem item = listaFiltros.get(position);

                                    imagemFiltro = imagem.copy(imagem.getConfig(), true);
                                    Filter filter = item.filter;
                                    imageFotoScolhida.setImageBitmap(filter.processFilter(imagemFiltro));

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            }
                    )
            );

            //recuperaListaFiltros
            recuperarFiltro();

        }
    }

    private void recuperarFiltro() {
        listaFiltros.clear();
        ThumbnailsManager.clearThumbs();

        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

        for (Filter filter : filters) {
            ThumbnailItem itemFilter = new ThumbnailItem();
            itemFilter.image = imagem;
            itemFilter.filter = filter;
            itemFilter.filterName = filter.getName();

            ThumbnailsManager.addThumb(itemFilter);
        }

        listaFiltros.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturas.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_salvar_postagem:
                publicarPostagem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void publicarPostagem() {
        abrirDialogCarregamento("Salvando postagem!");
        final Postagem postagem = new Postagem();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setDescricao(textDescricaoFiltro.getText().toString());

        //Recuperar dados da imagem para o firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        StorageReference storageReference = ConfiguracaoFirabese.getStorageReference();
        final StorageReference imagemRef = storageReference
                .child("imagens")
                .child("postagens")
                .child(postagem.getId() + ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FiltroActivity.this, "Erro ao salvar imagem, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Recuperar local da foto
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        postagem.setCaminhoFoto(url.toString());

                        //atualizar quantidade de postagem
                        int qtdPostagens = usuarioLogado.getPostagens() + 1;
                        usuarioLogado.setPostagens(qtdPostagens);
                        usuarioLogado.atualizarQtPostagens();

                        if (postagem.salvar(seguidoresSnapshot)) {

                            Toast.makeText(FiltroActivity.this, "Sucesso ao salvar postagem!", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void abrirDialogCarregamento(String titulo)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setCancelable(false);
        alert.setView(R.layout.carregamento);

        dialog = alert.create();
        dialog.show();
    }

    private void recuperarDadosPostagem() {
        abrirDialogCarregamento("Carregando dados, aguarde!");
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuarioLogado = dataSnapshot.getValue(Usuario.class);

                        DatabaseReference seguidoresRef = firebaseRef
                                .child("seguidores")
                                .child(idUsuarioLogado);
                        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                seguidoresSnapshot = dataSnapshot;
                                dialog.cancel();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}