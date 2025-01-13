package imageService;
import java.util.*;
import java.lang.*;
import io.protostuff.Tag;
public class ImageService {

public static class DownloadImageResponse {
    @Tag(1)
    String imagedata;
    public String getImagedata() {
        return imagedata;
    }
    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }
}

public static class DeleteImageResponse {
    @Tag(1)
    boolean result;
    public boolean getResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
}

public static class DeleteImageRequest {
    @Tag(1)
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

public static class UploadImageRequest {
    @Tag(1)
    String name;
    @Tag(2)
    String extension;
    @Tag(3)
    String imagedata;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getImagedata() {
        return imagedata;
    }
    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }
}

public static class UploadFileResponse {
    @Tag(1)
    boolean result;
    @Tag(2)
    String imagename;
    public boolean getResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public String getImagename() {
        return imagename;
    }
    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}

public static class DownloadImageRequest {
    @Tag(1)
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

}
