package app.ImageConverter;

import app.Classes.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngConverter implements ImageConverter {
    @Override
    public File convertToPng(File imageFile, String destinationPath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (bufferedImage == null) {
            System.out.println("Could not read image file: " + imageFile.getAbsolutePath());
            return null;
        }

        String outputFileName = "converted_" + imageFile.getName().substring(0, imageFile.getName().lastIndexOf('.')) + ".png";
        File outputFile = new File(destinationPath, outputFileName);
        ImageIO.write(bufferedImage, "png", outputFile);
        return outputFile;
    }
}