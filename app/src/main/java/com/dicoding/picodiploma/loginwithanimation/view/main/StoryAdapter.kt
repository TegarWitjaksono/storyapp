package com.dicoding.picodiploma.loginwithanimation.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem

class StoryAdapter(
    private var stories: List<ListStoryItem?>?,
    private val onItemClick: (ListStoryItem, ImageView) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyImage: ImageView = itemView.findViewById(R.id.story_image)
        val storyName: TextView = itemView.findViewById(R.id.story_name)
        val storyDescription: TextView = itemView.findViewById(R.id.story_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories?.get(position)
        holder.storyName.text = story?.name
        holder.storyDescription.text = story?.description

        Glide.with(holder.itemView.context)
            .load(story?.photoUrl)
            .into(holder.storyImage)

        holder.storyImage.transitionName = "transition_story_image_${story?.id}"

        holder.itemView.setOnClickListener {
            story?.let { item -> onItemClick(item, holder.storyImage) }
        }
    }

    override fun getItemCount(): Int {
        return stories?.size ?: 0
    }

    fun updateStories(newStories: List<ListStoryItem?>?) {
        stories = newStories
        notifyDataSetChanged() // Notify adapter of data change
    }
}