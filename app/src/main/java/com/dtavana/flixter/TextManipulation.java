package com.dtavana.flixter;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dtavana.flixter.databinding.ActivityMovieDetailsBinding;

import java.util.Objects;

public class TextManipulation {
    public static void setTextColor(final ActivityMovieDetailsBinding binding, String imageUrl) {
        GlideApp
                .with(binding.getRoot())
                .asBitmap()
                .load(imageUrl)
                .listener(new RequestListener<Bitmap>() {
                          @Override
                          public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                              Toast.makeText(binding.getRoot().getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                              return false;
                          }

                          @Override
                          public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                              Palette.Swatch s = Palette.from(bitmap).generate().getDominantSwatch();
                              if(s != null) {
                                  int color = s.getBodyTextColor();
                                  Log.d("TextManipulation", String.format("Color chosen: \"0x%08X\"", color));
                                  binding.tvTitle.setTextColor(color);
                                  binding.tvOverview.setTextColor(color);
                                  binding.tvRelease.setTextColor(color);
                                  binding.tvVoteCount.setTextColor(color);
                              }
                              return false;
                          }
                      }
            ).submit();
    }
}
