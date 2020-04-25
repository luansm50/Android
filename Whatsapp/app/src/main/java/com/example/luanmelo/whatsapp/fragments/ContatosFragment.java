package com.example.luanmelo.whatsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.activity.ChatActivity;
import com.example.luanmelo.whatsapp.activity.GrupoActivity;
import com.example.luanmelo.whatsapp.adapter.ContatosAdapter;
import com.example.luanmelo.whatsapp.adapter.ConversasAdapter;
import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.helper.RecyclerItemClickListener;
import com.example.luanmelo.whatsapp.helper.UsuarioFirebase;
import com.example.luanmelo.whatsapp.model.Conversa;
import com.example.luanmelo.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter contatosAdapter;
    private List<Usuario> listaContatos = new ArrayList<>();
    private FirebaseUser usuarioAtual;
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);

        usuariosRef = ConfiguracaoFirebase.getDatabaseReference().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //configurar adapter
        contatosAdapter = new ContatosAdapter(getContext(), listaContatos);
        //configurar recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.setAdapter(contatosAdapter);
        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                    getActivity(),
                    recyclerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }

                            @Override
                            public void onItemClick(View view, int position) {
                                List<Usuario> listaUsuarioAtualizada = contatosAdapter.getContatos();

                                Usuario usuarioSelecionado = listaUsuarioAtualizada.get(position);
                                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();

                                if(cabecalho)
                                {
                                    Intent i = new Intent(getActivity(), GrupoActivity.class);
                                    i.putExtra("chatContato", usuarioSelecionado);
                                    startActivity(i);
                                }
                                else
                                {
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato", usuarioSelecionado);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }
        ));

        adicionarMenuNovoGrupo();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void adicionarMenuNovoGrupo()
    {
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);
    }

    public void recuperarContatos() {



        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaContatos.clear();
                adicionarMenuNovoGrupo();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    if (!emailUsuarioAtual.equals(usuario.getEmail()))
                        listaContatos.add(usuario);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void pesquisarContatos(String texto)
    {
        List<Usuario> listaContatosBusca = new ArrayList<>();
        for(Usuario usuario : listaContatos)
        {
            String nome = usuario.getNome().toUpperCase();

            if(nome.contains(nome))
            {
                listaContatosBusca.add(usuario);
            }
        }

        contatosAdapter = new ContatosAdapter(getActivity(), listaContatosBusca);
        recyclerViewListaContatos.setAdapter(contatosAdapter);
        contatosAdapter.notifyDataSetChanged();
    }

    public void recarregarContatos()
    {
        contatosAdapter =new ContatosAdapter(getActivity(), listaContatos);
        recyclerViewListaContatos.setAdapter(contatosAdapter);
        contatosAdapter.notifyDataSetChanged();
    }

}
