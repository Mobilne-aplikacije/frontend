package project.roomeo.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;

public class PhotoAdapterUpd extends RecyclerView.Adapter<PhotoAdapterUpd.PhotoViewHolder> {

    private final List<Bitmap> photos;
    private final Context context;
    private final OnPhotoClickListener onPhotoClickListener;

    public PhotoAdapterUpd(Context context, List<Bitmap> photos, OnPhotoClickListener onPhotoClickListener) {
        this.context = context;
        this.photos = photos;
        this.onPhotoClickListener = onPhotoClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_upd, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Bitmap photo = photos.get(position);
        holder.imageView.setImageBitmap(photo);

        holder.removeButton.setOnClickListener(v -> {
            onPhotoClickListener.onRemoveClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public interface OnPhotoClickListener {
        void onRemoveClick(int position);
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton removeButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPhoto);
            removeButton = itemView.findViewById(R.id.buttonRemovePhoto);
        }
    }
}
