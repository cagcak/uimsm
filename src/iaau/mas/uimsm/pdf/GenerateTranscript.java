package iaau.mas.uimsm.pdf;

import iaau.mas.uimsm.HomeActivity;
import iaau.mas.uimsm.fragment.home.Fragment_Transcript;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Current;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class GenerateTranscript 
{
	public static String FILENAME = "Transcript.pdf";
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/UIMSM/Transcript/";
	public static String FILE_ADDRESS = PATH + FILENAME;
	
	private static Bundle extras;
	
	private static Map<String, HashMap<String, String>> outer_map = new HashMap<String, HashMap<String, String>>();
    @SuppressWarnings("unused")
	private static Map<String, String> inner_map = new HashMap<String, String>();
	
	private static String userName;
	
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	@SuppressWarnings("unused")
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	@SuppressWarnings("unused")
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	@SuppressWarnings("unused")
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	
	private static List<String> list_1stYear = new LinkedList<String>();
	private static List<String> list_2ndYear = new LinkedList<String>();
	private static List<String> list_3rdYear = new LinkedList<String>();
	private static List<String> list_4thYear = new LinkedList<String>();
	private static List<String> list_5thYear = new LinkedList<String>();
	private static List<String> list_6thYear = new LinkedList<String>(); // Extra year (optional) 
	
	private static Map<String, HashMap<String, String>> firstYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> firstYear_inner_map = new HashMap<String, String>();
    
    private static Map<String, HashMap<String, String>> secondYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> secondYear_inner_map = new HashMap<String, String>();
    
    private static Map<String, HashMap<String, String>> thirdYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> thirdYear_inner_map = new HashMap<String, String>();
    
    private static Map<String, HashMap<String, String>> forthYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> forthYear_inner_map = new HashMap<String, String>();
    
    private static Map<String, HashMap<String, String>> fifthYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> fifthYear_inner_map = new HashMap<String, String>();
    
    // Extra year (optional)
    private static Map<String, HashMap<String, String>> sixthYear_outer_map = new HashMap<String, HashMap<String, String>>();
    private static Map<String, String> sixthYear_inner_map = new HashMap<String, String>();
	
    public static String[] semesters = {"FALL", "SPRING"};
    private static List<String> first_year = new ArrayList<String>();
    private static List<String> second_year = new ArrayList<String>();
    private static List<String> third_year = new ArrayList<String>();
    private static List<String> forth_year = new ArrayList<String>();
    private static List<String> fifth_year = new ArrayList<String>();
    private static List<String> sixth_year = new ArrayList<String>();
	
	public GenerateTranscript() 
	{
		super();
	}
	
	public void generatePDF()
	{
		File directory = new File(PATH);    
        directory.mkdirs();
		
        getUserCredentials();
        
        setFirstYear();
	    setSecondYear();
	    setThirdYear();
	    setForthYear();
	    setFifthYear();
	    setSixthYear();
        
		try{
			Document document = new Document();
		    PdfWriter.getInstance(document, new FileOutputStream(FILE_ADDRESS));
		    document.open();
		    addMetaData(document);
		    addTitlePage(document);
		    addContent(document);
//		    document.add( new Chunk("testing")); 
		    document.close();
		} catch (Exception e) {
		      e.printStackTrace();
	    }
	}
	

	@SuppressWarnings("unchecked")
	private static void getUserCredentials()
	{
		Fragment_Transcript bundleReference = new Fragment_Transcript();
		extras = bundleReference.sendUserBundle();
		outer_map = (Map<String, HashMap<String, String>>) extras.getSerializable("outer");
		inner_map = (Map<String, String>) extras.getSerializable("inner");
		
		userName = Fragment_MyInformation_Current.fullname;
	}
	
	private static String getUserID()
	{
		String id = HomeActivity.userID;
		return id;
	}
	
	@SuppressLint("SimpleDateFormat")
	private static String getDate()
	{
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	private static void addMetaData(Document document) 
	{
	    document.addTitle("New Technologies Faculty - Department of Computer Engineering");
	    document.addSubject("Transcript of " + getUserID());
	}

	private static Paragraph addTitlePage(Document document) throws DocumentException
	{
		Paragraph preface = new Paragraph();
	    
//			addEmptyLine(preface, 1);   // one empty line
//	    preface.add(new Paragraph((Element.ALIGN_CENTER), "New Technologies Faculty - Department of Computer Engineering", catFont));
//	    	addEmptyLine(preface, 1);   // one empty line
	    preface.add(new Paragraph((Element.ALIGN_CENTER), "Transcript of " + userName + "   [  " + getUserID() + "  ]	      Date:" + getDate(), catFont));
	    
//	    document.add(preface);
	    
		return preface;
	}

	private static void addContent(Document document) throws DocumentException
	{
		//  Head (Header)
		String header = "New Technologies Faculty - Department of Computer Engineering"; 
		Paragraph paragraphTitle = new Paragraph((Element.ALIGN_CENTER), header, catFont);
			addEmptyLine(paragraphTitle, 1);   // one empty line
		Chapter chapter1 = new Chapter(paragraphTitle, 0);
		Section section1 = chapter1.addSection(addTitlePage(document));
		
		// Body (Table and Lists)
		createOuterTable(section1);

		
		
		// Foot (Signature)
		
		document.add(chapter1);
		chapter1.setNumberDepth(0);
		
		
		
	}
	
	private static void createOuterTable(Section section) throws BadElementException
	{
		checkYearExistance();
		
		PdfPTable outWrapper = new PdfPTable(1);
		outWrapper.setWidthPercentage(110);
		outWrapper.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell outerCell = new PdfPCell();
		if( !( first_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, first_year.get(0), semesters[0], semesters[1]));
		}
		if( !( second_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, second_year.get(0), semesters[0], semesters[1]));
		}
		if( !( third_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, third_year.get(0), semesters[0], semesters[1]));
		}
		if( !( forth_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, forth_year.get(0), semesters[0], semesters[1]));
		}
		if( !( fifth_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, fifth_year.get(0), semesters[0], semesters[1]));
		}if( !( sixth_year.isEmpty() ) )
		{
			outerCell.addElement(createTable(section, sixth_year.get(0), semesters[0], semesters[1]));
		}
		
		
		outWrapper.addCell(outerCell);
		section.add(outWrapper);
	}
	
	private static Element createTable(Section section, String year_param, String fall_semester_param, String spring_semester_param) throws BadElementException
	{   
		PdfPTable academic_year_table = new PdfPTable(2); // Define outer table with 2 columns
		academic_year_table.setWidthPercentage(100);
		academic_year_table.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		PdfPCell cell_1_ayt = new PdfPCell();  // Define 1.cell of the outer table
		
		PdfPTable fall_header_ayt = new PdfPTable(1);    // Define first cell has a table
		fall_header_ayt.addCell(new Phrase("COURSE NAME    " + year_param + "   " + fall_semester_param + "           C   G"));  // Add a phrase to the first cell's table
		fall_header_ayt.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		fall_header_ayt.setHorizontalAlignment(Element.ALIGN_TOP); // Set the alignment of the first cell's inner table as top
		fall_header_ayt.setWidthPercentage(100);  // Set the width percentage 
		fall_header_ayt.addCell(createSubjectList(section, firstYear_outer_map).toArray().toString());
		
		cell_1_ayt.addElement(fall_header_ayt); // Add the inner table to the first cell

		
		PdfPCell cell_2_ayt = new PdfPCell();  // Define 2.cell of the outer table
		
		PdfPTable spring_header_ayt = new PdfPTable(1);
		spring_header_ayt.addCell(new Phrase("COURSE NAME   " + year_param + "   " + spring_semester_param + "           C   G"));
		spring_header_ayt.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		spring_header_ayt.setHorizontalAlignment(Element.ALIGN_TOP);
		spring_header_ayt.setWidthPercentage(100);
		
		cell_2_ayt.addElement(spring_header_ayt);
		
		
		academic_year_table.addCell(cell_1_ayt);  // Add the first (FALL) cell to the outer table 
		academic_year_table.addCell(cell_2_ayt);  // Add the second (SPRING) cell to the outer table 
		
		section.add(academic_year_table);  // Add the whole table structure to the section
		createSubjectList(section, firstYear_outer_map);
//		createSubjectList(section);
		
		return academic_year_table;
	}
	
	private static Section createSubjectList(Section listSection, Map<String, HashMap<String,String>> map)
	{
		Paragraph singleSubjectPrgf = new Paragraph();
		
		for( int i=1; i<=map.size(); i++ )
		{
//			if( map.get(String.valueOf(i)).get("semester").equals("fall") )
//			{
//				singleSubjectPrgf = new Paragraph(
//						map.get(String.valueOf(i)).get("subject_code") + "   " +
//						map.get(String.valueOf(i)).get("subject_name") + "   " +
//						map.get(String.valueOf(i)).get("credits") + "   " +
//						map.get(String.valueOf(i)).get("average")
//						);
//				singleSubjectPrgf.setIndentationLeft(0);
//			} else if( map.get(String.valueOf(i)).get("semester").equals("spring") ){
//				singleSubjectPrgf = new Paragraph(
//						map.get(String.valueOf(i)).get("subject_code") + "   " +
//						map.get(String.valueOf(i)).get("subject_name") + "   " +
//						map.get(String.valueOf(i)).get("credits") + "   " +
//						map.get(String.valueOf(i)).get("average")
//						);
//				singleSubjectPrgf.setIndentationLeft(50);
//			}
			
			if( map.get(String.valueOf(i)).get("semester").equals("fall") )
			{
				singleSubjectPrgf = new Paragraph(
						map.get(String.valueOf(i)).get("subject_code") + "   " +
						map.get(String.valueOf(i)).get("subject_name") + "   " +
						map.get(String.valueOf(i)).get("credits") + "   " +
						map.get(String.valueOf(i)).get("average")
						);
				singleSubjectPrgf.setIndentationLeft(0);
			}
			
			listSection.add(singleSubjectPrgf);  
		}
		return listSection;
		

	}
	
	
	
	/****************************************************************************
	 ******************************* Academic year ******************************
	 * To update 'Academic Year's first year implementation on each year passed,  
	 * just change the 'equals("XXXX-YYYY")' definition with a value '2008-2009'
	 * or '2009-2010' or etc. whichever the first academic year value registered
	 * in the database for a specific user. 
	 ****************************************************************************/	
	private static void setFirstYear()  // 2008-2009
	{
		Log.i("Total Trascript subjects",String.valueOf(outer_map.size()));

        int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2008-2009") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_1stYear.addAll( outer_map.get(key).values() );
            			
            			firstYear_inner_map.put( "subject_code" , list_1stYear.get(3) );
            			firstYear_inner_map.put( "subject_name" , list_1stYear.get(1) );
            			firstYear_inner_map.put( "semester" , list_1stYear.get(5) );
            			firstYear_inner_map.put( "year" , list_1stYear.get(2) );
            			firstYear_inner_map.put( "credits" , list_1stYear.get(0) );
            			firstYear_inner_map.put( "average" , list_1stYear.get(4) );
            			
            			firstYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) firstYear_inner_map);
            			firstYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list ",String.valueOf( list_1stYear.toString() ));
            			nodeCounter++;

            			list_1stYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("1st academic year total subject",String.valueOf(firstYear_outer_map.entrySet().size()));
//        Log.i("firstYear_outer_map.entrySet().toString()",firstYear_outer_map.entrySet().toString() );

	}

	private static void setSecondYear()  // 2009-2010
	{
		int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2009-2010") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_2ndYear.addAll( outer_map.get(key).values() );
            			
            			secondYear_inner_map.put( "subject_code" , list_2ndYear.get(3) );
            			secondYear_inner_map.put( "subject_name" , list_2ndYear.get(1) );
            			secondYear_inner_map.put( "semester" , list_2ndYear.get(5) );
            			secondYear_inner_map.put( "year" , list_2ndYear.get(2) );
            			secondYear_inner_map.put( "credits" , list_2ndYear.get(0) );
            			secondYear_inner_map.put( "average" , list_2ndYear.get(4) );
            			
            			secondYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) secondYear_inner_map);
            			secondYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list for 2009-2010",String.valueOf( list_2ndYear.toString() ));
            			nodeCounter++;

            			list_2ndYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("2nd academic year total subject",String.valueOf(secondYear_outer_map.entrySet().size()));
//        Log.i("secondYear_outer_map.entrySet().toString()",secondYear_outer_map.entrySet().toString() );

	}
	
	private static void setThirdYear()  // 2010-2011
	{
		int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2010-2011") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_3rdYear.addAll( outer_map.get(key).values() );
            			
            			thirdYear_inner_map.put( "subject_code" , list_3rdYear.get(3) );
            			thirdYear_inner_map.put( "subject_name" , list_3rdYear.get(1) );
            			thirdYear_inner_map.put( "semester" , list_3rdYear.get(5) );
            			thirdYear_inner_map.put( "year" , list_3rdYear.get(2) );
            			thirdYear_inner_map.put( "credits" , list_3rdYear.get(0) );
            			thirdYear_inner_map.put( "average" , list_3rdYear.get(4) );
            			
            			thirdYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) thirdYear_inner_map);
            			thirdYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list for 2010-2011",String.valueOf( list_3rdYear.toString() ));
            			nodeCounter++;

            			list_3rdYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("3rd academic year total subject",String.valueOf(thirdYear_outer_map.entrySet().size()));
//        Log.i("thirdYear_outer_map.entrySet().toString()",thirdYear_outer_map.entrySet().toString() );

	}

	private static void setForthYear()  // 2011-2012
	{
		int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2011-2012") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_4thYear.addAll( outer_map.get(key).values() );
            			
            			forthYear_inner_map.put( "subject_code" , list_4thYear.get(3) );
            			forthYear_inner_map.put( "subject_name" , list_4thYear.get(1) );
            			forthYear_inner_map.put( "semester" , list_4thYear.get(5) );
            			forthYear_inner_map.put( "year" , list_4thYear.get(2) );
            			forthYear_inner_map.put( "credits" , list_4thYear.get(0) );
            			forthYear_inner_map.put( "average" , list_4thYear.get(4) );
            			
            			forthYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) forthYear_inner_map);
            			forthYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list for 2011-2012",String.valueOf( list_4thYear.toString() ));
            			nodeCounter++;

            			list_4thYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("4th academic year total subject",String.valueOf(forthYear_outer_map.entrySet().size()));
//        Log.i("forthYear_outer_map.entrySet().toString()",forthYear_outer_map.entrySet().toString() );

	}
	
	private static void setFifthYear()  // 2012-2013
	{
		int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2012-2013") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_5thYear.addAll( outer_map.get(key).values() );
            			
            			fifthYear_inner_map.put( "subject_code" , list_5thYear.get(3) );
            			fifthYear_inner_map.put( "subject_name" , list_5thYear.get(1) );
            			fifthYear_inner_map.put( "semester" , list_5thYear.get(5) );
            			fifthYear_inner_map.put( "year" , list_5thYear.get(2) );
            			fifthYear_inner_map.put( "credits" , list_5thYear.get(0) );
            			fifthYear_inner_map.put( "average" , list_5thYear.get(4) );
            			
            			fifthYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) fifthYear_inner_map);
            			fifthYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list for 2012-2013",String.valueOf( list_5thYear.toString() ));
            			nodeCounter++;

            			list_5thYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("5th academic year total subject",String.valueOf(fifthYear_outer_map.entrySet().size()));
//        Log.i("fifthYear_outer_map.entrySet().toString()",fifthYear_outer_map.entrySet().toString() );

	}
	
	// Extra year (optional)
	private static void setSixthYear()  // 2013-2014
	{
		int nodeCounter = 1;
        
        // Iterate over the outer map
        for(String key : outer_map.keySet())
        {
            for(Entry<String, String> innerEntry : outer_map.get(key).entrySet())
            {
            	if( innerEntry.getValue().equals("2013-2014") )
            	{
            		if( outer_map.get(key).values().contains(innerEntry.getValue()) )
            		{
            			list_6thYear.addAll( outer_map.get(key).values() );
            			
            			sixthYear_inner_map.put( "subject_code" , list_6thYear.get(3) );
            			sixthYear_inner_map.put( "subject_name" , list_6thYear.get(1) );
            			sixthYear_inner_map.put( "semester" , list_6thYear.get(5) );
            			sixthYear_inner_map.put( "year" , list_6thYear.get(2) );
            			sixthYear_inner_map.put( "credits" , list_6thYear.get(0) );
            			sixthYear_inner_map.put( "average" , list_6thYear.get(4) );
            			
            			sixthYear_outer_map.put(String.valueOf(nodeCounter), (HashMap<String, String>) sixthYear_inner_map);
            			sixthYear_inner_map = new HashMap<String, String>();

//            			Log.i(nodeCounter+". Subject, added to the temporary list for 2013-2014",String.valueOf( list_6thYear.toString() ));
            			nodeCounter++;

            			list_6thYear.clear();
            			
            		}
            	}
            }
        }

        Log.i("6th academic year total subject",String.valueOf(sixthYear_outer_map.entrySet().size()));
//        Log.i("sixthYear_outer_map.entrySet().toString()",sixthYear_outer_map.entrySet().toString() );

	}
	
	private static void checkYearExistance()
	{	
		if( !( firstYear_outer_map.isEmpty() ) )
		{
			first_year.add(firstYear_outer_map.get("1").get("year"));
			first_year.add(semesters[0]);
			first_year.add(semesters[1]);
		} else {
			Log.e("First year does not exist","");
		}
		
		if( !( secondYear_outer_map.isEmpty() ) )
		{
			second_year.add(secondYear_outer_map.get("1").get("year"));
			second_year.add(semesters[0]);
			second_year.add(semesters[1]);
		} else {
			Log.e("Second year does not exist","");
		}
		
		if( !( thirdYear_outer_map.isEmpty() ) )
		{
			third_year.add(thirdYear_outer_map.get("1").get("year"));
			third_year.add(semesters[0]);
			third_year.add(semesters[1]);
		} else {
			Log.e("third year does not exist","");
		}
		
		if( !( forthYear_outer_map.isEmpty() ) )
		{
			forth_year.add(forthYear_outer_map.get("1").get("year"));
			forth_year.add(semesters[0]);
			forth_year.add(semesters[1]);
		} else {
			Log.e("forth year does not exist","");
		}
		
		if( !( fifthYear_outer_map.isEmpty() ) )
		{
			fifth_year.add(fifthYear_outer_map.get("1").get("year"));
			fifth_year.add(semesters[0]);
			fifth_year.add(semesters[1]);
		} else {
			Log.e("fifth year does not exist","");
		}
		
		if( !( sixthYear_outer_map.isEmpty() ) )
		{
			sixth_year.add(sixthYear_outer_map.get("1").get("year"));
			sixth_year.add(semesters[0]);
			sixth_year.add(semesters[1]);
		} else {
			Log.e("sixth year does not exist","");
		}
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) 
	{
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	  }

}
