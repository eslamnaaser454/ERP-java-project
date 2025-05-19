package app.Classes;

import app.ImageConverter.ImageConverter;
import app.ImageConverter.PngConverter;

import java.io.File;
import java.io.IOException;

public class Image {
    private File file;
    private ImageConverter converter;

    public Image(String imagePath) {
        this.file = new File(imagePath);
        this.converter = new PngConverter(); // هنا بنحدد الأداプター اللي هنستخدمه
    }

    public File getFile() {
        return file;
    }

    public File convertAndSave(String SavePath) throws IOException {
        if (!getFile().exists()) {
            System.out.println("File '" + getFile().toPath() + "' Doesn't Exist");
            return null;
        }

        File pathCheck = new File(SavePath);
        if (!pathCheck.exists())
            pathCheck.mkdirs();

        return converter.convertToPng(getFile(), SavePath);
    }
}