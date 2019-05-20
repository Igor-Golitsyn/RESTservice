package news.ruTor;

/**
 * Created by golit on 05.06.2017.
 */
public enum RutorMirrors {
    rutor8("http://rutor.is/kino"),
    rutor9("http://rutorc6mqdinc4cz.onion/kino"),
    rutor2("http://rutor.info"),
    rutor3("http://rutor.is"),
    rutor7("http://rutorc6mqdinc4cz.onion"),
    rutor0("http://[2001:67c:28f8:7b:42df:833:9648:5d6d]"),
    rutor1("http://rus-film.org"),
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