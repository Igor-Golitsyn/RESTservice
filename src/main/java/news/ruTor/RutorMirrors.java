package news.ruTor;

/**
 * Created by golit on 05.06.2017.
 */
public enum RutorMirrors {
    rutor1("http://rus-film.org"),
    rutor2("http://rutor.info"),
    rutor3("http://rutor.is"),
    rutor4("http://fast-bit.org"),
    rutor5("http://zerkalo-rutor.org"),
    rutor6("http://free-tor.org");

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