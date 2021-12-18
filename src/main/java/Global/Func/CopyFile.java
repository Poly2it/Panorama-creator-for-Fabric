package Global.Func;

import java.io.*;
import java.nio.channels.FileChannel;

public class CopyFile {
    public static void copyFile(File sf, File df) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(sf);
            fos = new FileOutputStream(df);

            FileChannel sc = fis.getChannel();
            FileChannel dc = fos.getChannel();
            dc.transferFrom(sc, 0, sc.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) try { fos.close(); } catch (IOException e) {}
            if (fis != null) try { fis.close(); } catch (IOException e) {}
        }
    }
}
