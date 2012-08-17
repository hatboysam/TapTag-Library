package com.taptag.beta.nfc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.taptag.beta.vendor.Vendor;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class NFCActions {

	private static final String TAG = "TapTag";
	private static final String TAG_MIME = "application/com.taptag.tag";

	/**
	 * Convert a string to a writeable NdefMessage (single NdefRecord)
	 * @param toTranslate
	 * @return
	 */
	public static NdefMessage stringAsNdef(String toTranslate) {
		byte[] stringBytes = toTranslate.getBytes();
		NdefRecord stringRecord = recordFromByteArray(stringBytes);
		return new NdefMessage(new NdefRecord[] { stringRecord });
	}

	/**
	 * Overloaded.
	 * Convert a series of strings to a multi-NdefRecord NdefMessage
	 * @param strings
	 * @return
	 */
	public static NdefMessage stringAsNdef(String... strings) {
		int numRecords = strings.length;
		NdefRecord[] allRecords = new NdefRecord[numRecords];
		for (int i = 0; i < numRecords; i++) {
			byte[] stringBytes = strings[i].getBytes();
			NdefRecord stringRecord = recordFromByteArray(stringBytes);
			allRecords[i] = stringRecord;
		}
		return new NdefMessage(allRecords);
	}

	/**
	 * Convert Vendor to NdefMessage for writing to Tag
	 * @param vendor
	 * @return
	 */
	public static NdefMessage vendorAsNdef(Vendor vendor) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		NdefRecord[] singleRecord = new NdefRecord[1];
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(vendor);
			byte[] vendorBytes = bos.toByteArray();
			NdefRecord vendorRecord = recordFromByteArray(vendorBytes);
			singleRecord[0] = vendorRecord;
			out.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (new NdefMessage(singleRecord));
	}

	public static Vendor vendorFromNdef(NdefMessage message) {
		NdefRecord[] records = message.getRecords();
		NdefRecord vendorRecord = records[0];
		ByteArrayInputStream bis = new ByteArrayInputStream(vendorRecord.getPayload());
		Vendor vendor = new Vendor();
		try {
			ObjectInput in = new ObjectInputStream(bis);
			Object object = in.readObject();
			vendor = (Vendor) object;
			in.close();
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return vendor;
	}

	/**
	 * Create an NDEF record with TapTag's MIME type from a byte array
	 * @param bytes
	 * @return
	 */
	public static NdefRecord recordFromByteArray(byte[] bytes) {
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, TAG_MIME.getBytes(), new byte[] {}, bytes);
		return record;
	}

	public static boolean writeNdefToTag(Tag tag, NdefMessage message) {
		Ndef ndef = Ndef.get(tag);
		try {
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					Log.i(TAG, "This tag is read only, sorry!");
					return false;
				}
				if (ndef.getMaxSize() < message.toByteArray().length) {
					Log.i(TAG, "The message is too big!");
					return false;
				}
				ndef.writeNdefMessage(message);
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						Log.i(TAG, "Formatted Tag and Wrote Message");
						return true;
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
						return false;
					}
				} else {
					Log.i(TAG, "NDEF not supported, it seems");
					return false;
				}
			}
		} catch (Exception e) {
			Log.e("TagTap", e.getMessage());
			return false;
		}
	}

	/**
	 * Overloaded.
	 * Write a tag with a variable number of records
	 * @param tag
	 * @param messages
	 * @return
	 */
	public static boolean writeTag(Tag tag, String... messages) {
		NdefMessage message = NFCActions.stringAsNdef(messages);
		return writeNdefToTag(tag, message);
	}

	public static boolean writeTag(Tag tag, Vendor vendor) {
		NdefMessage message = NFCActions.vendorAsNdef(vendor);
		return writeNdefToTag(tag, message);
	}

	/**
	 * Copied straight from Google Sticky Notes
	 * @param intent
	 * @return
	 */
	public static NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] {
						record
				});
				msgs = new NdefMessage[] {
						msg
				};
			}
		} else {
			Log.e(TAG, "Unknown intent");
		}
		return msgs;
	}

	/**
	 * Get the message from a tag known to have a single message
	 * @param intent
	 * @return
	 */
	public static NdefMessage getFirstMessage(Intent intent) {
		return (getNdefMessages(intent))[0];
	}

}
