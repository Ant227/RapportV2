package com.example.rapportv2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    private CircleImageView profile;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference PostRef = db.collection("Posts");

    //private PostAdapter adapter;

    private ProgressBar mProgressCircle;

    private Boolean isMinimize = true;

    FirestorePagingAdapter adapter;


    private FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mProgressCircle = findViewById(R.id.progress_circle);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SettingCustomToolbar();

        setUpRecyclerView();



    }

    public void addPostBtn(View view)
    {
        Intent addPostIntent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(addPostIntent);
    }

    private void setUpRecyclerView()
    {
        Query query = PostRef.orderBy("counter", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Posts> options = new FirestorePagingOptions.Builder<Posts>()
                .setQuery(query,config,Posts.class)
                .build();

        adapter = new FirestorePagingAdapter<Posts, PostHolder>(options) {
            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_post_layout,parent,false);
                return new PostHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PostHolder holder, int position, @NonNull final Posts model) {
                holder.textViewUsername.setText("\uD83D\uDC68\u200D⚕ "+model.getUsername());
                holder.textViewDescription.setText(model.getDescription());

                holder.relativeLayoutPostImageBox.setVisibility(View.VISIBLE);

                Picasso.get().load(model.getPostimage())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(holder.imageViewPostImage);

                if( model.getPostimage() == null )
                {
                    holder.relativeLayoutPostImageBox.setVisibility(View.GONE);
                }

                holder.textViewDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isMinimize)
                        {
                            holder.textViewDescription.setMaxLines(1000);
                            isMinimize = false;
                        }
                        else
                        {
                            holder.textViewDescription.setMaxLines(12);
                            isMinimize = true;
                        }


                    }
                });

                holder.imageViewPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewImageIntent = new Intent(MainActivity.this,ViewImageActivity.class);
                        viewImageIntent.putExtra("url",model.getPostimage());
                        startActivity(viewImageIntent);
                    }
                });

                holder.textViewUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getUid();


                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,
                                R.style.BottomSheetDialogTheme);

                        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                                .inflate(R.layout.layout_buttom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));


                        final TextView profileName,profileWork,profileLocation,profileUniversity,profileGradDate,profileDescription;

                        profileName = bottomSheetView.findViewById(R.id.profile_username);
                        profileWork = bottomSheetView.findViewById(R.id.profile_work);
                        profileLocation = bottomSheetView.findViewById(R.id.profile_location);

                        profileUniversity = bottomSheetView.findViewById(R.id.profile_university);
                        profileGradDate = bottomSheetView.findViewById(R.id.profile_grad_date);

                        profileDescription = bottomSheetView.findViewById(R.id.profile_description);

                        UsersRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    String  username = dataSnapshot.child("A_name").getValue().toString();
                                    String  city = dataSnapshot.child("C_city").getValue().toString();
                                    String job   = dataSnapshot.child("B_job").getValue().toString();
                                    String university = dataSnapshot.child("E_university").getValue().toString();
                                    String gradDate = dataSnapshot.child("F_dateOfGraduation").getValue().toString();

                                    profileName.setText(username);
                                    profileWork.setText(city);
                                    profileLocation.setText(job);
                                    profileUniversity.setText(university);
                                    profileGradDate.setText("Graduated on : "+gradDate);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        bottomSheetView.findViewById(R.id.buttonSeePost).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText( MainActivity.this,"see my posts", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();



                    }
                });


            }
        };


        recyclerView =  findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        floatingActionButton = findViewById(R.id.float_add_btn);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });


    }

    public class PostHolder extends RecyclerView.ViewHolder{

        TextView textViewUsername;
        TextView textViewDescription;
        ImageView imageViewPostImage;
        RelativeLayout relativeLayoutPostImageBox;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.post_username);
            textViewDescription = itemView.findViewById(R.id.post_description);
            imageViewPostImage = itemView.findViewById(R.id.post_image);
            relativeLayoutPostImageBox = itemView.findViewById(R.id.post_image_box);






        }

    }






    private void SettingCustomToolbar() {
        profile = findViewById(R.id.main_custom_profile);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                SendUserToLoginActivity();
            }
        });

    }

    @Override
    protected void onStart() {


        super.onStart();
        adapter.startListening();
        PostRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    mProgressCircle.setVisibility(View.VISIBLE);
                }
            }
        });


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            SendUserToLoginActivity();
        } else {
            CheckUserExistence();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    private void CheckUserExistence() {
        final String currentuser_id = mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentuser_id)) {
                    SendUserToSetupActivity();
                }
            }

            private void SendUserToSetupActivity() {
                Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(setupIntent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


//    @Override
//    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//
//    }
}



