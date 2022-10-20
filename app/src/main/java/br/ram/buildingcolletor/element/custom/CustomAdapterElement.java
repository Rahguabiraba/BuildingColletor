package br.ram.buildingcolletor.element.custom;

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

public class CustomAdapterElement extends RecyclerView.Adapter<CustomAdapterElement.MyViewHolder>{

    //Declaration variables
    protected Context context;
    protected ArrayList name, requirementsSize;
    private static RecyclerViewClickListener itemListener;
    protected int position;
    protected int row_index = -1;

    //Constructor
    public CustomAdapterElement(Context context,RecyclerViewClickListener itemListener, ArrayList name, ArrayList requirementsSize) {
        this.context = context;
        this.itemListener = itemListener;
        this.name = name;
        this.requirementsSize = requirementsSize;
    }

    @NonNull
    @Override
    public CustomAdapterElement.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_element_layout, parent, false);
        return new CustomAdapterElement.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterElement.MyViewHolder holder, int position) {

        this.position = position;

        holder.name_txt.setText(String.valueOf(name.get(position)));

        holder.requirement_txt.setText("R"+String.valueOf(requirementsSize.get(position)));

        if (row_index == position) {

            holder.elementCard.setCardBackgroundColor(Color.parseColor("#00008B"));

        } else {

            holder.elementCard.setCardBackgroundColor(Color.parseColor("#D0F0ED"));

        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView name_txt;
        protected TextView requirement_txt;
        protected CardView elementCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.elementName);
            requirement_txt = itemView.findViewById(R.id.requirementsCount);
            elementCard = itemView.findViewById(R.id.elementCardView);
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
