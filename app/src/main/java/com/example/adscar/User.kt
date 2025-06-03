package com.example.adscar

import android.os.Parcel
import android.os.Parcelable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val password: String,
    val birthDate: String,
    val accessLevel: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(cpf)
        parcel.writeString(password)
        parcel.writeString(birthDate)
        parcel.writeInt(accessLevel)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

