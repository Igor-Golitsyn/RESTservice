package news.ruTor;

/**
 * Created by golit on 05.06.2017.
 */
public enum RutorMirrors {
    rutor2("http://rutor.info"),
    rutor1("http://rus-film.org"),
    rutor3("http://rutor.is");

    private final String name;

    RutorMirrors(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}