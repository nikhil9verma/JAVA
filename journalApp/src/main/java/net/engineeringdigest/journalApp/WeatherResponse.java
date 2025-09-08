package net.engineeringdigest.journalApp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse{
//    public Location location;
    public Current current;
    public Condition  condition;

    @Getter
    @Setter
    public static class Condition{
        public String text;
        public String icon;
        public int code;
    }

    @Getter
    @Setter
    public static class Current{

        public double feelslike_c;

        public Condition condition;


    }

}





