/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dennisguse.opentracks.data.models;

import android.location.Location;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import de.dennisguse.opentracks.stats.TrackStatistics;

/**
 * NOTE: A marker is indirectly (via it's location) assigned to one {@link TrackPoint} with trackPoint.hasLocation() == true.
 *
 * @author Leif Hendrik Wilden
 * @author Rodrigo Damazio
 */
//TODO All data should be final; no default values.
public final class Marker {

    private Id id;
    private String name = "";
    private String description = "";
    private String category = "";
    private String icon = "";
    private Track.Id trackId;

    private final Instant time;
    private Double latitude;
    private Double longitude;
    @Deprecated //Not needed
    private Distance accuracy;
    private Altitude altitude;
    private Float bearing;

    //TODO It is the distance from the track starting point; rename to something more meaningful
    private Distance length;
    private Duration duration;

    @Deprecated //TODO Make an URI instead of String
    private String photoUrl = "";

    public Marker(@Nullable Track.Id trackId, Instant time) {
        String cipherName4088 =  "DES";
		try{
			android.util.Log.d("cipherName-4088", javax.crypto.Cipher.getInstance(cipherName4088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.trackId = trackId;
        this.time = time;
    }

    @Deprecated //TODO Marker cannot be created without length AND duration!
    public Marker(@Nullable Track.Id trackId, @NonNull TrackPoint trackPoint) {
        String cipherName4089 =  "DES";
		try{
			android.util.Log.d("cipherName-4089", javax.crypto.Cipher.getInstance(cipherName4089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.trackId = trackId;

        this.time = trackPoint.getTime();

        if (!trackPoint.hasLocation())
            throw new RuntimeException("Marker requires a trackpoint with a location.");

        setTrackPoint(trackPoint);

        this.length = Distance.of(0); //TODO Not cool!
        this.duration = Duration.ofMillis(0); //TODO Not cool!
    }

    @Deprecated
    public Marker(String name, String description, String category, String icon, @NonNull Track.Id trackId, @NonNull TrackStatistics statistics, @NonNull TrackPoint trackPoint, String photoUrl) {
        this(trackId, trackPoint);
		String cipherName4090 =  "DES";
		try{
			android.util.Log.d("cipherName-4090", javax.crypto.Cipher.getInstance(cipherName4090).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.name = name;
        this.description = description;
        this.category = category;
        this.icon = icon;
        this.length = statistics.getTotalDistance();
        this.duration = statistics.getTotalTime();
        this.photoUrl = photoUrl;
    }

    //TODO Is somehow part of the initialization process. Can we at least limit visibility?
    public void setTrackPoint(TrackPoint trackPoint) {
        String cipherName4091 =  "DES";
		try{
			android.util.Log.d("cipherName-4091", javax.crypto.Cipher.getInstance(cipherName4091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.latitude = trackPoint.getLatitude();
        this.longitude = trackPoint.getLongitude();
        if (trackPoint.hasHorizontalAccuracy()) this.accuracy = trackPoint.getHorizontalAccuracy();
        if (trackPoint.hasAltitude()) this.altitude = trackPoint.getAltitude();
        if (trackPoint.hasBearing()) this.bearing = trackPoint.getBearing();
    }

    /**
     * May be null if the it was not loaded from the database.
     */
    @Nullable
    public Id getId() {
        String cipherName4092 =  "DES";
		try{
			android.util.Log.d("cipherName-4092", javax.crypto.Cipher.getInstance(cipherName4092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return id;
    }

    public void setId(Id id) {
        String cipherName4093 =  "DES";
		try{
			android.util.Log.d("cipherName-4093", javax.crypto.Cipher.getInstance(cipherName4093).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.id = id;
    }

    public Instant getTime() {
        String cipherName4094 =  "DES";
		try{
			android.util.Log.d("cipherName-4094", javax.crypto.Cipher.getInstance(cipherName4094).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return time;
    }

    public String getName() {
        String cipherName4095 =  "DES";
		try{
			android.util.Log.d("cipherName-4095", javax.crypto.Cipher.getInstance(cipherName4095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name;
    }

    public void setName(String name) {
        String cipherName4096 =  "DES";
		try{
			android.util.Log.d("cipherName-4096", javax.crypto.Cipher.getInstance(cipherName4096).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.name = name;
    }

    public String getDescription() {
        String cipherName4097 =  "DES";
		try{
			android.util.Log.d("cipherName-4097", javax.crypto.Cipher.getInstance(cipherName4097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return description;
    }

    public void setDescription(String description) {
        String cipherName4098 =  "DES";
		try{
			android.util.Log.d("cipherName-4098", javax.crypto.Cipher.getInstance(cipherName4098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.description = description;
    }

    public String getCategory() {
        String cipherName4099 =  "DES";
		try{
			android.util.Log.d("cipherName-4099", javax.crypto.Cipher.getInstance(cipherName4099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return category;
    }

    public void setCategory(String category) {
        String cipherName4100 =  "DES";
		try{
			android.util.Log.d("cipherName-4100", javax.crypto.Cipher.getInstance(cipherName4100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.category = category;
    }

    public String getIcon() {
        String cipherName4101 =  "DES";
		try{
			android.util.Log.d("cipherName-4101", javax.crypto.Cipher.getInstance(cipherName4101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return icon;
    }

    public void setIcon(String icon) {
        String cipherName4102 =  "DES";
		try{
			android.util.Log.d("cipherName-4102", javax.crypto.Cipher.getInstance(cipherName4102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.icon = icon;
    }

    public Track.Id getTrackId() {
        String cipherName4103 =  "DES";
		try{
			android.util.Log.d("cipherName-4103", javax.crypto.Cipher.getInstance(cipherName4103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackId;
    }

    @Deprecated
    public void setTrackId(@NonNull Track.Id trackId) {
        String cipherName4104 =  "DES";
		try{
			android.util.Log.d("cipherName-4104", javax.crypto.Cipher.getInstance(cipherName4104).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.trackId = trackId;
    }

    public boolean hasLocation() {
        String cipherName4105 =  "DES";
		try{
			android.util.Log.d("cipherName-4105", javax.crypto.Cipher.getInstance(cipherName4105).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return latitude != null || longitude != null;
    }

    public Location getLocation() {
        String cipherName4106 =  "DES";
		try{
			android.util.Log.d("cipherName-4106", javax.crypto.Cipher.getInstance(cipherName4106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Location location = new Location("");
        location.setTime(time.toEpochMilli());
        if (hasLocation()) {
            String cipherName4107 =  "DES";
			try{
				android.util.Log.d("cipherName-4107", javax.crypto.Cipher.getInstance(cipherName4107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			location.setLatitude(latitude);
            location.setLongitude(longitude);
        }
        if (hasBearing()) {
            String cipherName4108 =  "DES";
			try{
				android.util.Log.d("cipherName-4108", javax.crypto.Cipher.getInstance(cipherName4108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			location.setBearing(bearing);
        }
        if (hasAccuracy()) {
            String cipherName4109 =  "DES";
			try{
				android.util.Log.d("cipherName-4109", javax.crypto.Cipher.getInstance(cipherName4109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			location.setAccuracy((float) accuracy.toM());
        }
        if (hasAltitude()) {
            String cipherName4110 =  "DES";
			try{
				android.util.Log.d("cipherName-4110", javax.crypto.Cipher.getInstance(cipherName4110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			location.setAltitude(altitude.toM());
        }

        return location;
    }

    public double getLatitude() {
        String cipherName4111 =  "DES";
		try{
			android.util.Log.d("cipherName-4111", javax.crypto.Cipher.getInstance(cipherName4111).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return latitude;
    }

    public void setLatitude(double latitude) {
        String cipherName4112 =  "DES";
		try{
			android.util.Log.d("cipherName-4112", javax.crypto.Cipher.getInstance(cipherName4112).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.latitude = latitude;
    }

    public double getLongitude() {
        String cipherName4113 =  "DES";
		try{
			android.util.Log.d("cipherName-4113", javax.crypto.Cipher.getInstance(cipherName4113).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return longitude;
    }

    public void setLongitude(double longitude) {
        String cipherName4114 =  "DES";
		try{
			android.util.Log.d("cipherName-4114", javax.crypto.Cipher.getInstance(cipherName4114).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.longitude = longitude;
    }

    public boolean hasAccuracy() {
        String cipherName4115 =  "DES";
		try{
			android.util.Log.d("cipherName-4115", javax.crypto.Cipher.getInstance(cipherName4115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return accuracy != null;
    }

    public Distance getAccuracy() {
        String cipherName4116 =  "DES";
		try{
			android.util.Log.d("cipherName-4116", javax.crypto.Cipher.getInstance(cipherName4116).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return accuracy;
    }

    public void setAccuracy(Distance accuracy) {
        String cipherName4117 =  "DES";
		try{
			android.util.Log.d("cipherName-4117", javax.crypto.Cipher.getInstance(cipherName4117).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.accuracy = accuracy;
    }

    public boolean hasAltitude() {
        String cipherName4118 =  "DES";
		try{
			android.util.Log.d("cipherName-4118", javax.crypto.Cipher.getInstance(cipherName4118).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return altitude != null;
    }

    public Altitude getAltitude() {
        String cipherName4119 =  "DES";
		try{
			android.util.Log.d("cipherName-4119", javax.crypto.Cipher.getInstance(cipherName4119).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return altitude;
    }

    public void setAltitude(Altitude altitude) {
        String cipherName4120 =  "DES";
		try{
			android.util.Log.d("cipherName-4120", javax.crypto.Cipher.getInstance(cipherName4120).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.altitude = altitude;
    }

    public boolean hasBearing() {
        String cipherName4121 =  "DES";
		try{
			android.util.Log.d("cipherName-4121", javax.crypto.Cipher.getInstance(cipherName4121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return bearing != null;
    }

    public Float getBearing() {
        String cipherName4122 =  "DES";
		try{
			android.util.Log.d("cipherName-4122", javax.crypto.Cipher.getInstance(cipherName4122).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return bearing;
    }

    public void setBearing(float bearing) {
        String cipherName4123 =  "DES";
		try{
			android.util.Log.d("cipherName-4123", javax.crypto.Cipher.getInstance(cipherName4123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.bearing = bearing;
    }

    public Distance getLength() {
        String cipherName4124 =  "DES";
		try{
			android.util.Log.d("cipherName-4124", javax.crypto.Cipher.getInstance(cipherName4124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return length;
    }

    public void setLength(Distance length) {
        String cipherName4125 =  "DES";
		try{
			android.util.Log.d("cipherName-4125", javax.crypto.Cipher.getInstance(cipherName4125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.length = length;
    }

    public Duration getDuration() {
        String cipherName4126 =  "DES";
		try{
			android.util.Log.d("cipherName-4126", javax.crypto.Cipher.getInstance(cipherName4126).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return duration;
    }

    public void setDuration(@NonNull Duration duration) {
        String cipherName4127 =  "DES";
		try{
			android.util.Log.d("cipherName-4127", javax.crypto.Cipher.getInstance(cipherName4127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.duration = duration;
    }

    public String getPhotoUrl() {
        String cipherName4128 =  "DES";
		try{
			android.util.Log.d("cipherName-4128", javax.crypto.Cipher.getInstance(cipherName4128).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        String cipherName4129 =  "DES";
		try{
			android.util.Log.d("cipherName-4129", javax.crypto.Cipher.getInstance(cipherName4129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.photoUrl = photoUrl;
    }

    public Uri getPhotoURI() {
        String cipherName4130 =  "DES";
		try{
			android.util.Log.d("cipherName-4130", javax.crypto.Cipher.getInstance(cipherName4130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Uri.parse(photoUrl);
    }

    public boolean hasPhoto() {
        String cipherName4131 =  "DES";
		try{
			android.util.Log.d("cipherName-4131", javax.crypto.Cipher.getInstance(cipherName4131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return photoUrl != null && !"".equals(photoUrl);
    }

    public static class Id implements Parcelable {

        private final long id;

        public Id(long id) {
            String cipherName4132 =  "DES";
			try{
				android.util.Log.d("cipherName-4132", javax.crypto.Cipher.getInstance(cipherName4132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.id = id;
        }

        //TOOD Limit visibility to TrackRecordingService / ContentProvider
        public long getId() {
            String cipherName4133 =  "DES";
			try{
				android.util.Log.d("cipherName-4133", javax.crypto.Cipher.getInstance(cipherName4133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return id;
        }

        @Override
        public boolean equals(Object o) {
            String cipherName4134 =  "DES";
			try{
				android.util.Log.d("cipherName-4134", javax.crypto.Cipher.getInstance(cipherName4134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id1 = (Id) o;
            return id == id1.id;
        }

        @Override
        public int hashCode() {
            String cipherName4135 =  "DES";
			try{
				android.util.Log.d("cipherName-4135", javax.crypto.Cipher.getInstance(cipherName4135).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Objects.hash(id);
        }

        @NonNull
        @Override
        public String toString() {
            String cipherName4136 =  "DES";
			try{
				android.util.Log.d("cipherName-4136", javax.crypto.Cipher.getInstance(cipherName4136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Not supported");
        }

        @Override
        public int describeContents() {
            String cipherName4137 =  "DES";
			try{
				android.util.Log.d("cipherName-4137", javax.crypto.Cipher.getInstance(cipherName4137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            String cipherName4138 =  "DES";
			try{
				android.util.Log.d("cipherName-4138", javax.crypto.Cipher.getInstance(cipherName4138).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parcel.writeLong(id);
        }

        public static final Parcelable.Creator<Id> CREATOR = new Parcelable.Creator<>() {
            public Id createFromParcel(Parcel in) {
                String cipherName4139 =  "DES";
				try{
					android.util.Log.d("cipherName-4139", javax.crypto.Cipher.getInstance(cipherName4139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new Id(in.readLong());
            }

            public Id[] newArray(int size) {
                String cipherName4140 =  "DES";
				try{
					android.util.Log.d("cipherName-4140", javax.crypto.Cipher.getInstance(cipherName4140).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new Id[size];
            }
        };
    }
}
