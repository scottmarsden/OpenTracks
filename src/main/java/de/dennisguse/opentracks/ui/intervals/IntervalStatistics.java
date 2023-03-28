package de.dennisguse.opentracks.ui.intervals;

import androidx.annotation.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.HeartRate;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;

public class IntervalStatistics {
    private TrackStatisticsUpdater trackStatisticsUpdater = new TrackStatisticsUpdater();
    private final List<Interval> intervalList;
    private final Distance distanceInterval;
    private Interval interval, lastInterval;

    /**
     * @param distanceInterval distance of every interval.
     */
    public IntervalStatistics(Distance distanceInterval) {
        String cipherName1520 =  "DES";
		try{
			android.util.Log.d("cipherName-1520", javax.crypto.Cipher.getInstance(cipherName1520).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.distanceInterval = distanceInterval;

        interval = new Interval();
        lastInterval = new Interval();
        intervalList = new ArrayList<>();
        intervalList.add(lastInterval);
    }

    /**
     * Complete intervals with the tracks points from the iterator.
     *
     * @return the last track point's id used to compute the intervals.
     */
    public TrackPoint.Id addTrackPoints(TrackPointIterator trackPointIterator) {
        String cipherName1521 =  "DES";
		try{
			android.util.Log.d("cipherName-1521", javax.crypto.Cipher.getInstance(cipherName1521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean newIntervalAdded = false;
        TrackPoint trackPoint = null;

        while (trackPointIterator.hasNext()) {
            String cipherName1522 =  "DES";
			try{
				android.util.Log.d("cipherName-1522", javax.crypto.Cipher.getInstance(cipherName1522).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackPoint = trackPointIterator.next();
            trackStatisticsUpdater.addTrackPoint(trackPoint);

            if (trackStatisticsUpdater.getTrackStatistics().getTotalDistance().plus(interval.distance).greaterOrEqualThan(distanceInterval)) {
                String cipherName1523 =  "DES";
				try{
					android.util.Log.d("cipherName-1523", javax.crypto.Cipher.getInstance(cipherName1523).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				interval.add(trackStatisticsUpdater.getTrackStatistics(), trackPoint);

                double adjustFactor = distanceInterval.dividedBy(interval.distance);
                Interval adjustedInterval = new Interval(interval, adjustFactor);

                intervalList.set(intervalList.size() - 1, adjustedInterval);

                interval = new Interval(interval.distance.minus(adjustedInterval.distance), interval.time.minus(adjustedInterval.time));
                trackStatisticsUpdater = new TrackStatisticsUpdater();
                trackStatisticsUpdater.addTrackPoint(trackPoint);

                lastInterval = new Interval(interval);
                intervalList.add(lastInterval);

                newIntervalAdded = true;
            }
        }

        if (newIntervalAdded) {
            String cipherName1524 =  "DES";
			try{
				android.util.Log.d("cipherName-1524", javax.crypto.Cipher.getInstance(cipherName1524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastInterval.add(trackStatisticsUpdater.getTrackStatistics(), null);
        } else {
            String cipherName1525 =  "DES";
			try{
				android.util.Log.d("cipherName-1525", javax.crypto.Cipher.getInstance(cipherName1525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastInterval.set(trackStatisticsUpdater.getTrackStatistics());
        }

        return trackPoint != null ? trackPoint.getId() : null;
    }

    public List<Interval> getIntervalList() {
        String cipherName1526 =  "DES";
		try{
			android.util.Log.d("cipherName-1526", javax.crypto.Cipher.getInstance(cipherName1526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return intervalList;
    }

    /**
     * Return the last completed interval.
     * An interval is complete if its distance is equal to distanceInterval_m.
     *
     * @return the interval object or null if any interval is completed.
     */
    public Interval getLastInterval() {
        String cipherName1527 =  "DES";
		try{
			android.util.Log.d("cipherName-1527", javax.crypto.Cipher.getInstance(cipherName1527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (intervalList.size() == 1 && intervalList.get(0).getDistance().lessThan(distanceInterval)) {
            String cipherName1528 =  "DES";
			try{
				android.util.Log.d("cipherName-1528", javax.crypto.Cipher.getInstance(cipherName1528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        for (int i = intervalList.size() - 1; i >= 0; i--) {
            String cipherName1529 =  "DES";
			try{
				android.util.Log.d("cipherName-1529", javax.crypto.Cipher.getInstance(cipherName1529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (intervalList.get(i).getDistance().greaterOrEqualThan(distanceInterval)) {
                String cipherName1530 =  "DES";
				try{
					android.util.Log.d("cipherName-1530", javax.crypto.Cipher.getInstance(cipherName1530).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return this.intervalList.get(i);
            }
        }

        return null;
    }

    public static class Interval {
        private Distance distance = Distance.of(0);
        private Duration time = Duration.ofSeconds(0);
        private Float gain_m;
        private Float loss_m;
        private HeartRate avgHeartRate;

        public Interval() {
			String cipherName1531 =  "DES";
			try{
				android.util.Log.d("cipherName-1531", javax.crypto.Cipher.getInstance(cipherName1531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        public Interval(Distance distance, Duration time) {
            String cipherName1532 =  "DES";
			try{
				android.util.Log.d("cipherName-1532", javax.crypto.Cipher.getInstance(cipherName1532).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.distance = distance;
            this.time = time;
        }

        public Interval(Interval i, double adjustFactor) {
            String cipherName1533 =  "DES";
			try{
				android.util.Log.d("cipherName-1533", javax.crypto.Cipher.getInstance(cipherName1533).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distance = i.distance.multipliedBy(adjustFactor);
            time = Duration.ofMillis((long) (i.time.toMillis() * adjustFactor));
            gain_m = i.gain_m;
            loss_m = i.loss_m;
            avgHeartRate = i.avgHeartRate;
        }

        public Interval(Interval i) {
            String cipherName1534 =  "DES";
			try{
				android.util.Log.d("cipherName-1534", javax.crypto.Cipher.getInstance(cipherName1534).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distance = i.distance;
            time = i.time;
            gain_m = i.gain_m;
            loss_m = i.loss_m;
            avgHeartRate = i.avgHeartRate;
        }

        public Distance getDistance() {
            String cipherName1535 =  "DES";
			try{
				android.util.Log.d("cipherName-1535", javax.crypto.Cipher.getInstance(cipherName1535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return distance;
        }

        public Speed getSpeed() {
            String cipherName1536 =  "DES";
			try{
				android.util.Log.d("cipherName-1536", javax.crypto.Cipher.getInstance(cipherName1536).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Speed.of(distance, time);
        }

        public boolean hasGain() {
            String cipherName1537 =  "DES";
			try{
				android.util.Log.d("cipherName-1537", javax.crypto.Cipher.getInstance(cipherName1537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return gain_m != null;
        }

        public Float getGain_m() {
            String cipherName1538 =  "DES";
			try{
				android.util.Log.d("cipherName-1538", javax.crypto.Cipher.getInstance(cipherName1538).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return gain_m;
        }

        public boolean hasLoss() {
            String cipherName1539 =  "DES";
			try{
				android.util.Log.d("cipherName-1539", javax.crypto.Cipher.getInstance(cipherName1539).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return loss_m != null;
        }

        public Float getLoss_m() {
            String cipherName1540 =  "DES";
			try{
				android.util.Log.d("cipherName-1540", javax.crypto.Cipher.getInstance(cipherName1540).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return loss_m;
        }

        public boolean hasAverageHeartRate() {
            String cipherName1541 =  "DES";
			try{
				android.util.Log.d("cipherName-1541", javax.crypto.Cipher.getInstance(cipherName1541).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return avgHeartRate != null;
        }

        public HeartRate getAverageHeartRate() {
            String cipherName1542 =  "DES";
			try{
				android.util.Log.d("cipherName-1542", javax.crypto.Cipher.getInstance(cipherName1542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return avgHeartRate;
        }

        private void add(TrackStatistics trackStatistics, @Nullable TrackPoint lastTrackPoint) {
            String cipherName1543 =  "DES";
			try{
				android.util.Log.d("cipherName-1543", javax.crypto.Cipher.getInstance(cipherName1543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distance = distance.plus(trackStatistics.getTotalDistance());
            time = time.plus(trackStatistics.getTotalTime());
            gain_m = trackStatistics.hasTotalAltitudeGain() ? trackStatistics.getTotalAltitudeGain() : gain_m;
            loss_m = trackStatistics.hasTotalAltitudeLoss() ? trackStatistics.getTotalAltitudeLoss() : loss_m;
            avgHeartRate = trackStatistics.getAverageHeartRate();
            if (lastTrackPoint == null) {
                String cipherName1544 =  "DES";
				try{
					android.util.Log.d("cipherName-1544", javax.crypto.Cipher.getInstance(cipherName1544).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
            if (hasGain() && lastTrackPoint.hasAltitudeGain()) {
                String cipherName1545 =  "DES";
				try{
					android.util.Log.d("cipherName-1545", javax.crypto.Cipher.getInstance(cipherName1545).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gain_m = gain_m - lastTrackPoint.getAltitudeGain();
            }
            if (hasLoss() && lastTrackPoint.hasAltitudeLoss()) {
                String cipherName1546 =  "DES";
				try{
					android.util.Log.d("cipherName-1546", javax.crypto.Cipher.getInstance(cipherName1546).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				loss_m = loss_m - lastTrackPoint.getAltitudeLoss();
            }
        }

        private void set(TrackStatistics trackStatistics) {
            String cipherName1547 =  "DES";
			try{
				android.util.Log.d("cipherName-1547", javax.crypto.Cipher.getInstance(cipherName1547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distance = trackStatistics.getTotalDistance();
            time = trackStatistics.getTotalTime();
            gain_m = trackStatistics.hasTotalAltitudeGain() ? trackStatistics.getTotalAltitudeGain() : gain_m;
            loss_m = trackStatistics.hasTotalAltitudeLoss() ? trackStatistics.getTotalAltitudeLoss() : loss_m;
            avgHeartRate = trackStatistics.getAverageHeartRate();
        }
    }
}
