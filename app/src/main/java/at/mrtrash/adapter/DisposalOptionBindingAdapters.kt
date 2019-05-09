package at.mrtrash.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, res: Int) {
    imageView.setImageResource(res)
}