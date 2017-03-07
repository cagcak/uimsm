package iaau.mas.uimsm.pdf;

import iaau.mas.uimsm.HomeActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.R.drawable;
import iaau.mas.uimsm.fragment.home.Fragment_Diploma;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Current;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_General;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class GenerateDiploma 
{
	public static String FILENAME = "Diploma Information.pdf";
	public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/UIMSM/Diploma/";
	public static String font_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/UIMSM/Font/";
	public static String FILE_ADDRESS = PATH + FILENAME;

	public static String BACKGROUND_IMAGE;
	
	static Context context;
	
	private static JSONObject infos = Fragment_Diploma.jsonDiplomInfo;
	private static String userName = Fragment_MyInformation_Current.fullname;
	
	private static Bundle extras;
	private static Map<String, String> map = new HashMap<String, String>();
	
	private static Font catFontSmall = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL, BaseColor.GRAY);
	private static Font catFontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD, BaseColor.BLACK);
	private static Font catFontBig = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.GRAY);
	@SuppressWarnings("unused")
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,Font.BOLD, BaseColor.GRAY);
	private static BaseFont cyrillicFont;
	private static Font cyrillic_SMALL;
	private static Font cyrillic_NORMAL;
	private static Font cyrillic_BIG;
	


	public GenerateDiploma() {
		super();
	}

	public void generatePDF() throws DocumentException, IOException
	{
		cyrillicFont = BaseFont.createFont(font_PATH + "times.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		FontFactory.register(font_PATH + "times.ttf");
		FontFactory.getFont("times.ttf", BaseFont.WINANSI, 12);
		cyrillicFont.getCodePagesSupported();
		cyrillic_SMALL = new Font(cyrillicFont, 8);
		cyrillic_NORMAL = new Font(cyrillicFont, 10);
		cyrillic_NORMAL = new Font(cyrillicFont, 14);
		
		File directory = new File(PATH);    
        directory.mkdirs();
        
        File fontPATH = new File(font_PATH);    
        fontPATH.mkdirs();
        
        getUserCredentials();
        
        try{
			Document document = new Document();
		    PdfWriter.getInstance(document, new FileOutputStream(FILE_ADDRESS));
		    document.open();
		    addMetaData(document);
		    createHeaderTable(document, cyrillic_NORMAL);
		    addTitlePage(document, cyrillic_BIG);
		    createBodyTable(document, cyrillic_NORMAL);
		    addBodyFooter(document, cyrillic_NORMAL);
		    addFooter(document, cyrillic_SMALL);
		    document.close();
		} catch (Exception e) {
		      e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("unchecked")
	private static void getUserCredentials()
	{
		Fragment_Diploma bundleRef = new Fragment_Diploma();
		extras = bundleRef.sendUserBundle();
		map = (Map<String, String>) extras.getSerializable("bundle_map");
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
	
	private static void addMetaData(Document document) throws JSONException 
	{
	    document.addTitle("New Technologies Faculty - Department of Computer Engineering");
	    document.addSubject("Diploma Information of " + map.get("idnumber"));
	}
	
	private static Element createHeaderTable(Document document, Font font) throws DocumentException, MalformedURLException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
//		Uri path = Uri.parse("android.resource://iaau.mas.uimsm/drawable/" + "pdf_iaau_logo");
//		BACKGROUND_IMAGE = path.getPath();C:\Windows\Fonts\Comic Sans MS
//		InputStream stream = context.getContentResolver().openInputStream(path);
//		Log.i(BACKGROUND_IMAGE,"");
		
		
//		    Class<drawable> res = R.drawable.class;
//		    Field field = res.getField("pdf_iaau_logo");
//		    @SuppressWarnings("unused")
//			int drawableId = field.getInt(null);
//		    Image image = Image.getInstance(field.toGenericString());
		
		
//		Image image = Image.getInstance(field);
		
		PdfPTable headerTable = new PdfPTable(3);
		headerTable.setWidths(new int[]{ 6, 3, 6 });
		headerTable.setWidthPercentage(90);
		headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		PdfPCell cell = new PdfPCell(new Phrase("ЭЛАРАЛЫК «АТАТҮРК – АЛАТОО» УНИВЕРСИТЕТИ", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
//        cell.setBackgroundColor(BaseColor.WHITE);
		headerTable.addCell(cell);
//		headerTable.setHeaderRows(1);
		
//		cell = new PdfPCell(image, true);
		cell = new PdfPCell(new Phrase("IMAGE", font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);
//        cell.setBackgroundColor(BaseColor.WHITE);
		headerTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("МЕЖДУНАРОДНЫЙ УНИВЕРСИТЕТ «АТАТЮРК – АЛАТОО»", font));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setBorder(PdfPCell.NO_BORDER);
//        cell.setBackgroundColor(BaseColor.WHITE);
	    headerTable.addCell(cell);
		
		
	    document.add(headerTable);
		return headerTable;
	}

	private static Paragraph addTitlePage(Document document, Font font) throws DocumentException
	{
		Paragraph preface = new Paragraph();

		String above = "                            International Atatürk Alatoo University";
		String below = "                       ULUSLARARASI ATATÜRK ALATOO ÜNİVERSİTESİ";
		String date = "                                                                                         Дата:" + getDate();
		String header = "                          ИНФОРМАЦИЯ ДЛЯ ДИПЛОМА";
		
		addEmptyLine(preface, 1);
		preface.add(new Paragraph((Element.ALIGN_CENTER), above, catFontNormal));
		addEmptyLine(preface, 1);   
	    preface.add(new Paragraph((Element.ALIGN_CENTER), below, catFontSmall));
	    addEmptyLine(preface, 1);
	    /*PdfPCell borderCell = new PdfPCell(new Paragraph("") );
	    borderCell.setBorder(Rectangle.OUT_BOTTOM);*/
	    final LineSeparator lineSeparator = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
	    preface.add(lineSeparator);
	    addEmptyLine(preface, 1);
	    
	    preface.add(new Paragraph((Element.ALIGN_RIGHT), date, subFont));
	    addEmptyLine(preface, 2);
	    
	    preface.add(new Paragraph((Element.ALIGN_RIGHT), header, font));
	    addEmptyLine(preface, 1);
	    
	    document.add(preface);
	    
		return preface;
	}

	private static Element createBodyTable(Document document, Font font) throws DocumentException, JSONException, IOException
	{
		String[] cellCyrillic =  {
				"ID студента",
				"Ф.И.О / Аты Жөнү [кыр.] ",
				"Ф.И.О / Аты Жөнү [рус.]",
				"Ф.И.О / Аты Жөнү [анг.]",
				"Дата рождения / Туулган күнү [д/м/г]",
				"Год получения аттестата / Аттестат алынган жыл",
				"Год поступления в университет / Университетке тапшырган жыл",
				"Паспорт №",
				"Адрес проживания / Жашаган адреси",
				"Группа / Группасы",
				"Телефон",
				"Название дипломной работы / Дипломдук иштин аталышы / Diploma Project Name",
				"Кыргызча",
				"По-русски",
		};
		
		
		
//		BaseFont bfComic = BaseFont.createFont(font_PATH + "comicbd.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//	FontFactory.register(font_PATH+"comicbd.ttf");
//	FontFactory.getFont("ComicSansMS", BaseFont.WINANSI, 12);
//	bfComic.getCodePagesSupported();
//	Font font = new Font(bfComic, 12);
        PdfPTable bodyTable = new PdfPTable(2);
        bodyTable.setWidths(new int[]{ 10, 10 });
        bodyTable.setWidthPercentage(110);
		bodyTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        

        PdfPCell cell = new PdfPCell(new Paragraph(cellCyrillic[0], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(getUserID()));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[1], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("fullname_ru")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);	
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[2], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("fullname_ru")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[3], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(userName + " " + map.get("middlename")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[4], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("birthday")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[5], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("year_graduate")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[6], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("")); // No record collected in database
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[7], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("passport_no")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[8], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("current_address")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[9], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(Fragment_MyInformation_General.group));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("e-mail"));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("")); // No record collected in database
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(cellCyrillic[10], font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(map.get("phone_no")));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell);
        
        PdfPCell cell2 = new PdfPCell(new Paragraph(cellCyrillic[11], font));
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2.setColspan(2);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph(cellCyrillic[12], font));
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph(map.get("thesis_kg")));
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph(cellCyrillic[13], font));
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph(map.get("thesis_ru"))); 
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph("In English "));
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);
        
        cell2 = new PdfPCell(new Paragraph(map.get("thesis_en"))); 
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        bodyTable.addCell(cell2);

        document.add(bodyTable);

		
		
		return bodyTable;
	}

	private static Paragraph addBodyFooter(Document document, Font font) throws DocumentException
	{
		Paragraph bodyFooter = new Paragraph();
		
		String text = "Подпись студента / Студенттин колу ________________";
		
		addEmptyLine(bodyFooter, 4);
		bodyFooter.add(new Paragraph((Element.ALIGN_LEFT), text, font));
		
		document.add(bodyFooter);
		
		return bodyFooter;
	}

	private static Paragraph addFooter(Document document, Font font) throws DocumentException
	{
		Paragraph footer = new Paragraph();
		
		String contactA = "Address: M.Gorky Street 1/8 720048 Bishkek, Kyrgyzstan (Дареги: Микр.Тунгуч, М.Горький 1/8 720048, Бишкек, Кыргызстан)";
		String contactB = "Tel: (996-312)63 14 23, 63 14 26; Fax: (996-312)63 04 09; E-mail: info@iaau.edu.kg; Web: http://www.iaau.edu.kg";
		
		addEmptyLine(footer, 3);
		
		final LineSeparator lineSeparator = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
	    footer.add(lineSeparator);
	    addEmptyLine(footer, 3);
	    
	    footer.add(new Paragraph((Element.ALIGN_CENTER), contactA, smallBold));
	    addEmptyLine(footer, 1);
	    footer.add(new Paragraph((Element.ALIGN_CENTER), contactB, smallBold));
		
		document.add(footer);
		
		
		return footer;
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) 
	{
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	}
	
}
