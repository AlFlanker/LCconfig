
package ru.yugsys.vvvresearch.lconfig.Services;

import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;


public class NFCCommand {

	 //***********************************************************************/
	 //* the function send an Get System Info command (0x02 0x2B) 
	 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
	 //***********************************************************************/
     //* REFACTOR

	public static byte[] SendGetSystemInfoCommandCustom(Tag myTag, DataDevice dataDevice)
	 {
		 boolean boolDeviceDetected = false;
         dataDevice.setBasedOnTwoBytesAddress(false);
         dataDevice.setTechno("");
         dataDevice.setManufacturer("");

         // detect 1 or 2 bytes address ---
		 byte[] UIDFrame = new byte[] { (byte) 0x26, (byte) 0x01, (byte) 0x00 };
		 byte[] response = new byte[] { (byte) 0xAA };
		 
		 int errorOccured = 1;
		 while(errorOccured != 0)
		 {
			 try 
			 {
				 NfcV nfcvTag = NfcV.get(myTag);
				 nfcvTag.close();
				 nfcvTag.connect();
				 response = nfcvTag.transceive(UIDFrame);
				 nfcvTag.close();
                 if (response[0] == (byte) 0x00 || response[0] == (byte) 0x01)
				 {
					 errorOccured = 0;				
				 }
			 }
			 catch (Exception e)
			 {
				 errorOccured ++;
                 if (errorOccured >= 5) return response;

			}
		 }

         //  UID analysis
		 if (response[9] == (byte) 0xE0)
		 {
             dataDevice.setTechno("ISO 15693");
			 byte[] uid = new byte[8];

             StringBuilder sb = new StringBuilder();
			 for (int i = 1; i <= 8; i++) 
			 {
				 uid[i - 1] = response[10 - i];
                 sb.append(Util.ConvertHexByteToString(uid[i - 1]));
			 }
             dataDevice.setUid(sb.toString());
			 if (response[8] == (byte) 0x02) //ST product
			 {
                 dataDevice.setManufacturer("STMicroelectronics");
				 if(response[7] >= (byte) 0x04 && response[7] <= (byte) 0x07)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x14 && response[7] <= (byte) 0x17)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
					 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x20 && response[7] <= (byte) 0x23)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
					 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x28 && response[7] <= (byte) 0x2B)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x2C && response[7] <= (byte) 0x2F)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x40 && response[7] <= (byte) 0x43)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x44 && response[7] <= (byte) 0x47)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x48 && response[7] <= (byte) 0x4B)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x4C && response[7] <= (byte) 0x4F)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x50 && response[7] <= (byte) 0x53)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x54 && response[7] <= (byte) 0x57)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x58 && response[7] <= (byte) 0x5B)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
					 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x5C && response[7] <= (byte) 0x5F)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x60 && response[7] <= (byte) 0x63)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(false);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x64 && response[7] <= (byte) 0x67)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0x6C && response[7] <= (byte) 0x6F)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }
				 else if(response[7] >= (byte) 0xF8 && response[7] <= (byte) 0xFB)
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
				 	 boolDeviceDetected=true;
				 }	 
				 else
				 {
                     dataDevice.setBasedOnTwoBytesAddress(true);
					 boolDeviceDetected=false;
				 }		
				 
			 } else if (response[8] == (byte) 0x01)
			 {
                 dataDevice.setManufacturer("Motorola");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x03)
			 {
                 dataDevice.setManufacturer("Hitachi");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x04)
			 {
                 dataDevice.setManufacturer("NXP");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x05)
			 {
                 dataDevice.setManufacturer("Infineon");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x06)
			 {
                 dataDevice.setManufacturer("Cylinc");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x07)
			 {
                 dataDevice.setManufacturer("Texas Instruments");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x08)
			 {
                 dataDevice.setManufacturer("Fujitsu");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x09)
			 {
                 dataDevice.setManufacturer("Matsushita");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0A)
			 {
                 dataDevice.setManufacturer("NEC");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0B)
			 {
                 dataDevice.setManufacturer("Oki");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0C)
			 {
                 dataDevice.setManufacturer("Toshiba");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0D)
			 {
                 dataDevice.setManufacturer("Mitsubishi");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0E)
			 {
                 dataDevice.setManufacturer("Samsung");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x0F)
			 {
                 dataDevice.setManufacturer("Hyundai");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else if (response[8] == (byte) 0x10)
			 {
                 dataDevice.setManufacturer("LG");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 } else
			 {
                 dataDevice.setManufacturer("Unknown manufacturer");
                 dataDevice.setBasedOnTwoBytesAddress(false);
				 boolDeviceDetected=true;
			 }
				 
		 }
		 else
		 {
			 boolDeviceDetected=false;
             dataDevice.setManufacturer("Unknown techno");
		 }


		 int cpt2 = 0;
		 int cpt_2bytes = 0;
		 
		 response = new byte[] { (byte) 0xAA };
         byte[] systemInfoFrame2bytesAddress;
         systemInfoFrame2bytesAddress = new byte[]{(byte) 0x0A, (byte) 0x2B};
         byte[] systemInfoFrame1bytesAddress;
         systemInfoFrame1bytesAddress = new byte[]{(byte) 0x02, (byte) 0x2B};

         if (boolDeviceDetected) {
				 while ((response == null || response[0] == (byte)0xAA) && cpt2 <= 1 && cpt_2bytes <= 5)
				 {	 
					 try 
					 {
						 cpt_2bytes++;
						 NfcV nfcvTag = NfcV.get(myTag);
						 nfcvTag.close();  
						 nfcvTag.connect();
                         if (dataDevice.isBasedOnTwoBytesAddress())
                             response = nfcvTag.transceive(systemInfoFrame2bytesAddress);
						 else
                             response = nfcvTag.transceive(systemInfoFrame1bytesAddress);
						 if (response[0] == (byte) 0x00)
						 {
						 	return response;
						 }
					 }
					 catch (Exception e)
					 {

                         dataDevice.setBasedOnTwoBytesAddress(false);
						 cpt2++;
					}
				 }
		 }
		 else
		 {
			 int cpt = 0;		 
			 response = new byte[] { (byte) 0xAA };
             byte[] systemInfoFrame;
			 
			 // to know if tag's addresses are coded on 1 or 2 byte we consider 2  
			 // then we wait the response if it's not good we trying with 1
             dataDevice.setBasedOnTwoBytesAddress(true);
			 
			 //1st flag=1 for 2 bytes address products
             systemInfoFrame = new byte[]{(byte) 0x0A, (byte) 0x2B};
			 while ((response == null || response[0] == (byte)0xAA) && cpt <= 1)
			 {	 
				 try 
				 {
					 NfcV nfcvTag = NfcV.get(myTag);
					 nfcvTag.close();
					 nfcvTag.connect();
                     response = nfcvTag.transceive(systemInfoFrame);
					 nfcvTag.close();
					 if (response[0] == (byte) 0x00)
					 {
                         dataDevice.setBasedOnTwoBytesAddress(true);    //1st (flag=1) = 2 add bytes (M24LR64 FREEDOM2)
					 	return response;
					 }
				 }
				 catch (Exception e)
				 {
					//Used for DEBUG : Log.i("Exception","Get System Info Exception " + e.getMessage());
					 cpt++;
				}
			 }
	
			 //2nd flag=0 for 1 byte address products
			 cpt=0;
             systemInfoFrame = new byte[]{(byte) 0x02, (byte) 0x2B};
			 while ((response == null || response[0] == (byte)0xAA) && cpt <= 1)
			 {	 
				 try 
				 {
					 NfcV nfcvTag = NfcV.get(myTag);
					 nfcvTag.close();
					 nfcvTag.connect();
                     response = nfcvTag.transceive(systemInfoFrame);
					 nfcvTag.close();
					 if (response[0] == (byte) 0x00)
					 {
                         dataDevice.setBasedOnTwoBytesAddress(false);    //1st (flag=1) = 2 add bytes (M24LR64 FREEDOM2)
					 	return response;
					 }
				 }
				 catch (Exception e)
				 {
					//Used for DEBUG : Log.i("Exception","Get System Info Exception " + e.getMessage());
					 cpt++;
				}
			 }

             //Used for DEBUG : Log.i("NFCCOmmand", "Response Get System Info " + Util.ConvertHexByteArrayToString(response));
			return response;
		 }

         //Used for DEBUG : Log.i("NFCCOmmand", "Response Get System Info " + Util.ConvertHexByteArrayToString(response));
		return response;
	 } 
	 
	//***********************************************************************/
	 //* the function send an ReadSingle command (0x0A 0x20) || (0x02 0x20) 
	 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
	 //* example : StartAddress {0x00, 0x02}  NbOfBlockToRead : {0x04}
	 //* the function will return 04 blocks read from address 0002
	 //* According to the ISO-15693 maximum block read is 32 for the same sector
	 //***********************************************************************/
    public static byte[] ReadSingleBlockCommand(Tag myTag, byte[] StartAddress, DataDevice ma)
	 {
		 byte[] response = new byte[] {(byte) 0x0A};
         byte[] readSingleBlockFrame;
		 
		 if(ma.isBasedOnTwoBytesAddress())
             readSingleBlockFrame = new byte[]{(byte) 0x0A, (byte) 0x20, StartAddress[1], StartAddress[0]};
		 else
             readSingleBlockFrame = new byte[]{(byte) 0x02, (byte) 0x20, StartAddress[1]};

		 int errorOccured = 1;
		 while(errorOccured != 0)
		 {
			 try
			 {
				 NfcV nfcvTag = NfcV.get(myTag);
				 nfcvTag.close();
				 nfcvTag.connect();
                 response = nfcvTag.transceive(readSingleBlockFrame);
				 if(response[0] == (byte) 0x00 || response[0] == (byte) 0x01)//response 01 = error sent back by tag (new Android 4.2.2) or BC
				 {
					 Log.d("NFC_tag", Byte.toString(response[0]));
					 errorOccured = 0;
                     //Used for DEBUG : Log.i("NFCCOmmand", "SENDED Frame : " + Util.ConvertHexByteArrayToString(readSingleBlockFrame));
				 }
			 }
			 catch(Exception e)
			 {
				 errorOccured++;
                 //Used for DEBUG : Log.i("NFCCOmmand", "Response Read Single Block" + Util.ConvertHexByteArrayToString(response));
				 if(errorOccured == 2)
				 {
					//Used for DEBUG : Log.i("Exception","Exception " + e.getMessage());
					 return response;
				 }
			 }
		 }
         //Used for DEBUG : Log.i("NFCCOmmand", "Response Read Sigle Block" + Util.ConvertHexByteArrayToString(response));
		 return response;
	 }
	 
	 
	 //***********************************************************************/
	 //* the function send an ReadSingle Custom command (0x0A 0x20) || (0x02 0x20) 
	 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
	 //* example : StartAddress {0x00, 0x02}  NbOfBlockToRead : {0x04}
	 //* the function will return 04 blocks read from address 0002
	 //* According to the ISO-15693 maximum block read is 32 for the same sector
	 //***********************************************************************/
     public static byte[] SendReadMultipleBlockCommandCustom(Tag myTag, byte[] StartAddress, byte numOfBlockToRead, DataDevice dataDevice)
	 {
		 Log.d("NFC", "SendReadMultipleBlockCommandCustom in");
		long cpt =0;
		boolean checkCorrectAnswer = true;

         //int numBytesToRead = (NbOfBlockToRead*4)+1;
         int numBytesToRead = numOfBlockToRead * 4;
         byte[] finalResponse = new byte[numBytesToRead + 1];

         for (int i = 0; i <= (numOfBlockToRead * 4) - 4; i = i + 4) {
             byte[] temp;
             int incrementAddressStart0 = (StartAddress[0] + i / 256);
             int incrementAddressStart1 = (StartAddress[1] + i / 4) - (incrementAddressStart0 * 255);
			
			temp = null;
			while (temp == null || temp[0] == 1 && cpt <= 2)
			{

                temp = ReadSingleBlockCommand(myTag, new byte[]{(byte) incrementAddressStart0, (byte) incrementAddressStart1}, dataDevice);

				cpt++;
			}
			cpt =0;
			
			//Check  if Read problem
			if (temp[0] != 0x00)
				checkCorrectAnswer = false;
				
			if(i==0)
			{
				for(int j=0;j<=4;j++)
				{
					if (temp[0] == 0x00)
                        finalResponse[j] = temp[j];
					else
                        finalResponse[j] = (byte) 0xFF;
				}
			}
			else 
			{
				for(int j=1;j<=4;j++)
				{
					if (temp[0] == 0x00)
                        finalResponse[i + j] = temp[j];
                    else
                        finalResponse[j] = (byte) 0xFF;
                }
            }
         }
		
		if (checkCorrectAnswer == false)
            finalResponse[0] = (byte) 0xAE;

         return finalResponse;
     }


    public static byte[] Send_several_ReadSingleBlockCommands_NbBlocks(Tag myTag, byte[] startAddress, byte[] bNbOfBlockToRead, DataDevice dataDevice)
	 {
		long cpt =0;
		boolean checkCorrectAnswer = true;
         int iNbOfBlockToRead = Util.Convert2bytesHexaFormatToInt(bNbOfBlockToRead);
         byte[] finalResponse = new byte[iNbOfBlockToRead * 4 + 1];
         byte[] byteAddress;
         int intAddress = Util.Convert2bytesHexaFormatToInt(startAddress);
		
		int index = 0;

         byte[] temp;
		
		//boucle for(int i=0;i<iNbOfBlockToRead; i++)
		do
		 {
             byteAddress = Util.ConvertIntTo2bytesHexaFormat(intAddress);
			
			temp = null;
			while (temp == null || temp[0] == 1 && cpt <= 5)
			{
                temp = ReadSingleBlockCommand(myTag, new byte[]{byteAddress[0], byteAddress[1]}, dataDevice);
				cpt ++;
			}
			cpt =0;

             if (temp[0] != 0x00)
				checkCorrectAnswer = false;
			
			if (temp[0] == 0)
			{
				if(index==0)
				{
					for(int j=0;j<=4;j++)
                        finalResponse[j] = temp[j];
				}
				else 
				{
					for(int j=1;j<=4;j++)
                        finalResponse[(index * 4) + j] = temp[j];
				}
		 	}
			else
			{
				if(index==0)
				{
					for(int j=0;j<=4;j++)
                        finalResponse[j] = (byte) 0xFF;
				}
				else 
				{
					for(int j=1;j<=4;j++)
                        finalResponse[(index * 4) + j] = (byte) 0xFF;
				}
		 	}
			
			intAddress++;
			index++;
			
		} while(index < iNbOfBlockToRead);
		
		if (checkCorrectAnswer == false)
            finalResponse[0] = (byte) 0xAF;

         return finalResponse;
     }

	//***********************************************************************/
	 //* the function send an ReadSingle Custom command (0x0A 0x20) || (0x02 0x20) 
	 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
	 //* example : StartAddress {0x00, 0x02}  NbOfBlockToRead : {0x04}
	 //* the function will return 04 blocks read from address 0002
	 //* According to the ISO-15693 maximum block read is 32 for the same sector
	 //***********************************************************************/

    public static byte[] SendReadMultipleBlockCommandCustom2(Tag myTag, byte[] startAddress, byte[] bNbOfBlockToRead, DataDevice dataDevice)
	 {
		 
		 boolean checkCorrectAnswer = true;

         int iNbOfBlockToRead = Util.Convert2bytesHexaFormatToInt(bNbOfBlockToRead);
		 int iNumberOfSectorToRead;
         int iStartAddress = Util.Convert2bytesHexaFormatToInt(startAddress);
		 int iAddressStartRead = (iStartAddress/32)*32;
		 if(iNbOfBlockToRead%32 == 0)
		 {
			 iNumberOfSectorToRead = (iNbOfBlockToRead/32);
		 }
		 else
		 {
			 iNumberOfSectorToRead = (iNbOfBlockToRead/32)+1;
		 }
         byte[] bAddressStartRead = Util.ConvertIntTo2bytesHexaFormat(iAddressStartRead);

         byte[] allReadDatas = new byte[((iNumberOfSectorToRead * 128) + 1)];
         byte[] finalResponse = new byte[(iNbOfBlockToRead * 4) + 1];

         String sMemorySize = dataDevice.getMemorySize();
         sMemorySize = Util.StringForceDigit(sMemorySize, 4);
         byte[] bLastMemoryAddress = Util.ConvertStringToHexBytes(sMemorySize);
		 

		 for(int i=0; i<iNumberOfSectorToRead;i++)
		 {
             byte[] temp;
			 
			 int incrementAddressStart0 = (bAddressStartRead[0]+i/8)  ;									//Most Important Byte
			 int incrementAddressStart1 = (bAddressStartRead[1]+i*32) - (incrementAddressStart0*256);	//Less Important Byte
			 
			 
			 if(bAddressStartRead[0]<0)
			 	 incrementAddressStart0 = ((bAddressStartRead[0]+256)+i/8);	
			 
			 if(bAddressStartRead[1]<0)
				 incrementAddressStart1 = ((bAddressStartRead[1]+256)+i*32) - (incrementAddressStart0*256);
			
			 
			 if(incrementAddressStart1 > bLastMemoryAddress[1] && incrementAddressStart0 > bLastMemoryAddress[0])
			 {
				 
			 
			 }
			 else
			 {
				temp = null;
                 temp = readMultipleBlockCommand(myTag, new byte[]{(byte) incrementAddressStart0, (byte) incrementAddressStart1}, (byte) 0x1F, dataDevice);
				
				if (temp[0] != 0x00)
					checkCorrectAnswer = false;
				
				// if any error occurs during 
				if(temp[0] == (byte)0x01)
				{
					return temp;
				}
				else
				{
					// to construct a response with first byte = 0x00
					if(i==0)
					{
						for(int j=0;j<=128;j++)
						{
                            allReadDatas[j] = temp[j];
						}
					}
					else 
					{
						for(int j=1;j<=128;j++)
						{
                            allReadDatas[(i * 128) + j] = temp[j];
						}
					}
				}
			 }
		 }

         int iNbBlockToCopyInFinalReponse = Util.Convert2bytesHexaFormatToInt(bNbOfBlockToRead);
         int iNumberOfBlockToIgnoreInAllReadData = 4 * (Util.Convert2bytesHexaFormatToInt(startAddress) % 32);
		 
		 for(int h=1; h <= iNbBlockToCopyInFinalReponse*4 ; h++)
		 {
             finalResponse[h] = allReadDatas[h + iNumberOfBlockToIgnoreInAllReadData];
		 }
		 
		 if (checkCorrectAnswer == true)
             finalResponse[0] = allReadDatas[0];
         else
             finalResponse[0] = (byte) 0xAF;

         return finalResponse;
     }

	//***********************************************************************/
	 //* the function send an ReadMultiple command (0x0A 0x23) || (0x02 0x23) 
	 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
    //* example : startAddress {0x00, 0x02}  NbOfBlockToRead : {0x04}
	 //* the function will return 04 blocks read from address 0002
	 //* According to the ISO-15693 maximum block read is 32 for the same sector
	 //***********************************************************************/
    public static byte[] readMultipleBlockCommand(Tag myTag, byte[] startAddress, byte NbOfBlockToRead, DataDevice dataDevice)
	 {
		 byte[] response = new byte[] {(byte) 0x01};
         byte[] readMultipleBlockFrame;

         if (dataDevice.isBasedOnTwoBytesAddress())
             readMultipleBlockFrame = new byte[]{(byte) 0x0A, (byte) 0x23, startAddress[1], startAddress[0], NbOfBlockToRead};
		 else
             readMultipleBlockFrame = new byte[]{(byte) 0x02, (byte) 0x23, startAddress[1], NbOfBlockToRead};

		 
		 int errorOccured = 1;
		 while(errorOccured != 0)
		 {
			 try
			 {
				 NfcV nfcvTag = NfcV.get(myTag);
				 nfcvTag.close();
				 nfcvTag.connect();
                 response = nfcvTag.transceive(readMultipleBlockFrame);
				 if(response[0] == (byte) 0x00 || response[0] == (byte) 0x01)//response 01 = error sent back by tag (new Android 4.2.2) or BC
				 {
					 errorOccured = 0;
				 }
			 }
			 catch(Exception e)
			 {
				 errorOccured++;
				 if(errorOccured == 3)
				 {
                     return response;
				 }
			 }
		 }
		 return response;
	 }
	 
	 

		//***********************************************************************/
		 //* the function send an WriteSingle command (0x0A 0x21) || (0x02 0x21) 
		 //* the argument myTag is the intent triggered with the TAG_DISCOVERED
		 //* example : StartAddress {0x00, 0x02}  DataToWrite : {0x04 0x14 0xFF 0xB2}
		 //* the function will write {0x04 0x14 0xFF 0xB2} at the address 0002
		 //***********************************************************************/
        public static byte[] writeSingleBlockCommand(Tag myTag, byte[] StartAddress, byte[] DataToWrite, DataDevice ma)
		 {
			 byte[] response = new byte[] {(byte) 0xFF}; 
			 byte[] WriteSingleBlockFrame;
			 
			 if(ma.isBasedOnTwoBytesAddress())
			 {
				 if (ma.getManufacturer().equals("Texas Instruments"))
					 WriteSingleBlockFrame = new byte[]{(byte) 0x4A, (byte) 0x21, StartAddress[1], StartAddress[0], DataToWrite[0], DataToWrite[1], DataToWrite[2], DataToWrite[3]};
				 else
					 WriteSingleBlockFrame = new byte[]{(byte) 0x0A, (byte) 0x21, StartAddress[1], StartAddress[0], DataToWrite[0], DataToWrite[1], DataToWrite[2], DataToWrite[3]};
			 } 				
			 else
			 {
				 if (ma.getManufacturer().equals("Texas Instruments"))
					 WriteSingleBlockFrame = new byte[]{(byte) 0x42, (byte) 0x21, StartAddress[1], DataToWrite[0], DataToWrite[1], DataToWrite[2], DataToWrite[3]};
				 else
					 WriteSingleBlockFrame = new byte[]{(byte) 0x02, (byte) 0x21, StartAddress[1], DataToWrite[0], DataToWrite[1], DataToWrite[2], DataToWrite[3]};
		 	 }
			 
			 int errorOccured = 1;
			 while(errorOccured != 0)
			 {
				 try
				 {
					 NfcV nfcvTag = NfcV.get(myTag);
					 nfcvTag.close();
					 nfcvTag.connect();
					 response = nfcvTag.transceive(WriteSingleBlockFrame);
					 if(response[0] == (byte) 0x00 || response[0] == (byte) 0x01) //response 01 = error sent back by tag (new Android 4.2.2) or BC
					 {
						 errorOccured = 0;

                     }
				 }
				 catch(Exception e)
				 {
					 errorOccured++;

                     if(errorOccured == 2)
					 {
						 return response;
					 }
				 }
			 }
			 return response;
		 }

}
