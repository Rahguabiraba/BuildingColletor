package br.ram.buildingcolletor.asset.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.ram.buildingcolletor.R;
import br.ram.buildingcolletor.RecyclerViewClickListener;

public class CustomAdapterAsset extends RecyclerView.Adapter<CustomAdapterAsset.MyViewHolder>{

    //Declaración de variables
    protected Context context;
    protected ArrayList  name;
    protected int position;
    protected int row_index = -1;
    private static RecyclerViewClickListener itemListener;

    //Constructor
    public CustomAdapterAsset(Context context, RecyclerViewClickListener itemListener, ArrayList name) {
        this.context = context;
        this.itemListener = itemListener;
        this.name = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_asset_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        this.position = position;

        holder.name_txt.setText(String.valueOf(name.get(position)));

        if (row_index == position) {

            holder.assetCard.setCardBackgroundColor(Color.parseColor("#00008B"));

        } else {

            holder.assetCard.setCardBackgroundColor(Color.parseColor("#D0F0ED"));

        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView name_txt;
        protected CardView assetCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.assetName);
            assetCard = itemView.findViewById(R.id.assetCardView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            row_index = this.getLayoutPosition();
            notifyDataSetChanged();

            itemListener.recyclerViewListClicked(view,this.getLayoutPosition());
        }

    }
}
