package com.example.rapportv2;import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.os.Bundle;import android.sax.StartElementListener;import android.view.View;import android.widget.ImageView;import com.squareup.picasso.Picasso;public class ViewImageActivity extends AppCompatActivity {    ImageView image;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_view_image);        image = findViewById(R.id.viewimage_image);        String url = getIntent().getExtras().get("url").toString();        Picasso.get().load(url)                .placeholder(R.mipmap.ic_launcher)                .into(image);    }}