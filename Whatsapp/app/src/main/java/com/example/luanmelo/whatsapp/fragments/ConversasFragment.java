package com.example.luanmelo.whatsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.activity.ChatActivity;
import com.example.luanmelo.whatsapp.adapter.ContatosAdapter;
import com.example.luanmelo.whatsapp.adapter.ConversasAdapter;
import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.helper.RecyclerItemClickListener;
import com.example.luanmelo.whatsapp.helper.UsuarioFirebase;
import com.example.luanmelo.whatsapp.model.Conversa;
import com.example.luanmelo.whatsapp.model.Grupo;
import com.example.luanmelo.whatsapp.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversa;
    private ConversasAdapter conversasAdapter;

    private List<Conversa> listaConversas = new ArrayList<>();

    private DatabaseReference database;

    private DatabaseReference conversasRef;

    private String identificadorUsuario;

    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_conversas, container, false);

        identificadorUsuario = UsuarioFirebase.getIdUsuario();

        recyclerViewConversa = view.findViewById(R.id.idRecyclerListaConversa);

        //configurar Adap
        conversasAdapter = new ConversasAdapter(listaConversas, getActivity());
        //configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversa.setLayoutManager(layoutManager);
        recyclerViewConversa.setHasFixedSize(true);
        recyclerViewConversa.setAdapter(conversasAdapter);

        recyclerViewConversa.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewConversa,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Conversa> listConversaAtualizada = conversasAdapter.getConversas();
                                Conversa conversaSelecionada = listConversaAtualizada.get(position);

                                if(conversaSelecionada.getIsGroup().equals("true"))
                                {
                                    Grupo grupo = listaConversas.get(position).getGrupo();
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatGrupo", grupo);
                                    startActivity(i);
                                }
                                else
                                {
                                    Usuario usuario = listaConversas.get(position).getUsuarioExibicao();
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato", usuario);
                                    startActivity(i);
                                }

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



        database = ConfiguracaoFirebase.getDatabaseReference();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto)
    {
        List<Conversa> listaConversasBusca = new ArrayList<>();
        for(Conversa conversa : listaConversas)
        {
            if(conversa.getUsuarioExibicao() != null)
            {
                String nome = conversa.getUsuarioExibicao().getNome().toUpperCase();
                String ultimaMsg = conversa.getUltimaMensagem().toUpperCase();

                if (nome.contains(texto) || texto.contains(ultimaMsg)) {
                    listaConversasBusca.add(conversa);
                }
            }
            else
            {
                String nome = conversa.getGrupo().getName().toUpperCase();
                String ultimaMsg = conversa.getUltimaMensagem().toUpperCase();

                if (nome.contains(texto) || texto.contains(ultimaMsg)) {
                    listaConversasBusca.add(conversa);
                }
            }



        }

        conversasAdapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversa.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();
    }

    public void recarregarConversas()
    {
        conversasAdapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversa.setAdapter(conversasAdapter);
        conversasAdapter.notifyDataSetChanged();
    }

    public void recuperarConversas()
    {
        listaConversas.clear();

        conversasRef = database.child("conversas").child(identificadorUsuario);
        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                conversasAdapter.notifyDataSetChanged();
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
