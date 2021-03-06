package com.example.instagram.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirabese
{
    private static DatabaseReference referenceFirabese;
    private static FirebaseAuth referenciaAutencicao;
    private static StorageReference storageReference;

    public static FirebaseAuth getReferenciaAutencicao()
    {
        if(referenciaAutencicao == null)
        {
            referenciaAutencicao = FirebaseAuth.getInstance();
        }
        return referenciaAutencicao;
    }

    public static DatabaseReference getReferenceFirabese()
    {
        if(referenceFirabese == null)
        {
            referenceFirabese = FirebaseDatabase.getInstance().getReference();
        }
        return referenceFirabese;
    }

    public static StorageReference getStorageReference()
    {
        if(storageReference == null)
        {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }
}
