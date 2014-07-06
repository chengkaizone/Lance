package barcode.lance.client.handler;

import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class BarcodePreferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static final String DECODE_1D = "preferences_decode_1D";
	public static final boolean DECODE_1D_FLAG = true;
	public static final String DECODE_QR = "preferences_decode_QR";
	public static final boolean DECODE_QR_FLAG = true;
	public static final String DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
	public static final boolean DECODE_DATA_MATRIX_FLAG = true;

	public static final String CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";
	public static final boolean CUSTOM_PRODUCT_SEARCH_FLAG = true;

	public static final String REVERSE_IMAGE = "preferences_reverse_image";
	public static final boolean REVERSE_IMAGE_FLAG = false;
	public static final String PLAY_BEEP = "preferences_play_beep";
	public static final boolean PLAY_BEEP_FLAG = true;
	public static final String VIBRATE = "preferences_vibrate";
	public static final boolean VIBRATE_FLAG = false;
	public static final String COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
	public static final boolean COPY_TO_CLIPBOARD_FLAG = true;
	public static final String FRONT_LIGHT = "preferences_front_light";
	public static final boolean FRONT_LIGHT_FLAG = false;
	public static final String BULK_MODE = "preferences_bulk_mode";
	public static final boolean BULK_MODE_FLAG = false;
	public static final String REMEMBER_DUPLICATES = "preferences_remember_duplicates";
	public static final boolean REMEMBER_DUPLICATES_FLAG = false;
	public static final String SUPPLEMENTAL = "preferences_supplemental";
	public static final boolean SUPPLEMENTAL_FLAG = true;

	public static final String HELP_VERSION_SHOWN = "preferences_help_version_shown";
	public static final String NOT_OUR_RESULTS_SHOWN = "preferences_not_out_results_shown";

	private CheckBoxPreference decode1D;
	private CheckBoxPreference decodeQR;
	private CheckBoxPreference decodeDataMatrix;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen preferences = getPreferenceScreen();
		preferences.getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		decode1D = (CheckBoxPreference) preferences.findPreference(DECODE_1D);
		decodeQR = (CheckBoxPreference) preferences.findPreference(DECODE_QR);
		decodeDataMatrix = (CheckBoxPreference) preferences
				.findPreference(DECODE_DATA_MATRIX);
		disableLastCheckedPref();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		disableLastCheckedPref();
	}

	private void disableLastCheckedPref() {
		Collection<CheckBoxPreference> checked = new ArrayList<CheckBoxPreference>(
				3);
		if (decode1D.isChecked()) {
			checked.add(decode1D);
		}
		if (decodeQR.isChecked()) {
			checked.add(decodeQR);
		}
		if (decodeDataMatrix.isChecked()) {
			checked.add(decodeDataMatrix);
		}
		boolean disable = checked.size() < 2;
		for (CheckBoxPreference pref : new CheckBoxPreference[] { decode1D,
				decodeQR, decodeDataMatrix }) {
			pref.setEnabled(!(disable && checked.contains(pref)));
		}
	}
}
