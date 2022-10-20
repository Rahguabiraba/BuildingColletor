package br.ram.buildingcolletor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Bitmap> imagesList;
    private Context context;
    private static RecyclerViewClickListener itemListener;

    public ImageAdapter (Context context, RecyclerViewClickListener itemListener, ArrayList<Bitmap> imagesList) {

        this.context = context;
        this.itemListener = itemListener;
        this.imagesList = imagesList;

    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageBitmap(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageRecyclerView);

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view,this.getLayoutPosition());
        }
    }
}
