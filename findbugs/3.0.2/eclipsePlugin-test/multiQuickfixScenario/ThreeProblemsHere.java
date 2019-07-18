

public class ThreeProblemsHere {


    public static void main(String[] args) {
        Double d = new Double(8.1);        //should fix (broken)
        Double dokay = Double.valueOf("9.1");  //should not fix (not broken)
        Float f = new Float(8.2f);        //should fix (broken)
        Double d2 = helper();

        System.out.printf("%s %s %s %s", d, dokay, f, d2);        //to ignore "not used" warnings
    }

    private static Double helper() {
        return new Double(8.3);        //should fix (broken)
    }

}
