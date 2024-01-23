package com.lab.crmanagement.navigationmenu.itemmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private List<String> sectionNames;
    private SectionListener listener;

    public SectionAdapter(List<String> sectionNames, SectionListener listener) {
        this.sectionNames = sectionNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seciton_item, parent, false);
        return new SectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionHolder holder, int position) {
        holder.getSectionButton().setText(sectionNames.get(position));

        //todo it has to show items when its clicked
        holder.getSectionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener !=null)
                {
                    listener.getMenuItems(holder.getSectionButton().getText().toString());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return sectionNames.size();
    }

    public static class SectionHolder extends RecyclerView.ViewHolder{
        private Button sectionButton;

        public SectionHolder(@NonNull View itemView) {
            super(itemView);
            sectionButton = itemView.findViewById(R.id.sectionButton);
        }

        public Button getSectionButton() {
            return sectionButton;
        }
    }

    public interface SectionListener{
        void getMenuItems(String sectionName);
    }
}
