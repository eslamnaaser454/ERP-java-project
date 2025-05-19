package app.ImageConverter;

import java.io.File;
import java.io.IOException;

public interface ImageConverter {
    File convertToPng(File imageFile, String destinationPath) throws IOException;
}

