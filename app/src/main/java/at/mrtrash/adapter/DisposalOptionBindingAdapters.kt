package at.mrtrash.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * BindingAdapter for setting the image resource
 */
@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, res: Int) {
    imageView.setImageResource(res)
}