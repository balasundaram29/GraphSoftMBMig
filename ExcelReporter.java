
import java.io.File;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class ExcelReporter {

    public static File getFilledReport(File in, File out) {
        try {

            Workbook wb = Workbook.getWorkbook(in);

            WritableWorkbook copy = Workbook.createWorkbook(out, wb);
            WritableSheet sheet = copy.getSheet(0);
            Label lbl = new Label(3,17,"Jxl");
            sheet.addCell(lbl);
            copy.write();
            copy.close();
            return out;

        } catch (Exception ex) {

            ex.printStackTrace();
            return out;
        }
    }
}
