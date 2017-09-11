package news.vodaUUru;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataVoda  {

    @SerializedName("date1")
    @Expose
    public List<Date1> date1 = null;
    @SerializedName("date2")
    @Expose
    public List<Date2> date2 = null;
    @SerializedName("date3")
    @Expose
    public List<Date3> date3 = null;

    public abstract class DatesData{
        @SerializedName("coordinates")
        @Expose
        public List<Float> coordinates = null;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("type")
        @Expose
        public String type;
    }

    public class Date1 extends DatesData {

    }
    public class Date2 extends DatesData  {

    }
    public class Date3 extends DatesData  {

    }

    @Override
    public String toString() {
        return "DataVoda{" +
                "date1=" + date1 +
                ", date2=" + date2 +
                ", date3=" + date3 +
                '}';
    }
}
