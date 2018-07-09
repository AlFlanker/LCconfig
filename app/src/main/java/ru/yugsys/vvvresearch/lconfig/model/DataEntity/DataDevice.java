// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package ru.yugsys.vvvresearch.lconfig.model.DataEntity;


import android.nfc.Tag;
import ru.yugsys.vvvresearch.lconfig.Services.Util;

public class DataDevice
{
	private Tag currentTag;
	private String uid;
	private String techno;
	private String manufacturer;
	private String productName;
	private String dsfid;
	private String afi;
	private String memorySize;
	private String blockSize;
	private String icReference;
	private boolean basedOnTwoBytesAddress;
	private boolean MultipleReadSupported;
	private boolean MemoryExceed2048bytesSize;

    public DataDevice() {

    }

	public static boolean DecodeSystemInfoResponse(byte[] systemInfoResponse, DataDevice dataDevice) {
		DataDevice device = dataDevice;
		if (systemInfoResponse[0] == 0 && systemInfoResponse.length >= 12) {

			byte[] uid = new byte[8];
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= 8; ++i) {
				uid[i - 1] = systemInfoResponse[10 - i];
				sb.append(Util.ConvertHexByteToString(uid[i - 1]));
			}
			device.setUid(sb.toString());
			if (uid[0] == -32) {
				device.setTechno("ISO 15693");
			} else if (uid[0] == -48) {
				device.setTechno("ISO 14443");
			} else {
				device.setTechno("Unknown techno");
			}

			if (uid[1] == 2) {
				device.setManufacturer("STMicroelectronics");
			} else if (uid[1] == 4) {
				device.setManufacturer("NXP");
			} else if (uid[1] == 7) {
				device.setManufacturer("Texas Instruments");
			} else if (uid[1] == 1) {
				device.setManufacturer("Motorola");
			} else if (uid[1] == 3) {
				device.setManufacturer("Hitachi");
			} else if (uid[1] == 4) {
				device.setManufacturer("NXP");
			} else if (uid[1] == 5) {
				device.setManufacturer("Infineon");
			} else if (uid[1] == 6) {
				device.setManufacturer("Cylinc");
			} else if (uid[1] == 7) {
				device.setManufacturer("Texas Instruments");
			} else if (uid[1] == 8) {
				device.setManufacturer("Fujitsu");
			} else if (uid[1] == 9) {
				device.setManufacturer("Matsushita");
			} else if (uid[1] == 10) {
				device.setManufacturer("NEC");
			} else if (uid[1] == 11) {
				device.setManufacturer("Oki");
			} else if (uid[1] == 12) {
				device.setManufacturer("Toshiba");
			} else if (uid[1] == 13) {
				device.setManufacturer("Mitsubishi");
			} else if (uid[1] == 14) {
				device.setManufacturer("Samsung");
			} else if (uid[1] == 15) {
				device.setManufacturer("Hyundai");
			} else if (uid[1] == 16) {
				device.setManufacturer("LG");
			} else {
				device.setManufacturer("Unknown manufacturer");
			}

			if (uid[1] == 2) {
				if (uid[2] >= 4 && uid[2] <= 7) {
					device.setProductName("LRI512");
					device.setMultipleReadSupported(false);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 20 && uid[2] <= 23) {
					device.setProductName("LRI64");
					device.setMultipleReadSupported(false);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 32 && uid[2] <= 35) {
					device.setProductName("LRI2K");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 40 && uid[2] <= 43) {
					device.setProductName("LRIS2K");
					device.setMultipleReadSupported(false);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 44 && uid[2] <= 47) {
					device.setProductName("M24LR64");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= 64 && uid[2] <= 67) {
					device.setProductName("LRI1K");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 68 && uid[2] <= 71) {
					device.setProductName("LRIS64K");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= 72 && uid[2] <= 75) {
					device.setProductName("M24LR01E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 76 && uid[2] <= 79) {
					device.setProductName("M24LR16E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
					if (!device.isBasedOnTwoBytesAddress()) {
						return false;
					}
				} else if (uid[2] >= 80 && uid[2] <= 83) {
					device.setProductName("M24LR02E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= 84 && uid[2] <= 87) {
					device.setProductName("M24LR32E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
					if (!device.isBasedOnTwoBytesAddress()) {
						return false;
					}
				} else if (uid[2] >= 88 && uid[2] <= 91) {
					device.setProductName("M24LR04E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= 92 && uid[2] <= 95) {
					device.setProductName("M24LR64E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
					if (!device.isBasedOnTwoBytesAddress()) {
						return false;
					}
				} else if (uid[2] >= 96 && uid[2] <= 99) {
					device.setProductName("M24LR08E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= 100 && uid[2] <= 103) {
					device.setProductName("M24LR128E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
					if (!device.isBasedOnTwoBytesAddress()) {
						return false;
					}
				} else if (uid[2] >= 108 && uid[2] <= 111) {
					device.setProductName("M24LR256E");
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
					if (!device.isBasedOnTwoBytesAddress()) {
						return false;
					}
				} else if (uid[2] >= -8 && uid[2] <= -5) {
					device.setProductName("detected product");
					device.setBasedOnTwoBytesAddress(true);
					device.setMultipleReadSupported(true);
					device.setMemoryExceed2048bytesSize(true);
				} else {
					device.setProductName("Unknown product");
					device.setBasedOnTwoBytesAddress(false);
					device.setMultipleReadSupported(false);
					device.setMemoryExceed2048bytesSize(false);
				}

				device.setDsfid(Util.ConvertHexByteToString(systemInfoResponse[10]));
				device.setAfi(Util.ConvertHexByteToString(systemInfoResponse[11]));
				if (device.isBasedOnTwoBytesAddress()) {
					StringBuilder temp = new StringBuilder();
					temp.append(systemInfoResponse[13]).append(Util.ConvertHexByteToString(systemInfoResponse[12]));
					device.setMemorySize(temp.toString());
				} else {
					device.setMemorySize(Util.ConvertHexByteToString(systemInfoResponse[12]));
				}

				if (device.isBasedOnTwoBytesAddress()) {
					device.setBlockSize(Util.ConvertHexByteToString(systemInfoResponse[14]));
				} else {
					device.setBlockSize(Util.ConvertHexByteToString(systemInfoResponse[13]));
				}

				if (device.isBasedOnTwoBytesAddress()) {
					device.setIcReference(Util.ConvertHexByteToString(systemInfoResponse[15]));
				} else {
					device.setIcReference(Util.ConvertHexByteToString(systemInfoResponse[14]));
				}
			} else {
				device.setProductName("Unknown product");
				device.setBasedOnTwoBytesAddress(false);
				device.setMultipleReadSupported(false);
				device.setMemoryExceed2048bytesSize(false);
				device.setAfi(Util.ConvertHexByteToString(systemInfoResponse[11]));
				device.setDsfid(Util.ConvertHexByteToString(systemInfoResponse[10]));
				device.setMemorySize(Util.ConvertHexByteToString(systemInfoResponse[12]));
				device.setBlockSize(Util.ConvertHexByteToString(systemInfoResponse[13]));
				device.setIcReference(Util.ConvertHexByteToString(systemInfoResponse[14]));
			}

			return true;
		} else if (device.getTechno() == "ISO 15693") {
			device.setProductName("Unknown product");
			device.setBasedOnTwoBytesAddress(false);
			device.setMultipleReadSupported(false);
			device.setMemoryExceed2048bytesSize(false);
			device.setAfi("00 ");
			device.setDsfid("00 ");
			device.setMemorySize("3F ");
			device.setBlockSize("03 ");
			device.setIcReference("00 ");
			return true;
		} else {
			return false;
		}
	}

	public static DataDevice DecodeGetSystemInfoResponse(byte[] GetSystemInfoResponse, DataDevice dataDevice) {
		DataDevice ma = dataDevice;
		StringBuilder sb = new StringBuilder();
		//if the tag has returned a good response
		if (GetSystemInfoResponse[0] == (byte) 0x00 && GetSystemInfoResponse.length >= 12) {
			//DataDevice ma = (DataDevice)getApplication();
			for (Byte b : GetSystemInfoResponse) {
				sb.append(String.format("0x%02x; ", b));
			}
			String uidToString = "";
			sb = new StringBuilder();
			byte[] uid = new byte[8];
			// change uid format from byteArray to a String
			for (int i = 1; i <= 8; i++) {
				uid[i - 1] = GetSystemInfoResponse[10 - i];
				sb.append(Util.ConvertHexByteToString(uid[i - 1]));
			}
			uidToString = sb.toString();
			sb = new StringBuilder();

			//***** TECHNO ******
			ma.setUid(uidToString);
			if (uid[0] == (byte) 0xE0)
				ma.setTechno("ISO 15693");
			else if (uid[0] == (byte) 0xD0)
				ma.setTechno("ISO 14443");
			else
				ma.setTechno("Unknown techno");

			//***** MANUFACTURER ****
			if (uid[1] == (byte) 0x02)
				ma.setManufacturer("STMicroelectronics");
			else if (uid[1] == (byte) 0x04)
				ma.setManufacturer("NXP");
			else if (uid[1] == (byte) 0x07)
				ma.setManufacturer("Texas Instruments");
			else if (uid[1] == (byte) 0x01) //MOTOROLA (updated 20140228)
				ma.setManufacturer("Motorola");
			else if (uid[1] == (byte) 0x03) //HITASHI (updated 20140228)
				ma.setManufacturer("Hitachi");
			else if (uid[1] == (byte) 0x04) //NXP SEMICONDUCTORS
				ma.setManufacturer("NXP");
			else if (uid[1] == (byte) 0x05) //INFINEON TECHNOLOGIES (updated 20140228)
				ma.setManufacturer("Infineon");
			else if (uid[1] == (byte) 0x06) //CYLINC (updated 20140228)
				ma.setManufacturer("Cylinc");
			else if (uid[1] == (byte) 0x07) //TEXAS INSTRUMENTS TAG-IT
				ma.setManufacturer("Texas Instruments");
			else if (uid[1] == (byte) 0x08) //FUJITSU LIMITED (updated 20140228)
				ma.setManufacturer("Fujitsu");
			else if (uid[1] == (byte) 0x09) //MATSUSHITA ELECTRIC INDUSTRIAL (updated 20140228)
				ma.setManufacturer("Matsushita");
			else if (uid[1] == (byte) 0x0A) //NEC (updated 20140228)
				ma.setManufacturer("NEC");
			else if (uid[1] == (byte) 0x0B) //OKI ELECTRIC (updated 20140228)
				ma.setManufacturer("Oki");
			else if (uid[1] == (byte) 0x0C) //TOSHIBA (updated 20140228)
				ma.setManufacturer("Toshiba");
			else if (uid[1] == (byte) 0x0D) //MITSUBISHI ELECTRIC (updated 20140228)
				ma.setManufacturer("Mitsubishi");
			else if (uid[1] == (byte) 0x0E) //SAMSUNG ELECTRONICS (updated 20140228)
				ma.setManufacturer("Samsung");
			else if (uid[1] == (byte) 0x0F) //HUYNDAI ELECTRONICS (updated 20140228)
				ma.setManufacturer("Hyundai");
			else if (uid[1] == (byte) 0x10) //LG SEMICONDUCTORS (updated 20140228)
				ma.setManufacturer("LG");
			else
				ma.setManufacturer("Unknown manufacturer");

			if (uid[1] == (byte) 0x02) {
				//**** PRODUCT NAME *****
				if (uid[2] >= (byte) 0x04 && uid[2] <= (byte) 0x07) {
					ma.setProductName("LRI512");
					ma.setMultipleReadSupported(false);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x14 && uid[2] <= (byte) 0x17) {
					ma.setProductName("LRI64");
					ma.setMultipleReadSupported(false);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x20 && uid[2] <= (byte) 0x23) {
					ma.setProductName("LRI2K");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x28 && uid[2] <= (byte) 0x2B) {
					ma.setProductName("LRIS2K");
					ma.setMultipleReadSupported(false);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x2C && uid[2] <= (byte) 0x2F) {
					ma.setProductName("M24LR64");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= (byte) 0x40 && uid[2] <= (byte) 0x43) {
					ma.setProductName("LRI1K");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x44 && uid[2] <= (byte) 0x47) {
					ma.setProductName("LRIS64K");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= (byte) 0x48 && uid[2] <= (byte) 0x4B) {
					ma.setProductName("M24LR01E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x4C && uid[2] <= (byte) 0x4F) {
					ma.setProductName("M24LR16E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
					if (ma.isBasedOnTwoBytesAddress() == false)
						return null;
				} else if (uid[2] >= (byte) 0x50 && uid[2] <= (byte) 0x53) {
					ma.setProductName("M24LR02E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(false);
				} else if (uid[2] >= (byte) 0x54 && uid[2] <= (byte) 0x57) {
					ma.setProductName("M24LR32E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
					if (ma.isBasedOnTwoBytesAddress() == false)
						return null;
				} else if (uid[2] >= (byte) 0x58 && uid[2] <= (byte) 0x5B) {
					ma.setProductName("M24LR04E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= (byte) 0x5C && uid[2] <= (byte) 0x5F) {
					ma.setProductName("M24LR64E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
					if (ma.isBasedOnTwoBytesAddress() == false)
						return null;
				} else if (uid[2] >= (byte) 0x60 && uid[2] <= (byte) 0x63) {
					ma.setProductName("M24LR08E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
				} else if (uid[2] >= (byte) 0x64 && uid[2] <= (byte) 0x67) {
					ma.setProductName("M24LR128E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
					if (ma.isBasedOnTwoBytesAddress() == false)
						return null;
				} else if (uid[2] >= (byte) 0x6C && uid[2] <= (byte) 0x6F) {
					ma.setProductName("M24LR256E");
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
					if (ma.isBasedOnTwoBytesAddress() == false)
						return null;
				} else if (uid[2] >= (byte) 0xF8 && uid[2] <= (byte) 0xFB) {
					ma.setProductName("detected product");
					ma.setBasedOnTwoBytesAddress(true);
					ma.setMultipleReadSupported(true);
					ma.setMemoryExceed2048bytesSize(true);
				} else {
					ma.setProductName("Unknown product");
					ma.setBasedOnTwoBytesAddress(false);
					ma.setMultipleReadSupported(false);
					ma.setMemoryExceed2048bytesSize(false);
				}

				//*** DSFID ***
				ma.setDsfid(Util.ConvertHexByteToString(GetSystemInfoResponse[10]));

				//*** AFI ***
				ma.setAfi(Util.ConvertHexByteToString(GetSystemInfoResponse[11]));

				//*** MEMORY SIZE ***
				if (ma.isBasedOnTwoBytesAddress()) {
					sb = new StringBuilder();
					sb.append(Util.ConvertHexByteToString(GetSystemInfoResponse[13])).
							append(Util.ConvertHexByteToString(GetSystemInfoResponse[12]));
					ma.setMemorySize(sb.toString());
				} else
					ma.setMemorySize(Util.ConvertHexByteToString(GetSystemInfoResponse[12]));

				//*** BLOCK SIZE ***
				if (ma.isBasedOnTwoBytesAddress())
					ma.setBlockSize(Util.ConvertHexByteToString(GetSystemInfoResponse[14]));
				else
					ma.setBlockSize(Util.ConvertHexByteToString(GetSystemInfoResponse[13]));

				//*** IC REFERENCE ***
				if (ma.isBasedOnTwoBytesAddress())
					ma.setIcReference(Util.ConvertHexByteToString(GetSystemInfoResponse[15]));
				else
					ma.setIcReference(Util.ConvertHexByteToString(GetSystemInfoResponse[14]));
			} else {
				ma.setProductName("Unknown product");
				ma.setBasedOnTwoBytesAddress(false);
				ma.setMultipleReadSupported(false);
				ma.setMemoryExceed2048bytesSize(false);
				ma.setAfi(Util.ConvertHexByteToString(GetSystemInfoResponse[11]));
				ma.setDsfid(Util.ConvertHexByteToString(GetSystemInfoResponse[10]));
				ma.setMemorySize(Util.ConvertHexByteToString(GetSystemInfoResponse[12]));
				ma.setBlockSize(Util.ConvertHexByteToString(GetSystemInfoResponse[13]));
				ma.setIcReference(Util.ConvertHexByteToString(GetSystemInfoResponse[14]));
			}

			return ma;
        } else if ("ISO 15693".equals(ma.getTechno())) {
			ma.setProductName("Unknown product");
			ma.setBasedOnTwoBytesAddress(false);
			ma.setMultipleReadSupported(false);
			ma.setMemoryExceed2048bytesSize(false);
			ma.setAfi("00 ");
			ma.setDsfid("00 ");
			ma.setMemorySize("3F ");
			ma.setBlockSize("03 ");
			ma.setIcReference("00 ");
			return ma;
		}

		//if the tag has returned an error code
		else
			return null;

	}

    public void setCurrentTag(Tag currentTag) {
		this.currentTag = currentTag;
	}

	public Tag getCurrentTag() {
		return currentTag;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setTechno(String techno) {
		this.techno = techno;
	}

	public String getTechno() {
		return techno;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setDsfid(String dsfid) {
		this.dsfid = dsfid;
	}

	public String getDsfid() {
		return dsfid;
	}

	public void setAfi(String afi) {
		this.afi = afi;
	}

	public String getAfi() {
		return afi;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setBlockSize(String blockSize) {
		this.blockSize = blockSize;
	}

	public String getBlockSize() {
		return blockSize;
	}

	public void setIcReference(String icReference) {
		this.icReference = icReference;
	}

	public String getIcReference() {
		return icReference;
	}

	public void setBasedOnTwoBytesAddress(boolean basedOnTwoBytesAddress) {
		this.basedOnTwoBytesAddress = basedOnTwoBytesAddress;
	}

	public boolean isBasedOnTwoBytesAddress() {
		return basedOnTwoBytesAddress;
	}

	public void setMultipleReadSupported(boolean MultipleReadSupported) {
		this.MultipleReadSupported = MultipleReadSupported;
	}

	public boolean isMultipleReadSupported() {
		return MultipleReadSupported;
	}	
	
	public void setMemoryExceed2048bytesSize(boolean MemoryExceed2048bytesSize) {
		this.MemoryExceed2048bytesSize = MemoryExceed2048bytesSize;
	}

	public boolean isMemoryExceed2048bytesSize() {
		return MemoryExceed2048bytesSize;
	}	
	
}
