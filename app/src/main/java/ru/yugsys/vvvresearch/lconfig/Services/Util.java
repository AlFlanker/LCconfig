// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package ru.yugsys.vvvresearch.lconfig.Services;
import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


public class Util {


	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String castHexKeyboard (String sInput)
	{
        StringBuilder sOutput = new StringBuilder();
		
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && cInput[i] != '5' && 
				cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' && cInput[i] != 'A' && cInput[i] != 'B' && 
				cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E')
			{
				cInput[i] = 'F';
			}
            sOutput.append(cInput[i]);
        }

        return sOutput.toString();
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static boolean checkDataHexa (String sInput)
	{
		boolean checkedValue = true;
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && cInput[i] != '5' && 
				cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' && cInput[i] != 'A' && cInput[i] != 'B' && 
				cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E' && cInput[i] != 'F')
			{
				checkedValue=  false;
			}
		}
		return checkedValue;		
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String checkAndChangeDataHexa (String sInput)
	{
		String CheckedAndChangedValue = "";
		sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] == '0' || cInput[i] == '1' || cInput[i] == '2' || cInput[i] == '3' || 
				cInput[i] == '4' || cInput[i] == '5' ||	cInput[i] == '6' || cInput[i] == '7' || 
				cInput[i] == '8' || cInput[i] == '9' || cInput[i] == 'A' || cInput[i] == 'B' || 
				cInput[i] == 'C' || cInput[i] == 'D' || cInput[i] == 'E' || cInput[i] == 'F')
			{
				CheckedAndChangedValue +=  cInput[i];
			}
		}
		return CheckedAndChangedValue;		
	}
	
	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static boolean checkFileName (String sInput)
	{
		boolean checkedValue = true;
		//sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] != '0' && cInput[i] != '1' && cInput[i] != '2' && cInput[i] != '3' && cInput[i] != '4' && 
					cInput[i] != '5' &&	cInput[i] != '6' && cInput[i] != '7' && cInput[i] != '8' && cInput[i] != '9' &&
					cInput[i] != 'a' && cInput[i] != 'b' && cInput[i] != 'c' && cInput[i] != 'd' && cInput[i] != 'e' && 
					cInput[i] != 'f' && cInput[i] != 'g' && cInput[i] != 'h' && cInput[i] != 'i' && cInput[i] != 'j' && 
					cInput[i] != 'k' && cInput[i] != 'l' && cInput[i] != 'm' && cInput[i] != 'n' && cInput[i] != 'o' && 
					cInput[i] != 'p' && cInput[i] != 'q' && cInput[i] != 'r' && cInput[i] != 's' && cInput[i] != 't' &&
					cInput[i] != 'u' && cInput[i] != 'v' && cInput[i] != 'w' && cInput[i] != 'x' && cInput[i] != 'y' && 
					cInput[i] != 'z' &&				
					cInput[i] != 'A' && cInput[i] != 'B' && cInput[i] != 'C' && cInput[i] != 'D' && cInput[i] != 'E' && 
					cInput[i] != 'F' && cInput[i] != 'G' && cInput[i] != 'H' && cInput[i] != 'I' && cInput[i] != 'J' && 
					cInput[i] != 'K' && cInput[i] != 'L' && cInput[i] != 'M' && cInput[i] != 'N' && cInput[i] != 'O' && 
					cInput[i] != 'P' && cInput[i] != 'Q' && cInput[i] != 'R' && cInput[i] != 'S' && cInput[i] != 'T' &&
					cInput[i] != 'U' && cInput[i] != 'V' && cInput[i] != 'W' && cInput[i] != 'X' && cInput[i] != 'Y' && 
					cInput[i] != 'Z' &&
					cInput[i] != '.' && cInput[i] != '_')
			{
				checkedValue=  false;
			}
		}
		return checkedValue;		
	}

	//***********************************************************************/
	//* the function cast a String to hexa character only
	//* when a character is not hexa it's replaced by '0'
	//* Example : ConvertHexByteToString ("AZER") -> returns "AFEF"
	//* Example : ConvertHexByteToString ("12l./<4") -> returns "12FFFF4"
	//***********************************************************************/
	public static String checkAndChangeFileName (String sInput)
	{
        StringBuilder CheckedAndChangedName = new StringBuilder();
		//sInput = sInput.toUpperCase();
		char[] cInput = sInput.toCharArray();
		
		for(int i=0; i<sInput.length();i++)
		{
			if( cInput[i] == '0' || cInput[i] == '1' || cInput[i] == '2' || cInput[i] == '3' || cInput[i] == '4' || 
				cInput[i] == '5' ||	cInput[i] == '6' || cInput[i] == '7' || cInput[i] == '8' || cInput[i] == '9' ||
				cInput[i] == 'a' || cInput[i] == 'b' || cInput[i] == 'c' || cInput[i] == 'd' || cInput[i] == 'e' || 
				cInput[i] == 'f' || cInput[i] == 'g' || cInput[i] == 'h' || cInput[i] == 'i' || cInput[i] == 'j' || 
				cInput[i] == 'k' || cInput[i] == 'l' || cInput[i] == 'm' || cInput[i] == 'n' || cInput[i] == 'o' || 
				cInput[i] == 'p' || cInput[i] == 'q' || cInput[i] == 'r' || cInput[i] == 's' || cInput[i] == 't' ||
				cInput[i] == 'u' || cInput[i] == 'v' || cInput[i] == 'w' || cInput[i] == 'x' || cInput[i] == 'y' || 
				cInput[i] == 'z' ||				
				cInput[i] == 'A' || cInput[i] == 'B' || cInput[i] == 'C' || cInput[i] == 'D' || cInput[i] == 'E' || 
				cInput[i] == 'F' || cInput[i] == 'G' || cInput[i] == 'H' || cInput[i] == 'I' || cInput[i] == 'J' || 
				cInput[i] == 'K' || cInput[i] == 'L' || cInput[i] == 'M' || cInput[i] == 'N' || cInput[i] == 'O' || 
				cInput[i] == 'P' || cInput[i] == 'Q' || cInput[i] == 'R' || cInput[i] == 'S' || cInput[i] == 'T' ||
				cInput[i] == 'U' || cInput[i] == 'V' || cInput[i] == 'W' || cInput[i] == 'X' || cInput[i] == 'Y' || 
				cInput[i] == 'Z' ||
				cInput[i] == '.' || cInput[i] == '_')
			{
                CheckedAndChangedName.append(cInput[i]);
			}
		}
        return CheckedAndChangedName.toString();
	}	
	
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
	 //* the function Convert byte Array to a "String" Formated with spaces
	 //* Example : ConvertHexByteArrayToString { 0X0F ; 0X43 } -> returns "0F 43"
	//* Refactor Alex Flanker
	 //***********************************************************************/
	public static String ConvertHexByteArrayToString (byte[] byteArrayToConvert)
	 {
		 StringBuilder ConvertedByte = new StringBuilder();
		 for(int i=0;i<byteArrayToConvert.length;i++)
		 {
			 if (byteArrayToConvert[i] < 0) {
				 ConvertedByte.append(Integer.toString(byteArrayToConvert[i] + 256, 16));
			} else if (byteArrayToConvert[i] <= 15) {
				 ConvertedByte.append("0").append(Integer.toString(byteArrayToConvert[i], 16));
			} else {
				 ConvertedByte.append(Integer.toString(byteArrayToConvert[i], 16));
			}		
		 }

		 return ConvertedByte.toString();
	 }
	
	//***********************************************************************/
	 //* the function verify and convert the start address from the EditText
	 //* in order to not read out of memory range and code String on 4chars.
	 //* Example : FormatStringAddressStart ("0F") -> returns "000F"
	 //* Example : FormatStringAddressStart ("FFFF") -> returns "07FF"
	 //***********************************************************************/
	public static String FormatStringAddressStart (String stringToFormat, DataDevice ma)
	{
		String stringFormated = stringToFormat;
		stringFormated = StringForceDigit(stringToFormat,4);
		
		if (stringFormated.length() > 4)
		{
			stringFormated = ma.getMemorySize().replace(" ", "");
		}

        int iAddressStart = Integer.valueOf(stringFormated, 16);
        int iAddresStartMax = Integer.valueOf(StringForceDigit(ma.getMemorySize(), 4), 16);


		if(iAddressStart > iAddresStartMax)
		{
			iAddressStart = iAddresStartMax;
		}
			
		stringFormated = ConvertIntToHexFormatString(iAddressStart);
		
		
		return stringFormated.toUpperCase();
	}
	
	//***********************************************************************/
	//* this function give the right format for the 4 EditText to fill in
	//* the screen BASICWRITE
	//***********************************************************************/
	public static String FormatValueByteWrite (String stringToFormat)
	{
		String stringFormated = stringToFormat;
		stringFormated = StringForceDigit(stringToFormat,2);
		stringFormated = castHexKeyboard(stringFormated);
		return stringFormated.toUpperCase();
	}
	
	//***********************************************************************/
	//* the function convert an Int value to a String with Hexadecimal format
	//* Example : ConvertIntToHexFormatString (2047) -> returns "7FF"
    //* Refactor AlexFlanker
	//***********************************************************************/
	public static String ConvertIntToHexFormatString (int iNumberToConvert)
	{

        return Integer.toHexString(iNumberToConvert);
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
	//* the function Convert a "4-char String" to a X bytes format
	//* Example : "0F43" -> { 0X0F ; 0X43 }
	//***********************************************************************/
	public static byte[] ConvertStringToHexBytesArray (String StringToConvert)
	 {
			StringToConvert = StringToConvert.toUpperCase(); 
			StringToConvert = StringToConvert.replaceAll(" ", "");
			char[]CharArray = StringToConvert.toCharArray();
			char[] Char = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			int result = 0;
			byte[] ConvertedString = new byte[StringToConvert.length()/2];
			int iStringLen = (StringToConvert.length());
					
			for(int i=0;i <iStringLen ; i++ )
			{				 
				 for(int j=0; j<=15 ; j++)
				 {
					 if(CharArray[i] == Char[j])
					 {
						 if(i%2 == 1)
						 {
							 result = result + j;
							 j=15;
						 }
							 
						 else if (i%2 == 0)
						 {
							 result = result + j*16;
							 j = 15;
						 }
							 
					 }
				 }
				 if (i%2 == 1) 
				 {
					 ConvertedString[i/2]= (byte)result;
					 result = 0;
				 }
			}			
			  
			return ConvertedString;
	 }	
	
	//***********************************************************************/
	 //* the function Convert a "4-char String" to a two bytes format
	 //* Example : "43" -> { 0X43 }
	 //***********************************************************************/
	public static byte ConvertStringToHexByte (String StringToConvert)
	 {
		StringToConvert = StringToConvert.toUpperCase(); 
		char[]CharArray = StringToConvert.toCharArray();
		 char[] Char = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		 int result = 0;
		 for(int i=0;i <= 1 ; i++ )
		 { 
			 for(int j=0; j<=15 ; j++)
			 {
				 if(CharArray[i] == Char[j])
				 {
					 if(i == 1)
					 {
						 result = result + j;
						 j=15;
					 }
						 
					 else if (i==0)
					 {
						 result = result + j*16;
						 j = 15;
					 } 
				 }
			 }
		 }
		 return (byte)result;
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
	//* the function Convert Fields of Object to byte array
	// Alex Flanker
	//***********************************************************************/
	public static byte[] Object2ByteArray(Device dev) throws IllegalAccessException, IOException, NoSuchFieldException {
		Field field;
		StringBuilder sb = new StringBuilder();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] raw=new byte[8];
		field = Device.class.getField("type");
		String s = field.get(dev).toString();
		sb.append(s);
		while (sb.length() < 5) {
			sb.append((char) 0x00);
		}
		byteArrayOutputStream.write(sb.toString().getBytes());
		field = Device.class.getField("isOTTA");
		byteArrayOutputStream.write((field.get(dev).equals(Boolean.TRUE) ? 1 : 0));
		field = Device.class.getField("eui");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("appeui");
		//raw = new BigInteger(field.get(dev).toString(),16).toByteArray();
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("appkey");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("nwkid");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("devadr");
		byteArrayOutputStream.write(new BigInteger(field.get(dev).toString(),16).toByteArray());
		field = Device.class.getField("nwkskey");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("appskey");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));

		field = Device.class.getField("Latitude");
		float f = Float.parseFloat(String.valueOf(field.get(dev)));
		byteArrayOutputStream.write(ByteBuffer.allocate(4).order((ByteOrder.LITTLE_ENDIAN)).putFloat(f).array());



		field = Device.class.getField("Longitude");
		f = Float.parseFloat(String.valueOf(field.get(dev)));
		;
		byteArrayOutputStream.write(ByteBuffer.allocate(4).order((ByteOrder.LITTLE_ENDIAN)).putFloat(f).array());

		field = Device.class.getField("outType");
		s = field.get(dev).toString();
		sb = new StringBuilder();
		sb.append(s);
		while (sb.length() < 5) {
			sb.append((char) 0x00);
		}
		byteArrayOutputStream.write(sb.toString().getBytes());
		field = Device.class.getField("kV");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));
		field = Device.class.getField("kI");
		byteArrayOutputStream.write(hexToBytes(field.get(dev).toString()));

		return byteArrayOutputStream.toByteArray();
	}


	String getStringAchi(String s) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < s.length(); i += 2) {
			String str = s.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();

	}

	//***********************************************************************/
	//* the function Convert String to Device
	// Alex Flanker
	//***********************************************************************/
    public static Device decodeByteList(String s) {
		String srt = s.replace(" ", "");
		Device d = new Device();
        d.type = srt.substring(0, 10);
        //d.isOTAA = srt.substring(10,12);
		d.eui = srt.substring(12, 28);
		d.appeui = srt.substring(28, 44);
        d.appkey = srt.substring(44, 76);
        d.nwkid = srt.substring(76, 84);
		d.devadr = srt.substring(84, 92);
		d.nwkskey = srt.substring(92, 124);
		d.appskey = srt.substring(124, 156);
		d.setLatitude(Float.intBitsToFloat((int)Long.parseLong(srt.substring(156, 164), 16)));
		d.setLongitude(Float.intBitsToFloat((int) Long.parseLong(srt.substring(164, 172), 16)));
        d.outType = srt.substring(172, 182);
		d.kV = srt.substring(182,238);
		d.kI = srt.substring(238,242);
		return d;
	}
	//***********************************************************************/
	//* the function Convert raw byte[] to Device
	// Alex Flanker
	//***********************************************************************/
	public static Device decodeByteArrayToDevice(byte[] raw) throws IllegalAccessException, IOException {
		Device device = new Device();
		byte[] buf;
		StringBuilder stringBuilder = new StringBuilder();
		Field[] fields = Device.class.getFields();
		for (Field field : fields) {
			if (field.getName().equals("type")) {
				buf = new byte[5];
				System.arraycopy(raw, 0, buf, 0, 5);
				field.set(device, new String(buf, StandardCharsets.UTF_8));
			}
			if (field.getName().equals("isOTTA")) {
				buf = new byte[1];
				boolean isotta;
				System.arraycopy(raw, 5, buf, 0, 1);
				if (buf[0] > 0) {
					isotta = true;
				} else isotta = false;
				field.set(device, isotta);

			}
			if (field.getName().equals("eui")) {
				buf = new byte[8];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 6, buf, 0, 8);
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("appeui")) {
				buf = new byte[8];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 14, buf, 0, 8);
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("appkey")) {
				buf = new byte[16];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 22, buf, 0, 16);
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("nwkid")) {
				buf = new byte[4];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 38, buf, 0, 4);
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("devadr")) {
				buf = new byte[4];
				System.arraycopy(raw, 42, buf, 0, 4);
				stringBuilder = new StringBuilder();
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("nwkskey")) {
				buf = new byte[16];
				System.arraycopy(raw, 46, buf, 0, 16);
				stringBuilder = new StringBuilder();
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("appskey")) {
				buf = new byte[16];
				System.arraycopy(raw, 62, buf, 0, 16);
				stringBuilder = new StringBuilder();
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("Latitude")) {
				buf = new byte[4];
				System.arraycopy(raw, 78, buf, 0, 4);
				field.set(device, ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
				//order(ByteOrder.LITTLE_ENDIAN)
			}
			if (field.getName().equals("Longitude")) {
				buf = new byte[4];
				System.arraycopy(raw, 82, buf, 0, 4);
				field.set(device, ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
			}
			if (field.getName().equals("outType")) {    //refactor!
				buf = new byte[5];
				System.arraycopy(raw, 86, buf, 0, 5);
				stringBuilder = new StringBuilder();
				for (Byte b : buf) {
					if (b == 0x00) {
					} else
						stringBuilder.append(new String(new byte[]{b}, StandardCharsets.UTF_8));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("kV")) {
				buf = new byte[28];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 91, buf, 0, 28);
				stringBuilder = new StringBuilder();
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
			if (field.getName().equals("kI")) {
				buf = new byte[2];
				stringBuilder = new StringBuilder();
				System.arraycopy(raw, 119, buf, 0, 2);
				for (Byte b : buf) {
					stringBuilder.append(String.format("%02x", b));
				}
				field.set(device, stringBuilder.toString());
			}
		}
		return device;

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

	public static int ConvertStringToInt(String nbOfBlocks) {

		String msb;
		int count;
		if (nbOfBlocks.length() > 2) {
			msb = nbOfBlocks.substring(0, 2);
			String lsb = nbOfBlocks.substring(2, 4);
			count = Integer.parseInt(lsb, 16);
			count += Integer.parseInt(msb, 16) * 256;
		} else {
			msb = nbOfBlocks.substring(0, 2);
			count = Integer.parseInt(msb, 16);
		}

		return count;
	}

	public static Device generate(String EUI, Location location) {
		String mEUI;
		mEUI = EUI.replace(" ", "");
		mEUI = mEUI.substring(8);
		Device newDev = new Device();
		newDev.type = "LC503";
		newDev.isOTTA = Boolean.FALSE;
		newDev.eui = EUI.replace(" ", "");
		newDev.eui = EUI.trim();
		newDev.appeui = "0000000000000001";
		newDev.appkey = "2B7E151628AED2A6ABF7158809CF4F3C";
		newDev.nwkid = "00000000";
		newDev.devadr = mEUI;
		newDev.nwkskey = "2B7E151628AED2A6ABF7158809CF4F3C";
		newDev.appskey = "2B7E151628AED2A6ABF7158809CF4F3C";
		newDev.Latitude = location.getLatitude();
		newDev.Longitude = location.getLongitude();
		newDev.outType = "PMW";
		newDev.kV = "EC03CE03D003E103E30304040E04B9096C09CE080F087407A6060506";
		newDev.kI = "991C";
		return newDev;
//		newDev.setEui();
	}
}
