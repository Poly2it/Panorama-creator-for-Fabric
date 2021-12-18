package Global.Func;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Image {
    public static BufferedImage scaleImage(File in,File out, int destWidth, int destHeight) throws IOException {
        BufferedImage src = ImageIO.read(in);

        if(src == null){
            System.out.println("src is null");
        }

        int width = src.getWidth();
        int height = src.getHeight();

        double widthScale = (double) destWidth / (double) width;
        double heightScale = (double) destHeight / (double) height;
        double scale = widthScale < heightScale ? widthScale : heightScale;

        ImageFilter filter = new AreaAveragingScaleFilter(
                (int) (src.getWidth() * scale), (int) (src.getHeight() * scale));
        ImageProducer p = new FilteredImageSource(src.getSource(), filter);
        java.awt.Image dstImage = Toolkit.getDefaultToolkit().createImage(p);
        BufferedImage dst = new BufferedImage(
                dstImage.getWidth(null), dstImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        g.drawImage(dstImage, 0, 0, null);

        ImageIO.write(dst, "png", out);

        g.dispose();
        return dst;
    }

    public static BufferedImage deepCopy(BufferedImage bi, File saveAs,String ext) throws IOException {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        BufferedImage cImg = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        ImageIO.write(cImg, ext, saveAs);
        return cImg;
    }
}
