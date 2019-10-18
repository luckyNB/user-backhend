package com.bridgelabz.usermgmt.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateExtraction {

	public static void main(String[] args) throws ParseException {

	    String input = "2019-09-18T18:30:00.000Z";

	    // before Java 8
	    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    Date date = oldFormat.parse(input);
	    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

	    String output = newFormat.format(date);

	    System.out.println("Date in old format: " + input);
	    System.out.println("Date in new format: " + output);

	    // Using Java 8 new Date and Time API
	    DateTimeFormatter oldPattern = DateTimeFormatter
	        .ofPattern("yyyy-MM-dd HH:mm:ss");
	    DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    LocalDateTime datetime = LocalDateTime.parse(input, oldPattern);
	    output = datetime.format(newPattern);

	    System.out.println("Date in old format (java 8) : " + input);
	    System.out.println("Date in new format (java 8) : " + output);

	}

}
