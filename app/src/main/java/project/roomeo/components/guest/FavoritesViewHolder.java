package project.roomeo.components.guest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Accommodation;

public class FavoritesViewHolder  extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView description;
    public TextView location;
    public Button removeFromFav;
    public Button details;

    public FavoritesViewHolder(View itemView) {
        super(itemView);
        // Inicijalizujte vaše komponente ovde
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        location = itemView.findViewById(R.id.location);
        removeFromFav = itemView.findViewById(R.id.removeFromFav);
        details = itemView.findViewById(R.id.details);
        }

    public void bindData(Accommodation item) {
        name.setText(item.getName());
        description.setText(item.getDescription());
        location.setText(item.getLocation());
        }
}
