// Generated by view binder compiler. Do not edit!
package com.example.moviehub.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.moviehub.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMovieBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton backBtn;

  @NonNull
  public final TextView data;

  @NonNull
  public final TextView dataRilascio;

  @NonNull
  public final ImageView movieCover;

  @NonNull
  public final TextView movieTitle;

  @NonNull
  public final ScrollView scrollView2;

  @NonNull
  public final TextView tramaMedia;

  @NonNull
  public final TextView valutazioneMedia;

  private ActivityMovieBinding(@NonNull ConstraintLayout rootView, @NonNull ImageButton backBtn,
      @NonNull TextView data, @NonNull TextView dataRilascio, @NonNull ImageView movieCover,
      @NonNull TextView movieTitle, @NonNull ScrollView scrollView2, @NonNull TextView tramaMedia,
      @NonNull TextView valutazioneMedia) {
    this.rootView = rootView;
    this.backBtn = backBtn;
    this.data = data;
    this.dataRilascio = dataRilascio;
    this.movieCover = movieCover;
    this.movieTitle = movieTitle;
    this.scrollView2 = scrollView2;
    this.tramaMedia = tramaMedia;
    this.valutazioneMedia = valutazioneMedia;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMovieBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMovieBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_movie, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMovieBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backBtn;
      ImageButton backBtn = ViewBindings.findChildViewById(rootView, id);
      if (backBtn == null) {
        break missingId;
      }

      id = R.id.data;
      TextView data = ViewBindings.findChildViewById(rootView, id);
      if (data == null) {
        break missingId;
      }

      id = R.id.dataRilascio;
      TextView dataRilascio = ViewBindings.findChildViewById(rootView, id);
      if (dataRilascio == null) {
        break missingId;
      }

      id = R.id.movieCover;
      ImageView movieCover = ViewBindings.findChildViewById(rootView, id);
      if (movieCover == null) {
        break missingId;
      }

      id = R.id.movieTitle;
      TextView movieTitle = ViewBindings.findChildViewById(rootView, id);
      if (movieTitle == null) {
        break missingId;
      }

      id = R.id.scrollView2;
      ScrollView scrollView2 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView2 == null) {
        break missingId;
      }

      id = R.id.tramaMedia;
      TextView tramaMedia = ViewBindings.findChildViewById(rootView, id);
      if (tramaMedia == null) {
        break missingId;
      }

      id = R.id.valutazioneMedia;
      TextView valutazioneMedia = ViewBindings.findChildViewById(rootView, id);
      if (valutazioneMedia == null) {
        break missingId;
      }

      return new ActivityMovieBinding((ConstraintLayout) rootView, backBtn, data, dataRilascio,
          movieCover, movieTitle, scrollView2, tramaMedia, valutazioneMedia);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
