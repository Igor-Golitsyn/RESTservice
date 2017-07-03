package news;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Created by golit on 03.07.2017.
 */
public class NewsPage {
    private String name;
    private BufferedImage image;
    private String text;
    private String button1Text;
    private String button1Action;
    private String button2Text;
    private String button2Action;

    public NewsPage(String name, BufferedImage image, String text, String button1Text, String button1Action, String button2Text, String button2Action) {
        this.name = name;
        this.image = image;
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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
}
