package br.ram.buildingcolletor.requirement.custom;

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

public class CustomAdapterRequirement extends RecyclerView.Adapter<CustomAdapterRequirement.MyViewHolder>{

    //Declaration variables
    protected Context context;
    protected ArrayList name;
    protected int position;
    private static RecyclerViewClickListener itemListener;
    protected int row_index = -1;

    //Constructor
    public CustomAdapterRequirement(Context context,RecyclerViewClickListener itemListener, ArrayList name) {
        this.context = context;
        this.itemListener = itemListener;
        this.name = name;
    }

    @NonNull
    @Override
    public CustomAdapterRequirement.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_requirement_layout, parent, false);
        return new CustomAdapterRequirement.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterRequirement.MyViewHolder holder, int position) {

        this.position = position;

        holder.name_txt.setText(String.valueOf(name.get(position)));

        if (row_index == position) {

            holder.requirementCard.setCardBackgroundColor(Color.parseColor("#00008B"));

        } else {

            holder.requirementCard.setCardBackgroundColor(Color.parseColor("#D0F0ED"));

        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView name_txt;
        protected CardView requirementCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.requirementType);
            requirementCard = itemView.findViewById(R.id.requirementCardView);
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
