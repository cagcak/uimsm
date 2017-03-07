package iaau.mas.uimsm.pdf;

import iaau.mas.uimsm.fragment.home.Fragment_Success;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Current;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_General;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class GenerateSuccessReport extends PdfPageEventHelper
{
	public static String FILENAME = "SuccessReport_";
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/UIMSM/Success Report/";
	public static String FILE_ADDRESS = PATH + FILENAME;
	
	public static String BACKGROUND_IMAGE;
	
	private static Bundle extras;
	Context mContext;
	private static Map<String, HashMap<String, String>> outer_map = new HashMap<String, HashMap<String, String>>();
    @SuppressWarnings("unused")
	private static Map<String, String> inner_map = new HashMap<String, String>();
	
    private static String userName;
    private static String userGroup;
	
    private static Font catFontSmall = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.GRAY);
	private static Font catFontBig = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.GRAY);
	@SuppressWarnings("unused")
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	@SuppressWarnings("unused")
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	@SuppressWarnings("unused")
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	
	
	
	public void generatePDF()
	{
		File directory = new File(PATH);    
        directory.mkdirs();
		
        getUserCredentials();
        
        String token = outer_map.get("1").get("semester") + "_" + outer_map.get("1").get("year"); 
        FILE_ADDRESS = FILE_ADDRESS + token + ".pdf";
        
		try{
			Document document = new Document();
		    PdfWriter.getInstance(document, new FileOutputStream(FILE_ADDRESS));
		    document.open();
		    addMetaData(document);
		    addTitlePage(document);
		    addInfo(document);
		    createTable(document);
//		    addContent(document);
//		    document.add( new Chunk("testing")); 
		    document.close();
		} catch (Exception e) {
		      e.printStackTrace();
	    }
	}
	
	
	
	@SuppressWarnings("unchecked")
	private static void getUserCredentials()
	{
		Fragment_Success bundleReference = new Fragment_Success();
		extras = bundleReference.sendUserBundle();
		outer_map = (Map<String, HashMap<String, String>>) extras.getSerializable("outer");
		inner_map = (Map<String, String>) extras.getSerializable("inner");
		
		userName = Fragment_MyInformation_Current.fullname;
		userGroup = Fragment_MyInformation_General.group;
	}
	
	private static void addMetaData(Document document) 
	{
	    document.addTitle("New Technologies Faculty - Department of Computer Engineering");
	    document.addSubject("Success Report");
	}

	private static Paragraph addTitlePage(Document document) throws DocumentException
	{
		Paragraph preface = new Paragraph();

		String above = "                                     INTERNATIONAL ATATURK ALATOO UNIVERSITY";
		String below = "                STUDENT`S SUCCESS CONTROL FORM";
		
		preface.add(new Paragraph((Element.ALIGN_CENTER), above, catFontSmall));
		addEmptyLine(preface, 2);   // two empty lines
	    preface.add(new Paragraph((Element.ALIGN_CENTER), below, catFontBig));
	    
	    document.add(preface);
	    
		return preface;
	}
	
	private static Paragraph addInfo(Document document) throws DocumentException
	{
		Paragraph info = new Paragraph();
		
		addEmptyLine(info, 2);
		info.add(new Paragraph((Element.ALIGN_CENTER), "Group:" + "                  " + userGroup + "                      " + "Student:        " + userName));
		addEmptyLine(info, 1);
		info.add(new Paragraph((Element.ALIGN_CENTER), "Academic Year:    " + outer_map.get("1").get("year") + "                  Semester:      " + outer_map.get("1").get("semester")));
		addEmptyLine(info, 1);
		
		document.add(info);
		
		return info;
	}
	
	private static Element createTable(Document document) throws DocumentException
	{
//		Paragraph singleSubjectRow = new Paragraph();
		
		PdfPTable successTable = new PdfPTable(8);
		successTable.setWidths(new int[]{ 1, 9, 2, 3, 2, 3, 3, 3 });
		successTable.setWidthPercentage(110);
		successTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell cell = new PdfPCell(new Phrase("#"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    successTable.setHeaderRows(1);
		
	    cell = new PdfPCell(new Phrase("Subjects"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setPaddingLeft(10);
	    cell.setPaddingRight(10);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Hours"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Midterm"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Final"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Make Up"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Average"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    
	    cell = new PdfPCell(new Phrase("Attendance"));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    successTable.addCell(cell);
	    

	    for( int i=1; i<=outer_map.size(); i++ )
	    {
	    	successTable.addCell( String.valueOf(i) );
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("subject_name") );
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("hours") );
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("midterm") );
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("final") );
	    	successTable.addCell( "IP" ); // makeup has been removed out of circulation
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("average") );
	    	successTable.addCell( outer_map.get(String.valueOf(i)).get("attandance") );
	    }
		
//		section.add(successTable);
	    
		document.add(successTable);
		return successTable;
		
//		return section;
	}
	
	/*@Override
	public void onEndPage(PdfWriter writer, Document document) 
	{		
		Uri path = Uri.parse("android.resource://iaau.mas.uimsm/drawable/" + R.drawable.pdf_iaau_logo);
		BACKGROUND_IMAGE = path.getPath();
		Log.i(BACKGROUND_IMAGE,"");
		
		Image backImage = Image.getInstance(BACKGROUND_IMAGE);
		
		float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();

        writer.getDirectContent().addImage(backImage, width, 0, 0, height, 0, 0);
//		Context mContext;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pdf_iaau_logo);
	    bitmap.compress(Bitmap.CompressFormat.JPEG , 100, stream);
	    Image img;
	    float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();
	    try {
	        img = Image.getInstance(stream.toByteArray());
	        img.setAbsolutePosition(width, height);  // 0,0 ile değiştir

	        document.add(img);
	    } catch (BadElementException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (MalformedURLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (DocumentException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	}*/
	
	
	private static void addEmptyLine(Paragraph paragraph, int number) 
	{
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	}
	

}
