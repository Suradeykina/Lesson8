package su.radmi;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckZip {

    ClassLoader cl = CheckZip.class.getClassLoader();


    @Test
    @DisplayName("Archiving and reading files")
    void zip() throws Exception{
        String fio = "Тестовяк Андрейка Тестович";
        String namePdfVacation = "Отпуск по беременности.pdf";
        String nameXlsxTemplate = "Шаблон списка сотрудников КДП Барсиков.xlsx";
        String nameCsvTemplate= "Шаблон - структура организации.csv";

        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("src/test/resources/Lesson8.zip"));
                ZipInputStream zipInputStream = new ZipInputStream(cl.getResourceAsStream("Lesson8.zip"));
                InputStream isPdfVacation = cl.getResourceAsStream(namePdfVacation);
                InputStream isXlsxTemplate = cl.getResourceAsStream(nameXlsxTemplate);
                InputStream isCsvTemplate = cl.getResourceAsStream(nameCsvTemplate);
        )
        {
            //archive pdf file
            ZipEntry zipEntryPdfVacation = new ZipEntry(namePdfVacation);
            zipOutputStream.putNextEntry(zipEntryPdfVacation);
            byte[] fileContentPdfVacation = new byte[isPdfVacation.available()];
            isPdfVacation.read(fileContentPdfVacation);
            zipOutputStream.write(fileContentPdfVacation);

            //archive xlsx file
            ZipEntry zipEntryXlsxTemplate = new ZipEntry(nameXlsxTemplate);
            zipOutputStream.putNextEntry(zipEntryXlsxTemplate);
            byte[] fileContentXlsxTemplate = new byte[isXlsxTemplate.available()];
            isXlsxTemplate.read(fileContentXlsxTemplate);
            zipOutputStream.write(fileContentXlsxTemplate);

            //archive csv file
            ZipEntry zipEntryCsvTemplate = new ZipEntry(nameCsvTemplate);
            zipOutputStream.putNextEntry(zipEntryCsvTemplate);
            byte[] fileContentCsvTemplate = new byte[isCsvTemplate.available()];
            isCsvTemplate.read(fileContentCsvTemplate);
            zipOutputStream.write(fileContentCsvTemplate);

            //unpacking
          ZipEntry entry;
          while ((entry = zipInputStream.getNextEntry()) != null){
                      try (
                              InputStream is = cl.getResourceAsStream(entry.getName())
                      ) {
                          if (entry.getName().contains(namePdfVacation)) {
                          PDF pdfVacation = new PDF(is);
                          assertThat(pdfVacation.text).contains("Ф.И.О.: " + fio);
                      } else if (entry.getName().contains(nameXlsxTemplate)) {
                              XLS xlsxTemplate = new XLS(is);
                              assertThat(xlsxTemplate.excel
                                      .getSheetAt(0)
                                      .getRow(2)
                                      .getCell(0)
                                      .getStringCellValue())
                                      .isEqualTo("Барсиков");
                              assertThat(xlsxTemplate.excel
                                      .getSheetAt(0)
                                      .getRow(2)
                                      .getCell(1)
                                      .getStringCellValue())
                                      .isEqualTo("Александр");
                              assertThat(xlsxTemplate.excel
                                      .getSheetAt(0)
                                      .getRow(2)
                                      .getCell(2)
                                      .getStringCellValue())
                                      .isEqualTo("Олегович");
                          } else if (entry.getName().contains(nameCsvTemplate)) {
                              CSVReader csvReader = new CSVReader(new InputStreamReader(is));
                              List<String[]> contentCsv = csvReader.readAll();
                              assertThat(contentCsv).contains(
                                      new String[]{"Department", " Parent department code", " Department code"},
                                      new String[]{"Production department", " ", "dep_1/00001"}
                              );
                          }

                      }
          }




        }
    }

}
