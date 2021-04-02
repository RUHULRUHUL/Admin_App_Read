package com.ruhul.services;

import android.app.NotificationManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ruhul.admin_app.R;
import com.ruhul.admin_app.model.Token_Model;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


//    private DatabaseReference rotref;
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//
//
//        //update token server
//
//        Token_Model token_model=new Token_Model(token);
//
//        rotref= FirebaseDatabase.getInstance().getReference();
//
//        rotref.child("notification_token").setValue(token_model).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if (task.isSuccessful())
//                {
//                    Toast.makeText(MyFirebaseMessagingService.this, "successfully token update", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//
//
//
//
//
//    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        shownotification(title,body);

    }

    void shownotification(String title,String message)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"mynotification");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.adminbg);
        builder.setAutoCancel(false);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(999,builder.build());

    }
}
