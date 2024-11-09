package com.dicoding.picodiploma.loginwithanimation.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StoryResponse(
	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListStoryItem(
	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null, // Ubah dari Any? menjadi Double?

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null // Ubah dari Any? menjadi Double?
) : Parcelable {
	constructor(parcel: Parcel) : this(
		photoUrl = parcel.readString(),
		createdAt = parcel.readString(),
		name = parcel.readString(),
		description = parcel.readString(),
		lon = parcel.readValue(Double::class.java.classLoader) as? Double, // Ubah ke Double
		id = parcel.readString(),
		lat = parcel.readValue(Double::class.java.classLoader) as? Double // Ubah ke Double
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(photoUrl)
		parcel.writeString(createdAt)
		parcel.writeString(name)
		parcel.writeString(description)
		parcel.writeValue(lon)
		parcel.writeString(id)
		parcel.writeValue(lat)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ListStoryItem> {
		override fun createFromParcel(parcel: Parcel): ListStoryItem {
			return ListStoryItem(parcel)
		}

		override fun newArray(size: Int): Array<ListStoryItem?> {
			return arrayOfNulls(size)
		}
	}
}