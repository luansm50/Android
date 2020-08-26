package com.example.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activity.EditarPerfilActivity;
import com.example.instagram.adapter.AdapterGrid;
import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    public GridView gridViewPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private Button buttonAcaoPerfil;

    private String idUsuarioLogado;
    private Usuario usuarioLogado;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference postagensUsuarioRef;
    private ValueEventListener valueEventListenerUsuarioLogado;
    private AdapterGrid adapterGrid;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //configuracoes iniciais;
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        usuariosRef = firebaseRef.child("usuarios");

        //Configuraçoes dos componentes;
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBar = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagePerfilPostagem);
        textPublicacoes = view.findViewById(R.id.textPublicacoes);
        textSeguidores = view.findViewById(R.id.textSeguidores);
        textSeguindo = view.findViewById(R.id.textSeguindo);
        buttonAcaoPerfil = view.findViewById(R.id.buttonAcaoPerfil);

        //Abre ediçao de perfil
        buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });

        //configurar referencia postagens usuario
        postagensUsuarioRef = ConfiguracaoFirabese.getReferenceFirabese()
                .child("postagens")
                .child(idUsuarioLogado);

        inicializarImageLoader();
        carregarFotosPostagem();

        return view;
    }

    public void inicializarImageLoader()
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void carregarFotosPostagem()
    {
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid/3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlFotos = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Postagem postagem = dataSnapshot.getValue(Postagem.class);
                    urlFotos.add(postagem.getCaminhoFoto());
                }
//                int qtdPostagem = urlFotos.size();
//                textPublicacoes.setText(String.valueOf(qtdPostagem));

                //Configurar Adapter
                adapterGrid = new AdapterGrid(getActivity(), R.layout.grid_postagem, urlFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuarioLogado();
        recuperarFotoUsuarioLogado();

    }

    public void recuperarFotoUsuarioLogado()
    {
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Recuperar fotos do usuario
        String caminhoFoto = usuarioLogado.getCaminhoFoto();
        if (caminhoFoto == null || caminhoFoto.isEmpty())
        {
            imagePerfil.setImageResource(R.drawable.avatar);
        }
        else
        {
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(getActivity())
                    .load(url)
                    .into(imagePerfil);
        }
    }



    public void recuperarDadosUsuarioLogado()
    {
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        valueEventListenerUsuarioLogado = usuarioLogadoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        String postagens = String.valueOf(usuario.getPostagens());
                        String seguindo  = String.valueOf(usuario.getSeguindo());
                        String seguidores= String.valueOf(usuario.getSeguidores());

                        textPublicacoes.setText(postagens);
                        textSeguidores.setText(seguidores);
                        textSeguindo.setText(seguindo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}