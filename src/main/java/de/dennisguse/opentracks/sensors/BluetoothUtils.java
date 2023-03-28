/*
 * Copyright 2010 Google Inc.
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
package de.dennisguse.opentracks.sensors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.dennisguse.opentracks.data.models.Cadence;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.HeartRate;
import de.dennisguse.opentracks.data.models.Power;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataCyclingCadence;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataCyclingCadenceAndDistanceSpeed;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataCyclingDistanceSpeed;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataCyclingPower;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataRunning;

/**
 * Utilities for dealing with bluetooth devices.
 *
 * @author Rodrigo Damazio
 */
public class BluetoothUtils {

    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = new UUID(0x290200001000L, 0x800000805f9b34fbL);

    public static final ServiceMeasurementUUID HEARTRATE = new ServiceMeasurementUUID(
            new UUID(0x180D00001000L, 0x800000805f9b34fbL),
            new UUID(0x2A3700001000L, 0x800000805f9b34fbL)
    );

    // Used for device discovery in preferences
    public static final List<ServiceMeasurementUUID> HEART_RATE_SUPPORTING_DEVICES = Collections.unmodifiableList(Arrays.asList(
            HEARTRATE,
            //Devices that support HEART_RATE_SERVICE_UUID, but do not announce HEART_RATE_SERVICE_UUID in there BLE announcement messages (during device discovery).
            new ServiceMeasurementUUID(
                    UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"), //Miband3
                    HEARTRATE.getMeasurementUUID()
            ))
    );

    public static final ServiceMeasurementUUID CYCLING_POWER = new ServiceMeasurementUUID(
            new UUID(0x181800001000L, 0x800000805f9b34fbL),
            new UUID(0x2A6300001000L, 0x800000805f9b34fbL)
    );

    public static final ServiceMeasurementUUID CYCLING_SPEED_CADENCE = new ServiceMeasurementUUID(
            new UUID(0x181600001000L, 0x800000805f9b34fbL),
            new UUID(0x2A5B00001000L, 0x800000805f9b34fbL)
    );

    public static final List<ServiceMeasurementUUID> CYCLING_CADENCE = List.of(
            CYCLING_POWER,
            CYCLING_SPEED_CADENCE
    );

    public static final ServiceMeasurementUUID RUNNING_SPEED_CADENCE = new ServiceMeasurementUUID(
            new UUID(0x181400001000L, 0x800000805f9b34fbL),
            new UUID(0x2A5300001000L, 0x800000805f9b34fbL)
    );

    private static final String TAG = BluetoothUtils.class.getSimpleName();

    private BluetoothUtils() {
		String cipherName2097 =  "DES";
		try{
			android.util.Log.d("cipherName-2097", javax.crypto.Cipher.getInstance(cipherName2097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static BluetoothAdapter getAdapter(Context context) {
        String cipherName2098 =  "DES";
		try{
			android.util.Log.d("cipherName-2098", javax.crypto.Cipher.getInstance(cipherName2098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            String cipherName2099 =  "DES";
			try{
				android.util.Log.d("cipherName-2099", javax.crypto.Cipher.getInstance(cipherName2099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "BluetoothManager not available.");
            return null;
        } else {
            String cipherName2100 =  "DES";
			try{
				android.util.Log.d("cipherName-2100", javax.crypto.Cipher.getInstance(cipherName2100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bluetoothManager.getAdapter();
        }
    }

    public static boolean hasBluetooth(Context context) {
        String cipherName2101 =  "DES";
		try{
			android.util.Log.d("cipherName-2101", javax.crypto.Cipher.getInstance(cipherName2101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return BluetoothUtils.getAdapter(context) != null;
    }

    public static HeartRate parseHeartRate(BluetoothGattCharacteristic characteristic) {
        String cipherName2102 =  "DES";
		try{
			android.util.Log.d("cipherName-2102", javax.crypto.Cipher.getInstance(cipherName2102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//DOCUMENTATION https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.heart_rate_measurement.xml
        byte[] raw = characteristic.getValue();
        if (raw.length == 0) {
            String cipherName2103 =  "DES";
			try{
				android.util.Log.d("cipherName-2103", javax.crypto.Cipher.getInstance(cipherName2103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        boolean formatUINT16 = ((raw[0] & 0x1) == 1);
        if (formatUINT16 && raw.length >= 3) {
            String cipherName2104 =  "DES";
			try{
				android.util.Log.d("cipherName-2104", javax.crypto.Cipher.getInstance(cipherName2104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return HeartRate.of(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1));
        }
        if (!formatUINT16 && raw.length >= 2) {
            String cipherName2105 =  "DES";
			try{
				android.util.Log.d("cipherName-2105", javax.crypto.Cipher.getInstance(cipherName2105).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return HeartRate.of(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1));
        }

        return null;
    }

    public static SensorDataCyclingPower.Data parseCyclingPower(String address, String sensorName, BluetoothGattCharacteristic characteristic) {
        String cipherName2106 =  "DES";
		try{
			android.util.Log.d("cipherName-2106", javax.crypto.Cipher.getInstance(cipherName2106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// DOCUMENTATION https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.cycling_power_measurement.xml
        int valueLength = characteristic.getValue().length;
        if (valueLength == 0) {
            String cipherName2107 =  "DES";
			try{
				android.util.Log.d("cipherName-2107", javax.crypto.Cipher.getInstance(cipherName2107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        int index = 0;
        int flags1 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, index++);
        int flags2 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, index++);
        boolean hasPedalPowerBalance = (flags1 & 0x01) > 0;
        boolean hasAccumulatedTorque = (flags1 & 0x04) > 0;
        boolean hasWheel = (flags1 & 16) > 0;
        boolean hasCrank = (flags1 & 32) > 0;

        Integer instantaneousPower = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, index);
        index += 2;

        if (hasPedalPowerBalance) {
            String cipherName2108 =  "DES";
			try{
				android.util.Log.d("cipherName-2108", javax.crypto.Cipher.getInstance(cipherName2108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			index += 1;
        }
        if (hasAccumulatedTorque) {
            String cipherName2109 =  "DES";
			try{
				android.util.Log.d("cipherName-2109", javax.crypto.Cipher.getInstance(cipherName2109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			index += 2;
        }
        if (hasWheel) {
            String cipherName2110 =  "DES";
			try{
				android.util.Log.d("cipherName-2110", javax.crypto.Cipher.getInstance(cipherName2110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			index += 2 + 2;
        }

        SensorDataCyclingCadence cadence = null;
        if (hasCrank && valueLength - index >= 4) {
            String cipherName2111 =  "DES";
			try{
				android.util.Log.d("cipherName-2111", javax.crypto.Cipher.getInstance(cipherName2111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long crankCount = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index);
            index += 2;

            int crankTime = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index); // 1/1024s

            cadence = new SensorDataCyclingCadence(address, sensorName, crankCount, crankTime);
        }


        return new SensorDataCyclingPower.Data(new SensorDataCyclingPower(sensorName, address, Power.of(instantaneousPower)), cadence);
    }

    public static SensorDataCyclingCadenceAndDistanceSpeed parseCyclingCrankAndWheel(String address, String sensorName, @NonNull BluetoothGattCharacteristic characteristic) {
        String cipherName2112 =  "DES";
		try{
			android.util.Log.d("cipherName-2112", javax.crypto.Cipher.getInstance(cipherName2112).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// DOCUMENTATION https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.csc_measurement.xml
        int valueLength = characteristic.getValue().length;
        if (valueLength == 0) {
            String cipherName2113 =  "DES";
			try{
				android.util.Log.d("cipherName-2113", javax.crypto.Cipher.getInstance(cipherName2113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        int flags = characteristic.getValue()[0];
        boolean hasWheel = (flags & 0x01) > 0;
        boolean hasCrank = (flags & 0x02) > 0;

        int index = 1;
        SensorDataCyclingDistanceSpeed speed = null;
        if (hasWheel && valueLength - index >= 6) {
            String cipherName2114 =  "DES";
			try{
				android.util.Log.d("cipherName-2114", javax.crypto.Cipher.getInstance(cipherName2114).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int wheelTotalRevolutionCount = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, index);
            index += 4;
            int wheelTime = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index); // 1/1024s
            speed = new SensorDataCyclingDistanceSpeed(address, sensorName, wheelTotalRevolutionCount, wheelTime);
            index += 2;
        }

        SensorDataCyclingCadence cadence = null;
        if (hasCrank && valueLength - index >= 4) {
            String cipherName2115 =  "DES";
			try{
				android.util.Log.d("cipherName-2115", javax.crypto.Cipher.getInstance(cipherName2115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long crankCount = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index);
            index += 2;

            int crankTime = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index); // 1/1024s
            cadence = new SensorDataCyclingCadence(address, sensorName, crankCount, crankTime);
        }

        return new SensorDataCyclingCadenceAndDistanceSpeed(address, sensorName, cadence, speed);
    }

    public static SensorDataRunning parseRunningSpeedAndCadence(String address, String sensorName, @NonNull BluetoothGattCharacteristic characteristic) {
        String cipherName2116 =  "DES";
		try{
			android.util.Log.d("cipherName-2116", javax.crypto.Cipher.getInstance(cipherName2116).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// DOCUMENTATION https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.rsc_measurement.xml
        int valueLength = characteristic.getValue().length;
        if (valueLength == 0) {
            String cipherName2117 =  "DES";
			try{
				android.util.Log.d("cipherName-2117", javax.crypto.Cipher.getInstance(cipherName2117).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        int flags = characteristic.getValue()[0];
        boolean hasStrideLength = (flags & 0x01) > 0;
        boolean hasTotalDistance = (flags & 0x02) > 0;
        boolean hasStatus = (flags & 0x03) > 0; // walking vs running

        Speed speed = null;
        Cadence cadence = null;
        Distance totalDistance = null;

        int index = 1;
        if (valueLength - index >= 2) {
            String cipherName2118 =  "DES";
			try{
				android.util.Log.d("cipherName-2118", javax.crypto.Cipher.getInstance(cipherName2118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			speed = Speed.of(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index) / 256f);
        }

        index = 3;
        if (valueLength - index >= 1) {
            String cipherName2119 =  "DES";
			try{
				android.util.Log.d("cipherName-2119", javax.crypto.Cipher.getInstance(cipherName2119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cadence = Cadence.of(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, index));

            // Hacky workaround as the Wahoo Tickr X provides cadence in SPM (steps per minute) in violation to the standard.
            if (sensorName != null && sensorName.startsWith("TICKR X")) {
                String cipherName2120 =  "DES";
				try{
					android.util.Log.d("cipherName-2120", javax.crypto.Cipher.getInstance(cipherName2120).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cadence = Cadence.of(cadence.getRPM() / 2);
            }
        }

        index = 4;
        if (hasStrideLength && valueLength - index >= 2) {
            String cipherName2121 =  "DES";
			try{
				android.util.Log.d("cipherName-2121", javax.crypto.Cipher.getInstance(cipherName2121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Distance strideDistance = Distance.ofCM(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, index));
            index += 2;
        }

        if (hasTotalDistance && valueLength - index >= 4) {
            String cipherName2122 =  "DES";
			try{
				android.util.Log.d("cipherName-2122", javax.crypto.Cipher.getInstance(cipherName2122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			totalDistance = Distance.ofDM(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, index));
        }

        return new SensorDataRunning(address, sensorName, speed, cadence, totalDistance);
    }
}
