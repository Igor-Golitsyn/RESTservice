package news.ruTor;

public enum RuTorPic {
    pic1("Fastpic.Ru"),
    pic2("Sendpic.org"),
    pic3("Radikal.Ru"),
    pic4("Hostingkartinok.com"),
    pic5("Postimage.org"),
    pic6("Lostpic.net"),
    pic7("Scrin.org"),
    pic8("Imageshost.ru"),
    pic9("Imageban.ru"),
    pic10("Lostpix.com"),
    pic11("photo7ya.ru"),
    pic12("powerlogo.ru"),
    pic13("uploadimagex.com");

    private final String name;

    RuTorPic(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
