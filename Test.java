import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Test{
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("Order 1");
        matcher.find();
        System.out.println(matcher.group());
    }
}