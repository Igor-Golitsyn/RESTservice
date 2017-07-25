package news;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by golit on 03.07.2017.
 */
public class NewsPage implements Serializable {
    private String name;
    transient private BufferedImage[] images;
    private String text;
    private String button1Text;
    private String button1Action;
    private String button2Text;
    private String button2Action;

    public NewsPage(String name, BufferedImage[] images, String text, String button1Text, String button1Action, String button2Text, String button2Action) {
        this.name = name;
        this.images = images;
        this.text = text;
        this.button1Text = button1Text;
        this.button1Action = button1Action;
        this.button2Text = button2Text;
        this.button2Action = button2Action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage[] getImages() {
        return images;
    }

    public void setImages(BufferedImage[] images) {
        this.images = images;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getButton1Text() {
        return button1Text;
    }

    public void setButton1Text(String button1Text) {
        this.button1Text = button1Text;
    }

    public String getButton1Action() {
        return button1Action;
    }

    public void setButton1Action(String button1Action) {
        this.button1Action = button1Action;
    }

    public String getButton2Text() {
        return button2Text;
    }

    public void setButton2Text(String button2Text) {
        this.button2Text = button2Text;
    }

    public String getButton2Action() {
        return button2Action;
    }

    public void setButton2Action(String button2Action) {
        this.button2Action = button2Action;
    }

    @Override
    public String toString() {
        return "NewsPage{" +
                "name='" + name + '\'' +
                ", fotos='" + images.length + '\'' +
                ", text='" + text + '\'' +
                ", button1Text='" + button1Text + '\'' +
                ", button1Action='" + button1Action + '\'' +
                ", button2Text='" + button2Text + '\'' +
                ", button2Action='" + button2Action + '\'' +
                '}';
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(images.length); // how many images are serialized?
        for (BufferedImage eachImage : images) {
            ImageIO.write(eachImage, "jpg", out); // png is lossless
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        BufferedImage[] images = new BufferedImage[imageCount];
        for (int i = 0; i < imageCount; i++) {
            images[i] = ImageIO.read(in);
        }
    }
}
