package com.example.communitygrocerystoreapp.SendNotificationPack;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    if(firebaseUser!=null){
                        updateToken(token);
                    }
                }
            }
        });

    }
    private void updateToken(String refreshToken){
        Token token1= new Token(refreshToken);
        Map<String, String> ss= new HashMap<>();
        ss.put("token", token1.toString());
        FirebaseFirestore.getInstance().collection("Token").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(ss);
    }
}
