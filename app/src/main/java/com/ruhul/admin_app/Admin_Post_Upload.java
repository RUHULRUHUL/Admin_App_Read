package com.ruhul.admin_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ruhul.admin_app.model.StoryModel;
import com.ruhul.admin_app.model.StoryName;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Admin_Post_Upload extends AppCompatActivity {

    private LinearLayout image_chose_lay;
    private ImageView showImage;


    private EditText edt_story_name,edt_full_story_name,edt_part_number;
    private AppCompatSpinner spinner_story_name,spinner_part_number;
    private String spinner_story_sellect_name;
    private String firebase_story__name;
    private Button upload;
    private static final int IMAGE_CHOSE_CODE =10 ;


    private String part;


    private List<String> story_name;
    private List<String> story_part=new ArrayList<>();
    private List<StoryModel> storyModelList;

    private String storyname;
    private String partnumber;
    private String upload_story_name;
    private String imageuri;


    private ArrayAdapter<StoryModel> spiner_storyname_adapter;


    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rotref;


    private String parent_story_node="all_post";
    private String child_story_name_node="";
    private String child_story_part_node="part";

    private String STORY_NAME_NODE="all_story_name";


    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__upload);


        firebaseDatabase= FirebaseDatabase.getInstance();
        rotref=firebaseDatabase.getReference();


        storyModelList=new ArrayList<>();
        progressDialog=new ProgressDialog(Admin_Post_Upload.this);


        image_chose_lay=findViewById(R.id.linear_image_attach_id);
        showImage=findViewById(R.id.imageview_id);



        edt_story_name=findViewById(R.id.edt_story_name_id);
        edt_part_number=findViewById(R.id.edt_story_part_id);
        edt_full_story_name=findViewById(R.id.edt_fullstory_id);

        spinner_part_number=findViewById(R.id.spinner_part_id);
        spinner_story_name=findViewById(R.id.spinner_story_chose_id);


        upload=findViewById(R.id.btn_upload_id);



        image_chose_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(Admin_Post_Upload.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                choseimagefile();

                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response)
                            {

                            }
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                            {
                                token.continuePermissionRequest();

                            }
                        }).check();
            }
        });




        spinner_adapter_set_part();
        //spinner_adapter_set_part_name();


        spinner_part_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                try {

                    part=parent.getItemAtPosition(position).toString();
                    edt_part_number.setText(part);

                }catch (Exception e)
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinner_story_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                try {

                    spinner_story_sellect_name = parent.getItemAtPosition(position).toString();
                    edt_story_name.setText(spinner_story_sellect_name);


                }catch (Exception e)
                {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (TextUtils.isEmpty(edt_story_name.getText().toString()))
                {
                    edt_story_name.setError("input story name..");
                }
                else if (TextUtils.isEmpty(edt_full_story_name.getText().toString()))
                {
                    edt_full_story_name.setError("input full story");
                }
                else if (TextUtils.isEmpty(edt_part_number.getText().toString()))
                {
                    Toast.makeText(Admin_Post_Upload.this, "you have no select part", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressDialog.setTitle("post uploading");
                    progressDialog.setMessage("please wait,for uploading fineshed");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Date date=new Date(System.currentTimeMillis());
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy : hh:mm:ss aa", Locale.getDefault());


                    String curenttime=simpleDateFormat.format(date);
                    child_story_name_node=edt_story_name.getText().toString().trim();
                    partnumber=edt_part_number.getText().toString();
                    String story=edt_full_story_name.getText().toString();


                    StoryModel storyModel =new StoryModel(curenttime,child_story_name_node,story,partnumber,imageuri);


                    //check data from firebase value exist -------
                    rotref.child(parent_story_node).child(child_story_name_node)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {


                                        if (snapshot.child(partnumber).exists())
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(Admin_Post_Upload.this, "part already exist: ", Toast.LENGTH_SHORT).show();

                                        }

                                       else {

                                           rotref.child(parent_story_node).child(child_story_name_node).child(partnumber).setValue(storyModel)
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful())
                                                           {
                                                               progressDialog.dismiss();

                                                           }

                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   progressDialog.dismiss();
                                                   Toast.makeText(Admin_Post_Upload.this, "error: "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                               }
                                           });

                                       }
                                    }
                                    else {

                                        rotref.child(parent_story_node).child(child_story_name_node).child(partnumber).setValue(storyModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(Admin_Post_Upload.this, "upload successfully..", Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Log.d("error: ",e.getMessage());
                                            }
                                        });


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    progressDialog.dismiss();
                                    Toast.makeText(Admin_Post_Upload.this, "error: "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                }
                            });


                }
            }
        });





    }

    private void choseimagefile() {

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_CHOSE_CODE);

    }


    private void spinner_adapter_set_part()
    {
        story_part.add("01");
        story_part.add("02");
        story_part.add("03");
        story_part.add("04");
        story_part.add("05");
        story_part.add("06");
        story_part.add("07");
        story_part.add("07");
        story_part.add("08");
        story_part.add("09");
        story_part.add("10");
        story_part.add("11");
        story_part.add("12");
        story_part.add("13");
        story_part.add("14");
        story_part.add("15");
        story_part.add("16");
        story_part.add("17");
        story_part.add("18");
        story_part.add("19");
        story_part.add("20");
        story_part.add("21");
        story_part.add("22");
        ArrayAdapter<String> story_name_list=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,story_part);
        spinner_part_number.setAdapter(story_name_list);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK)
        {
            if (requestCode==IMAGE_CHOSE_CODE)
            {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }

        }
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                try {
                    showImage.setImageURI(resultUri);


                    Date date=new Date(System.currentTimeMillis());
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY hh:mm:ss a");

                    String datetime=simpleDateFormat.format(date);


                    StorageReference upload=FirebaseStorage.getInstance().getReference().child("img-"+String.valueOf(datetime)+".jpg");

                    if (resultUri!=null)
                    {
                        progressDialog.setTitle("image upload");
                        progressDialog.setMessage("please wait uploading ....");
                        progressDialog.show();
                        upload.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        imageuri=uri.toString();
                                        Toast.makeText(Admin_Post_Upload.this, "upload successfully..", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                });

                            }
                        });


                    }
                    else {
                        Toast.makeText(this, "image has been empty..", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }catch (Exception e)
                {
                    progressDialog.dismiss();

                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "error: ", Toast.LENGTH_SHORT).show();
            }
        }

    }
}