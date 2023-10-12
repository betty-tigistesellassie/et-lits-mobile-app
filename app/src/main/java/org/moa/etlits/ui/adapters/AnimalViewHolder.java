
package org.moa.etlits.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.moa.etlits.R;
import org.moa.etlits.data.models.Animal;

import androidx.recyclerview.widget.RecyclerView;

class AnimalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView tvAnimalId;
    private final TextView tvSex;
    private final TextView tvBreed;
    private final TextView tvAge;

    private ImageView ivDelete;

    private AnimalListAdapter.AnimalItemEventsListener animalItemEventsListener;
    private AnimalViewHolder(View itemView, AnimalListAdapter.AnimalItemEventsListener animalItemEventsListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.animalItemEventsListener = animalItemEventsListener;
        tvAnimalId = itemView.findViewById(R.id.tv_animal_id);
        tvSex = itemView.findViewById(R.id.tv_sex);
        tvBreed = itemView.findViewById(R.id.tv_breed);
        tvAge = itemView.findViewById(R.id.tv_age);
        ivDelete = itemView.findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animalItemEventsListener != null) {
                    animalItemEventsListener.onAnimalItemDeleteClick(getAdapterPosition());
                }
            }
        });


    }

    public void bind(Animal animal) {
        tvAnimalId.setText(String.valueOf(animal.getAnimalId()));
        tvSex.setText(animal.getSex());
        tvBreed.setText(animal.getBreed());
        tvAge.setText(tvAge.getContext().getString(R.string.animal_reg_age_months, String.valueOf(animal.getAge())));}



    public static AnimalViewHolder create(ViewGroup parent, AnimalListAdapter.AnimalItemEventsListener animalItemEventsListener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_animal_item, parent, false);

        return new AnimalViewHolder(view, animalItemEventsListener);
    }

        @Override
        public void onClick(View v) {
            if(animalItemEventsListener != null) {
                animalItemEventsListener.onAnimalItemClick(getAdapterPosition());
            }
        }

}
