package app.Classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Image {

    private String ImagePath;
    private File file;

    public Image(String imagePath) {
        setImagePath(imagePath);
        setFile(new File(getImagePath()));
    }

    private String getImagePath() {
        return ImagePath;
    }

    private void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    private File getFile() {
        return file;
    }

    private void setFile(File file) {
        this.file = file;
    }



    public void SaveAt(String SavePath){
        if (!getFile().exists()){
            System.out.println("File '" + getFile().toPath() +"' Doesn't Exists");
            return;
        }

        File pathCheck = new File(SavePath);
        if (!pathCheck.exists())
            pathCheck.mkdirs();

        Path newPath = new File(SavePath + getFile().getName()).toPath();

        try {
            Files.copy(getFile().toPath(),newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
