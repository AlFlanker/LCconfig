// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package ru.yugsys.vvvresearch.lconfig.Services;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;


public class Util {


	//***********************************************************************/
	//* the function Format a String with the right number of digit
	//* Example : ConvertHexByteToString ("23",4) -> returns "0023"
	//* Example : ConvertHexByteToString ("54",7) -> returns "0000054"
	//***********************************************************************/
	public static String StringForceDigit (String sStringToFormat, int nbOfDigit)
	{
        StringBuilder sFormated = new StringBuilder(sStringToFormat.replaceAll(" ", ""));

        if (sFormated.length() == 4) {
            return sFormated.toString();
        } else if (sFormated.length() < nbOfDigit) {
            while (sFormated.length() != nbOfDigit)
			{
                sFormated.insert(0, "0");
			}
		}

        return sFormated.toString();
	}
	
	//***********************************************************************/
	 //* the function Convert byte value to a "2-char String" Format
	 //* Example : ConvertHexByteToString ((byte)0X0F) -> returns "0F"
    //* refactor AlexFlanker
	 //***********************************************************************/
	public static String ConvertHexByteToString (byte byteToConvert)
	 {
		 StringBuilder ConvertedByte = new StringBuilder();
		 if (byteToConvert < 0) {
			 ConvertedByte.append(Integer.toString(byteToConvert + 256, 16)).append(" ");

		} else if (byteToConvert <= 15) {
			 ConvertedByte.append("0").append(Integer.toString(byteToConvert, 16)).append(" ");

		} else {
			 ConvertedByte.append(Integer.toString(byteToConvert, 16)).append(" ");
		}

		 return ConvertedByte.toString();
	 }


	//***********************************************************************/
	//* the function Convert a "4-char String" to a two bytes format
	//* Example : "0F43" -> { 0X0F ; 0X43 }
    //* refactor by AlexFlanker
	//***********************************************************************/
	public static byte[] ConvertStringToHexBytes (String StringToConvert)
	 {
         byte[] ConvertedString = new byte[StringToConvert.length() * 2];
         int j = 0;
         for (int i = 0; i < StringToConvert.length(); i += 2) {
             ConvertedString[j++] = Byte.valueOf(StringToConvert.toUpperCase().substring(i, i + 2), 16);
         }

         return ConvertedString;
	 }

	//***********************************************************************/
	 //* the function Convert Int value to a "2 bytes Array" Format
	//*  (decimal)1876 == (hexadecimal)0754 
	 //* Example : ConvertIntTo2bytesHexaFormat (1876) -> returns {0x07, 0x54}
	 //***********************************************************************/
	public static byte[] ConvertIntTo2bytesHexaFormat (int numberToConvert)
	 {
		 byte[] ConvertedNumber = new byte[2];
		 
		 ConvertedNumber[0]= (byte)(numberToConvert/256);
		 ConvertedNumber[1]= (byte)(numberToConvert-(256*(numberToConvert/256)));
		 
		 return ConvertedNumber;
	 }
	
	//***********************************************************************/
	 //* the function Convert a "2 bytes Array" To int Format
	//*  (decimal)1876 = (hexadecimal)0754 
	 //* Example : Convert2bytesHexaFormatToInt {0x07, 0x54} -> returns 1876
	 //***********************************************************************/
	public static int Convert2bytesHexaFormatToInt (byte[] ArrayToConvert)
	 {
		 int ConvertedNumber = 0;
		 if(ArrayToConvert[1]<=-1)//<0
			 ConvertedNumber += ArrayToConvert[1]+256;
		 else
			 ConvertedNumber += ArrayToConvert[1];
		 
		 if(ArrayToConvert[0]<=-1)//<0
			 ConvertedNumber += (ArrayToConvert[0]*256)+256;
		 else
			 ConvertedNumber += ArrayToConvert[0]*256;
		 
		 return ConvertedNumber;
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
		} else if (ma.getTechno().equals("ISO 15693")) {
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

	//***********************************************************************/
    //* the function Convert hex string to bytes
    //*example 0A10 -> bytes[]{0x0A,0x10};
    // Alex Flanker
    //***********************************************************************/
    public static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;

    }

	public static  String getDevadrMSBtoLSB(String devadr) {
		if (devadr != null) {
			StringBuilder devText = new StringBuilder();
			int length = devadr.length();
			for (int i = 0; i < length; i += 2) {
				devText = devText.insert(0, devadr.substring(i, i + 2));
			}
			return devText.toString().toUpperCase();
		} else return null;
	}

}
