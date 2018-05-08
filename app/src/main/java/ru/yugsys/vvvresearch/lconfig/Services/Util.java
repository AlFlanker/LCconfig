// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package ru.yugsys.vvvresearch.lconfig.Services;

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
