package ru.yugsys.vvvresearch.lconfig.Services;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    String[] lines = new String[]{
            "36B575BDD8B5178986108FCA6456412280B0519006BD3249C4760713655643FB4FD2F0D7AB28A8AD253848720630B190383DF000D5531D6C2AF4F2CD8BA20AA23306A558D016FA5F7B157A0BB0C6B8A06CD2619035C23701479F328362B821DDDD9F9AC4A43B9433D8AFC1D1C458B8502805C0A9B929704059",
            "36"
    };

    @Test(expected = ClassNotFoundException.class)
    public void decodeByteList() throws ClassNotFoundException {
        for (String s : lines)
            Helper.decodeByteList(s);


    }
}