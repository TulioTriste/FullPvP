package net.panda.fullpvp.utilities;

public class Ints {

	public static Integer tryParse(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public static Float tryParseFloat(String string) {
    	try {
			return Float.parseFloat(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
    }
    
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
