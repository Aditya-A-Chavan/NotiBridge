package org.aditya.notibridgedesktopappjava.util;


// import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStreamReader;
import org.aditya.notibridgedesktopappjava.util.DeviceIDUtil;

public class MachineGUIDReader {
    
    public static String GetMachineGUID(){
        try{
            Process process = new ProcessBuilder(
                "reg", "query",
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Cryptography",
                "/v", "MachineGuid"
            ).redirectErrorStream(true).start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            String line;

            Pattern pattern = Pattern.compile("MachineGuid\\s+REG_SZ\\s+(.*)");

            while ((line = reader.readLine()) != null){
                Matcher matcher = pattern.matcher(line.trim());
                if(matcher.find()) {
                    return matcher.group(1).trim();
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
} 