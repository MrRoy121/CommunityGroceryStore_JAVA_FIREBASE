package com.example.communitygrocerystoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.communitygrocerystoreapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExcelSheet extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    File file;
    Button btnUpDirectory;
    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;
    ListView lvInternalStorage;

    ArrayList<String> idd =new ArrayList<>();
    ArrayList<String> price =new ArrayList<>();
    private String[] FilePathStrings;
    ProgressBar progressBar;
    private String[] FileNameStrings;
    private File[] listFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_sheet);

        lvInternalStorage = findViewById(R.id.lvInternalStorage);
        btnUpDirectory = findViewById(R.id.btnUpDirectory);
        progressBar = findViewById(R.id.pbar);


        checkFilePermissions();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if (lastDirectory.equals(adapterView.getItemAtPosition(i))) {
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);
                    progressBar.setVisibility(View.VISIBLE);
                    readExcelData(lastDirectory);

                } else {
                    count++;
                    pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });

        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });

        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();
    }


    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");


        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                for (int c = 0; c < cellsCount; c++) {
                    if (c > 2) {
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                        Toast.makeText(this, "ERROR: Excel File Format is incorrect!", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        if(c==0){
                            idd.add(value);
                        }else{
                            price.add(value);
                        }
                        //Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        //sb.append(value + ", ");
                    }
                }

            }
            int q=0;
            for(String s:idd){
                int finalQ = q;
                FirebaseFirestore.getInstance().collection("Products").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        if(ds.exists()){
                            Map<String, String> pro = new HashMap<>();
                            pro.put("Name", ds.getString("Name"));
                            pro.put("Price", price.get(finalQ));
                            pro.put("Category", ds.getString("Category"));
                            pro.put("image", ds.getString("image"));
                            FirebaseFirestore.getInstance().collection("Products").document(s).set(pro);
                         }else{
                            Toast.makeText(getApplicationContext(), s + " Item doesn't Exists!", Toast.LENGTH_SHORT).show();
                        }
                        if(finalQ==idd.size()-1){
                            Toast.makeText(getApplicationContext(), "Items Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                q++;
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
        }
    }


    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "No Sq card Found", Toast.LENGTH_SHORT).show();
            } else {
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++) {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage());
        }
    }


    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}
