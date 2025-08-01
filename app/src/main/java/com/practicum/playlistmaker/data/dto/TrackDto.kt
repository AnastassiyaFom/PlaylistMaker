package com.practicum.playlistmaker.data.dto


import android.os.Parcel
import android.os.Parcelable
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId:Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis:Long,
    val artworkUrl100: String,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country:String,
    val previewUrl:String
)


    /*: Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
         parcel.readLong()?: 0L,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackDto> {
        override fun createFromParcel(parcel: Parcel): TrackDto {
            return TrackDto(parcel)
        }

        override fun newArray(size: Int): Array<TrackDto?> {
            return arrayOfNulls(size)
        }
    }

}
*/