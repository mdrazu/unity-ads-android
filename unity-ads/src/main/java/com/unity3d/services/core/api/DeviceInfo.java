package com.unity3d.services.core.api;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.media.AudioRouting;

import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.device.DeviceError;
import com.unity3d.services.core.device.VolumeChange;
import com.unity3d.services.core.device.VolumeChangeMonitor;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DeviceInfo {
	private static final VolumeChangeMonitor volumeChangeMonitor = Utilities.getService(VolumeChangeMonitor.class);
	public enum StorageType { EXTERNAL, INTERNAL }

	@WebViewExposed
	public static void getAdvertisingTrackingId(WebViewCallback callback) {
		callback.invoke(Device.getAdvertisingTrackingId());
	}

	@WebViewExposed
	public static void getLimitAdTrackingFlag(WebViewCallback callback) {
		callback.invoke(Device.isLimitAdTrackingEnabled());
	}

	@WebViewExposed
	public static void getOpenAdvertisingTrackingId(WebViewCallback callback) {
		callback.invoke(Device.getOpenAdvertisingTrackingId());
	}

	@WebViewExposed
	public static void getLimitOpenAdTrackingFlag(WebViewCallback callback) {
		callback.invoke(Device.isLimitOpenAdTrackingEnabled());
	}

	@WebViewExposed
	public static void getApiLevel (WebViewCallback callback) {
		callback.invoke(Device.getApiLevel());
	}

	@WebViewExposed
	public static void getOsVersion (WebViewCallback callback) {
		callback.invoke(Device.getOsVersion());
	}

	@WebViewExposed
	public static void getManufacturer (WebViewCallback callback) {
		callback.invoke(Device.getManufacturer());
	}

	@WebViewExposed
	public static void getModel (WebViewCallback callback) {
		callback.invoke(Device.getModel());
	}

	@WebViewExposed
	public static void getScreenLayout (WebViewCallback callback) {
		callback.invoke(Device.getScreenLayout());
	}

	@WebViewExposed
	public static void getDisplayMetricDensity (WebViewCallback callback) {
		callback.invoke(Device.getDisplayMetricDensity());
	}

	@WebViewExposed
	public static void getScreenDensity (WebViewCallback callback) {
		callback.invoke(Device.getScreenDensity());
	}

	@WebViewExposed
	public static void getScreenWidth (WebViewCallback callback) {
		callback.invoke(Device.getScreenWidth());
	}

	@WebViewExposed
	public static void getScreenHeight (WebViewCallback callback) {
		callback.invoke(Device.getScreenHeight());
	}

	@WebViewExposed
	public static void getTimeZone(Boolean dst, WebViewCallback callback) {
		callback.invoke(TimeZone.getDefault().getDisplayName(dst, TimeZone.SHORT, Locale.US));
	}

	@WebViewExposed
	public static void getTimeZoneOffset(WebViewCallback callback) {
		callback.invoke(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000);
	}

	@WebViewExposed
	public static void getConnectionType(WebViewCallback callback) {
		callback.invoke(Device.getConnectionType());
	}

	@WebViewExposed
	public static void getNetworkType(WebViewCallback callback) {
		callback.invoke(Device.getNetworkType());
	}

	@WebViewExposed
	public static void getNetworkMetered(WebViewCallback callback) {
		callback.invoke(Device.getNetworkMetered());
	}

	@WebViewExposed
	public static void getNetworkOperator(WebViewCallback callback) {
		callback.invoke(Device.getNetworkOperator());
	}

	@WebViewExposed
	public static void getNetworkOperatorName(WebViewCallback callback) {
		callback.invoke(Device.getNetworkOperatorName());
	}

	@WebViewExposed
	public static void getNetworkCountryISO(WebViewCallback callback) {
		callback.invoke(Device.getNetworkCountryISO());
	}

	@WebViewExposed
	public static void isRooted(WebViewCallback callback) {
		callback.invoke(Device.isRooted());
	}
	
	@WebViewExposed
	public static void isAdbEnabled(WebViewCallback callback) {
		Boolean adbEnabled = Device.isAdbEnabled();
		if (adbEnabled != null) {
			callback.invoke(adbEnabled);
		} else {
			callback.error(DeviceError.COULDNT_GET_ADB_STATUS);
		}
	}

	@WebViewExposed
	public static void getPackageInfo(WebViewCallback callback) {
		if (ClientProperties.getApplicationContext() != null) {
			String appName = ClientProperties.getAppName();
			try {
				JSONObject data = Device.getPackageInfo(ClientProperties.getApplicationContext().getPackageManager());
				callback.invoke(data);
			} catch (PackageManager.NameNotFoundException e) {
				callback.error(DeviceError.APPLICATION_INFO_NOT_AVAILABLE, appName);
			} catch (JSONException e) {
				callback.error(DeviceError.JSON_ERROR, e.getMessage());
			}
		}
		else {
			callback.error(DeviceError.APPLICATION_CONTEXT_NULL);
		}
	}

	@WebViewExposed
	public static void getUniqueEventId(WebViewCallback callback) {
		callback.invoke(Device.getUniqueEventId());
	}

	@WebViewExposed
	public static void getHeadset(WebViewCallback callback) {
		callback.invoke(Device.isWiredHeadsetOn());
	}

	@WebViewExposed
	public static void getSystemProperty(String propertyName, String defaultValue, WebViewCallback callback) {
		callback.invoke(Device.getSystemProperty(propertyName, defaultValue));
	}

	@WebViewExposed
	public static void getRingerMode (WebViewCallback callback) {
		int ringerMode = Device.getRingerMode();
		if (ringerMode > -1)
			callback.invoke(ringerMode);
		else {
			switch (ringerMode) {
				case -1:
					callback.error(DeviceError.APPLICATION_CONTEXT_NULL, ringerMode);
					break;
				case -2:
					callback.error(DeviceError.AUDIOMANAGER_NULL, ringerMode);
					break;
				default:
					DeviceLog.error("Unhandled ringerMode error: " + ringerMode);
					break;

			}
		}
	}

	@WebViewExposed
	public static void getSystemLanguage(WebViewCallback callback) {
		callback.invoke(Locale.getDefault().toString());
	}

	@WebViewExposed
	public static void getDeviceVolume(Integer streamType, WebViewCallback callback) {
		int volume = Device.getStreamVolume(streamType);
		if (volume > -1) {
			callback.invoke(volume);
		}
		else {
			switch (volume) {
				case -1:
					callback.error(DeviceError.APPLICATION_CONTEXT_NULL, volume);
					break;
				case -2:
					callback.error(DeviceError.AUDIOMANAGER_NULL, volume);
					break;
				default:
					DeviceLog.error("Unhandled deviceVolume error: " + volume);
					break;
			}
		}
	}

	@WebViewExposed
	public static void getDeviceMaxVolume(Integer streamType, WebViewCallback callback) {
		int maxVolume = Device.getStreamMaxVolume(streamType);
		if (maxVolume > -1) {
			callback.invoke(maxVolume);
		}
		else {
			switch (maxVolume) {
				case -1:
					callback.error(DeviceError.APPLICATION_CONTEXT_NULL, maxVolume);
					break;
				case -2:
					callback.error(DeviceError.AUDIOMANAGER_NULL, maxVolume);
					break;
				default:
					DeviceLog.error("Unhandled deviceMaxVolume error: " + maxVolume);
					break;
			}
		}
	}

	@WebViewExposed
	public static void registerVolumeChangeListener(final Integer streamType, WebViewCallback callback) {
		volumeChangeMonitor.registerVolumeChangeListener(streamType);
		callback.invoke();
	}

	@WebViewExposed
	public static void unregisterVolumeChangeListener(final Integer streamType, WebViewCallback callback) {
		volumeChangeMonitor.unregisterVolumeChangeListener(streamType);
		callback.invoke();
	}

	@WebViewExposed
	public static void getScreenBrightness (WebViewCallback callback) {
		int screenBrightness = Device.getScreenBrightness();
		if (screenBrightness > -1)
			callback.invoke(screenBrightness);
		else {
			switch (screenBrightness) {
				case -1:
					callback.error(DeviceError.APPLICATION_CONTEXT_NULL, screenBrightness);
					break;
				default:
					DeviceLog.error("Unhandled screenBrightness error: " + screenBrightness);
					break;
			}
		}
	}

	private static StorageType getStorageTypeFromString (String storageType) {
		StorageType storage;

		try {
			storage = StorageType.valueOf(storageType);
		}
		catch (IllegalArgumentException e) {
			DeviceLog.exception("Illegal argument: " + storageType, e);
			return null;
		}

		return storage;
	}

	private static File getFileForStorageType (StorageType storageType) {
		switch (storageType) {
			case INTERNAL:
				return ClientProperties.getApplicationContext().getCacheDir();
			case EXTERNAL:
				return ClientProperties.getApplicationContext().getExternalCacheDir();
			default:
				DeviceLog.error("Unhandled storagetype: " + storageType);
				return null;
		}
	}

	@WebViewExposed
	public static void getFreeSpace (String storageType, WebViewCallback callback) {
		StorageType storage = getStorageTypeFromString(storageType);

		if (storage == null) {
			callback.error(DeviceError.INVALID_STORAGETYPE, storageType);
			return;
		}

		long space = Device.getFreeSpace(getFileForStorageType(storage));
		if (space > -1)
			callback.invoke(space);
		else
			callback.error(DeviceError.COULDNT_GET_STORAGE_LOCATION, space);
	}

	@WebViewExposed
	public static void getTotalSpace (String storageType, WebViewCallback callback) {
		StorageType storage = getStorageTypeFromString(storageType);

		if (storage == null) {
			callback.error(DeviceError.INVALID_STORAGETYPE, storageType);
			return;
		}

		long space = Device.getTotalSpace(getFileForStorageType(storage));
		if (space > -1)
			callback.invoke(space);
		else
			callback.error(DeviceError.COULDNT_GET_STORAGE_LOCATION, space);
	}

	@WebViewExposed
	public static void getBatteryLevel(WebViewCallback callback) {
		callback.invoke(Device.getBatteryLevel());
	}

	@WebViewExposed
	public static void getBatteryStatus(WebViewCallback callback) {
		callback.invoke(Device.getBatteryStatus());
	}

	@WebViewExposed
	public static void getFreeMemory(WebViewCallback callback) {
		callback.invoke(Device.getFreeMemory());
	}

	@WebViewExposed
	public static void getTotalMemory(WebViewCallback callback) {
		callback.invoke(Device.getTotalMemory());
	}

	@WebViewExposed
	public static void getGLVersion (WebViewCallback callback) {
		String glVersion = Device.getGLVersion();

		if (glVersion != null) {
			callback.invoke(glVersion);
		}
		else {
			callback.error(DeviceError.COULDNT_GET_GL_VERSION);
		}
	}

	@WebViewExposed
	public static void getApkDigest (WebViewCallback callback) {
		try {
			callback.invoke(Device.getApkDigest());
		} catch(Exception e) {
			callback.error(DeviceError.COULDNT_GET_DIGEST, e.toString());
		}
	}

	@WebViewExposed
	public static void getCertificateFingerprint (WebViewCallback callback) {
		String fingerprint = Device.getCertificateFingerprint();

		if (fingerprint != null) {
			callback.invoke(fingerprint);
		}
		else {
			callback.error(DeviceError.COULDNT_GET_FINGERPRINT);
		}
	}

	@WebViewExposed
	public static void getBoard (WebViewCallback callback) {
		callback.invoke(Device.getBoard());
	}

	@WebViewExposed
	public static void getBootloader (WebViewCallback callback) {
		callback.invoke(Device.getBootloader());
	}

	@WebViewExposed
	public static void getBrand (WebViewCallback callback) {
		callback.invoke(Device.getBrand());
	}

	@WebViewExposed
	public static void getDevice (WebViewCallback callback) {
		callback.invoke(Device.getDevice());
	}

	@WebViewExposed
	public static void getHardware (WebViewCallback callback) {
		callback.invoke(Device.getHardware());
	}

	@WebViewExposed
	public static void getHost (WebViewCallback callback) {
		callback.invoke(Device.getHost());
	}

	@WebViewExposed
	public static void getProduct (WebViewCallback callback) {
		callback.invoke(Device.getProduct());
	}

	@WebViewExposed
	public static void getFingerprint(WebViewCallback callback) {
		callback.invoke(Device.getFingerprint());
	}

	@WebViewExposed
	public static void getSupportedAbis (WebViewCallback callback) {
		JSONArray abis = new JSONArray();

		for (String abi : Device.getSupportedAbis()) {
			abis.put(abi);
		}

		callback.invoke(abis);
	}

	@WebViewExposed
	public static void getSensorList (WebViewCallback callback) {
		JSONArray sensors = new JSONArray();
		List<Sensor> sensorList = Device.getSensorList();

		if (sensorList != null) {
			for (Sensor sensor : sensorList) {
				JSONObject sensorInfo = new JSONObject();
				try {
					sensorInfo.put("name", sensor.getName());
					sensorInfo.put("type", sensor.getType());
					sensorInfo.put("vendor", sensor.getVendor());
					sensorInfo.put("maximumRange", sensor.getMaximumRange());
					sensorInfo.put("power", sensor.getPower());
					sensorInfo.put("version", sensor.getVersion());
					sensorInfo.put("resolution", sensor.getResolution());
					sensorInfo.put("minDelay", sensor.getMinDelay());
				}
				catch (JSONException e) {
					callback.error(DeviceError.JSON_ERROR, e.getMessage());
					return;
				}

				sensors.put(sensorInfo);
			}
		}

		callback.invoke(sensors);
	}

	@WebViewExposed
	public static void getProcessInfo (WebViewCallback callback) {
		JSONObject retObj = new JSONObject();
		Map<String, String> processInfo = Device.getProcessInfo();

		if (processInfo != null) {
			try {
				if (processInfo.containsKey("stat")) {
					retObj.put("stat", processInfo.get("stat"));
				}
				if (processInfo.containsKey("uptime")) {
					retObj.put("uptime", processInfo.get("uptime"));
				}
			}
			catch (Exception e) {
				DeviceLog.exception("Error while constructing process info", e);
			}
		}

		callback.invoke(retObj);
	}

	@WebViewExposed
	public static void isUSBConnected(WebViewCallback callback) {
		callback.invoke(Device.isUSBConnected());
	}

	@WebViewExposed
	public static void getCPUCount(WebViewCallback callback) {
		callback.invoke(Device.getCPUCount());
	}

	@WebViewExposed
	public static void getUptime(WebViewCallback callback) {
		callback.invoke(Device.getUptime());
	}

	@WebViewExposed
	public static void getElapsedRealtime(WebViewCallback callback) {
		callback.invoke(Device.getElapsedRealtime());
	}

	@WebViewExposed
	public static void getBuildId(WebViewCallback callback) {
		callback.invoke(Device.getBuildId());
	}

	@WebViewExposed
	public static void getBuildVersionIncremental(WebViewCallback callback) {
		callback.invoke(Device.getBuildVersionIncremental());
	}
}

