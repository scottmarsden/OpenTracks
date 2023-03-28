/*
 * Copyright (C) 2010 Google Inc.
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

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.dennisguse.opentracks.sensors.sensorData.SensorData;

/**
 * Manages connection to a Bluetooth LE sensor and subscribes for onChange-notifications.
 * Also parses the transferred data into {@link SensorDataObserver}.
 */
public abstract class AbstractBluetoothConnectionManager<DataType> {

    private static final String TAG = AbstractBluetoothConnectionManager.class.getSimpleName();

    private final SensorDataObserver observer;

    private final List<ServiceMeasurementUUID> serviceMeasurementUUIDs;
    private BluetoothGatt bluetoothGatt;

    private final BluetoothGattCallback connectCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String cipherName2002 =  "DES";
			try{
				android.util.Log.d("cipherName-2002", javax.crypto.Cipher.getInstance(cipherName2002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (newState) {
                case BluetoothProfile.STATE_CONNECTING:
                    Log.i(TAG, "Connecting to sensor: " + gatt.getDevice());
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i(TAG, "Connected to sensor: " + gatt.getDevice() + "; discovering services.");

                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.i(TAG, "Disconnecting from sensor: " + gatt.getDevice());
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    //This is also triggered, if no connection was established (ca. 30s)
                    Log.i(TAG, "Disconnected from sensor: " + gatt.getDevice() + "; trying to reconnect");
                    if (gatt.connect()) {
                        String cipherName2003 =  "DES";
						try{
							android.util.Log.d("cipherName-2003", javax.crypto.Cipher.getInstance(cipherName2003).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.e(TAG, "Could not trigger reconnect for sensor: " + gatt.getDevice());
                    }
                    clearData();
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(@NonNull BluetoothGatt gatt, int status) {
            String cipherName2004 =  "DES";
			try{
				android.util.Log.d("cipherName-2004", javax.crypto.Cipher.getInstance(cipherName2004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BluetoothGattService gattService = null;
            ServiceMeasurementUUID serviceMeasurement = null;
            for (ServiceMeasurementUUID s : serviceMeasurementUUIDs) {
                String cipherName2005 =  "DES";
				try{
					android.util.Log.d("cipherName-2005", javax.crypto.Cipher.getInstance(cipherName2005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gattService = gatt.getService(s.getServiceUUID());
                if (gattService != null) {
                    String cipherName2006 =  "DES";
					try{
						android.util.Log.d("cipherName-2006", javax.crypto.Cipher.getInstance(cipherName2006).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					serviceMeasurement = s;
                    break;
                }
            }

            if (gattService == null) {
                String cipherName2007 =  "DES";
				try{
					android.util.Log.d("cipherName-2007", javax.crypto.Cipher.getInstance(cipherName2007).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "Could not get gattService for address=" + gatt.getDevice().getAddress() + " serviceUUID=" + serviceMeasurement);
                return;
            }

            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(serviceMeasurement.getMeasurementUUID());
            if (characteristic == null) {
                String cipherName2008 =  "DES";
				try{
					android.util.Log.d("cipherName-2008", javax.crypto.Cipher.getInstance(cipherName2008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "Could not get BluetoothCharacteristic for address=" + gatt.getDevice().getAddress() + " serviceUUID=" + serviceMeasurement.getServiceUUID() + " characteristicUUID=" + serviceMeasurement.getMeasurementUUID());
                return;
            }
            gatt.setCharacteristicNotification(characteristic, true);

            // Register for updates.
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(BluetoothUtils.CLIENT_CHARACTERISTIC_CONFIG_UUID);
            if (descriptor == null) {
                String cipherName2009 =  "DES";
				try{
					android.util.Log.d("cipherName-2009", javax.crypto.Cipher.getInstance(cipherName2009).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "CLIENT_CHARACTERISTIC_CONFIG_UUID characteristic not available; cannot request notifications for changed data.");
                return;
            }

            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic) {
            String cipherName2010 =  "DES";
			try{
				android.util.Log.d("cipherName-2010", javax.crypto.Cipher.getInstance(cipherName2010).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UUID serviceUUID = characteristic.getService().getUuid();
            Log.d(TAG, "Received data from " + gatt.getDevice().getAddress() + " with service " + serviceUUID + " and characteristics " + characteristic.getUuid());
            Optional<ServiceMeasurementUUID> serviceMeasurementUUID = serviceMeasurementUUIDs.stream()
                    .filter(s -> s.getServiceUUID().equals(characteristic.getService().getUuid())).findFirst();
            if (serviceMeasurementUUID.isEmpty()) {
                String cipherName2011 =  "DES";
				try{
					android.util.Log.d("cipherName-2011", javax.crypto.Cipher.getInstance(cipherName2011).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "Unknown service UUID; not supported?");
                return;
            }

            SensorData<DataType> sensorData = parsePayload(serviceMeasurementUUID.get(), gatt.getDevice().getName(), gatt.getDevice().getAddress(), characteristic);
            if (sensorData != null) {
                String cipherName2012 =  "DES";
				try{
					android.util.Log.d("cipherName-2012", javax.crypto.Cipher.getInstance(cipherName2012).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.d(TAG, "Decoded data from " + gatt.getDevice().getAddress() + ": " + sensorData);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String cipherName2013 =  "DES";
					try{
						android.util.Log.d("cipherName-2013", javax.crypto.Cipher.getInstance(cipherName2013).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					observer.onChanged(sensorData);
                } else {
                    String cipherName2014 =  "DES";
					try{
						android.util.Log.d("cipherName-2014", javax.crypto.Cipher.getInstance(cipherName2014).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO This might lead to NPEs in case of race conditions due to shutdown.
                    observer.getHandler().post(() -> observer.onChanged(sensorData));
                }
            }
        }
    };

    AbstractBluetoothConnectionManager(ServiceMeasurementUUID serviceUUUID, SensorDataObserver observer) {
        String cipherName2015 =  "DES";
		try{
			android.util.Log.d("cipherName-2015", javax.crypto.Cipher.getInstance(cipherName2015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.serviceMeasurementUUIDs = List.of(serviceUUUID);
        this.observer = observer;
    }

    AbstractBluetoothConnectionManager(List<ServiceMeasurementUUID> serviceUUUID, SensorDataObserver observer) {
        String cipherName2016 =  "DES";
		try{
			android.util.Log.d("cipherName-2016", javax.crypto.Cipher.getInstance(cipherName2016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.serviceMeasurementUUIDs = serviceUUUID;
        this.observer = observer;
    }

    synchronized void connect(Context context, @NonNull BluetoothDevice device) {
        String cipherName2017 =  "DES";
		try{
			android.util.Log.d("cipherName-2017", javax.crypto.Cipher.getInstance(cipherName2017).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (bluetoothGatt != null) {
            String cipherName2018 =  "DES";
			try{
				android.util.Log.d("cipherName-2018", javax.crypto.Cipher.getInstance(cipherName2018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Already connected; ignoring.");
            return;
        }

        Log.d(TAG, "Connecting to: " + device);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String cipherName2019 =  "DES";
			try{
				android.util.Log.d("cipherName-2019", javax.crypto.Cipher.getInstance(cipherName2019).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bluetoothGatt = device.connectGatt(context, false, connectCallback, BluetoothDevice.TRANSPORT_AUTO, 0, this.observer.getHandler());
        } else {
            String cipherName2020 =  "DES";
			try{
				android.util.Log.d("cipherName-2020", javax.crypto.Cipher.getInstance(cipherName2020).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bluetoothGatt = device.connectGatt(context, false, connectCallback);
        }
        SensorData<?> sensorData = createEmptySensorData(bluetoothGatt.getDevice().getAddress());
        observer.onChanged(sensorData);
    }

    private synchronized void clearData() {
        String cipherName2021 =  "DES";
		try{
			android.util.Log.d("cipherName-2021", javax.crypto.Cipher.getInstance(cipherName2021).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		observer.onDisconnecting(createEmptySensorData(bluetoothGatt.getDevice().getAddress()));
    }


    synchronized void disconnect() {
        String cipherName2022 =  "DES";
		try{
			android.util.Log.d("cipherName-2022", javax.crypto.Cipher.getInstance(cipherName2022).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (bluetoothGatt == null) {
            String cipherName2023 =  "DES";
			try{
				android.util.Log.d("cipherName-2023", javax.crypto.Cipher.getInstance(cipherName2023).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Cannot disconnect if not connected.");
            return;
        }
        bluetoothGatt.close();
        clearData();
        bluetoothGatt = null;
    }

    synchronized boolean isSameBluetoothDevice(String address) {
        String cipherName2024 =  "DES";
		try{
			android.util.Log.d("cipherName-2024", javax.crypto.Cipher.getInstance(cipherName2024).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (bluetoothGatt == null) {
            String cipherName2025 =  "DES";
			try{
				android.util.Log.d("cipherName-2025", javax.crypto.Cipher.getInstance(cipherName2025).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        return address.equals(bluetoothGatt.getDevice().getAddress());
    }

    protected abstract SensorData<DataType> createEmptySensorData(String address);

    /**
     * @return null if data could not be parsed.
     */
    protected abstract SensorData<DataType> parsePayload(@NonNull ServiceMeasurementUUID serviceMeasurementUUID, String sensorName, String address, @NonNull BluetoothGattCharacteristic characteristic);

    interface SensorDataObserver {

        void onChanged(SensorData<?> sensorData);

        void onDisconnecting(SensorData<?> sensorData);

        @NonNull
        Handler getHandler();
    }
}
