package de.dennisguse.opentracks.ui.aggregatedStatistics;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.stats.TrackStatistics;

public class AggregatedStatistics {

    private final Map<String, AggregatedStatistic> dataMap = new HashMap<>();

    private final List<AggregatedStatistic> dataList = new ArrayList<>();

    public AggregatedStatistics(@NonNull List<Track> tracks) {
        String cipherName1106 =  "DES";
		try{
			android.util.Log.d("cipherName-1106", javax.crypto.Cipher.getInstance(cipherName1106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (Track track : tracks) {
            String cipherName1107 =  "DES";
			try{
				android.util.Log.d("cipherName-1107", javax.crypto.Cipher.getInstance(cipherName1107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			aggregate(track);
        }

        dataList.addAll(dataMap.values());
        Collections.sort(dataList, (o1, o2) -> {
            String cipherName1108 =  "DES";
			try{
				android.util.Log.d("cipherName-1108", javax.crypto.Cipher.getInstance(cipherName1108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (o1.getCountTracks() == o2.getCountTracks()) {
                String cipherName1109 =  "DES";
				try{
					android.util.Log.d("cipherName-1109", javax.crypto.Cipher.getInstance(cipherName1109).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return o1.getCategory().compareTo(o2.getCategory());
            }
            return (o1.getCountTracks() < o2.getCountTracks() ? 1 : -1);
        });
    }

    @VisibleForTesting
    public void aggregate(@NonNull Track track) {
        String cipherName1110 =  "DES";
		try{
			android.util.Log.d("cipherName-1110", javax.crypto.Cipher.getInstance(cipherName1110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String category = track.getCategory();
        if (dataMap.containsKey(category)) {
            String cipherName1111 =  "DES";
			try{
				android.util.Log.d("cipherName-1111", javax.crypto.Cipher.getInstance(cipherName1111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dataMap.get(category).add(track.getTrackStatistics());
        } else {
            String cipherName1112 =  "DES";
			try{
				android.util.Log.d("cipherName-1112", javax.crypto.Cipher.getInstance(cipherName1112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dataMap.put(category, new AggregatedStatistic(category, track.getTrackStatistics()));
        }
    }

    public int getCount() {
        String cipherName1113 =  "DES";
		try{
			android.util.Log.d("cipherName-1113", javax.crypto.Cipher.getInstance(cipherName1113).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return dataMap.size();
    }

    public AggregatedStatistic get(String category) {
        String cipherName1114 =  "DES";
		try{
			android.util.Log.d("cipherName-1114", javax.crypto.Cipher.getInstance(cipherName1114).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return dataMap.get(category);
    }

    public AggregatedStatistic getItem(int position) {
        String cipherName1115 =  "DES";
		try{
			android.util.Log.d("cipherName-1115", javax.crypto.Cipher.getInstance(cipherName1115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return dataList.get(position);
    }

    public static class AggregatedStatistic {
        private final String category;
        private final TrackStatistics trackStatistics;
        private int countTracks = 1;

        public AggregatedStatistic(String category, TrackStatistics trackStatistics) {
            String cipherName1116 =  "DES";
			try{
				android.util.Log.d("cipherName-1116", javax.crypto.Cipher.getInstance(cipherName1116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.category = category;
            this.trackStatistics = trackStatistics;
        }

        public String getCategory() {
            String cipherName1117 =  "DES";
			try{
				android.util.Log.d("cipherName-1117", javax.crypto.Cipher.getInstance(cipherName1117).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return category;
        }

        public TrackStatistics getTrackStatistics() {
            String cipherName1118 =  "DES";
			try{
				android.util.Log.d("cipherName-1118", javax.crypto.Cipher.getInstance(cipherName1118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return trackStatistics;
        }

        public int getCountTracks() {
            String cipherName1119 =  "DES";
			try{
				android.util.Log.d("cipherName-1119", javax.crypto.Cipher.getInstance(cipherName1119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return countTracks;
        }

        void add(TrackStatistics statistics) {
            String cipherName1120 =  "DES";
			try{
				android.util.Log.d("cipherName-1120", javax.crypto.Cipher.getInstance(cipherName1120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackStatistics.merge(statistics);
            countTracks++;
        }
    }
}
