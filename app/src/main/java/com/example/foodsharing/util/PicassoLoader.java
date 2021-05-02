package com.example.foodsharing.util;

import android.net.Uri;
import android.widget.ImageView;

import com.example.foodsharing.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PicassoLoader {

    public static void loadImage(Uri url, ImageView imageView) {
        Picasso.get().load(url).transform(new CropCircleTransformation()).error(R.drawable.ic_launcher_background).into(imageView);
    }
}
