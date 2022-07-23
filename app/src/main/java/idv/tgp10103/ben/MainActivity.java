package idv.tgp10103.ben;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    private static final String TAG = "TAG_MainActivity";
//    private ImageView imageView1, imageView2, imageView3, imageView4;
//    private EditText etHeadline, etDescription;
//    private ImageButton button;
//    private File file;
//    private ActivityResultLauncher<Uri> takePicLauncher;
//    private static final String FILENAME = "userscripts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        takePicLauncher = getTakePicLauncher();
//        findViews();
//        handleButton();
//        load();
    }

//    private ActivityResultLauncher<Uri> getTakePicLauncher() {
//        return registerForActivityResult(
//                new ActivityResultContracts.TakePicture(), isOk -> {
//                    Bitmap bitmap = null;
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
//                        bitmap = BitmapFactory.decodeFile(file.getPath());
//                    } else {
//                        try {
//                            ImageDecoder.Source source = ImageDecoder.createSource(file);
//                            bitmap = ImageDecoder.decodeBitmap(source);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    imageView1.setImageBitmap(bitmap);
//                }
//        );
//    }
//
//    private void findViews() {
//        imageView1 = findViewById(R.id.imageView1);
//        imageView2 = findViewById(R.id.imageView2);
//        imageView3 = findViewById(R.id.imageView3);
//        imageView4 = findViewById(R.id.imageView4);
//        etHeadline = findViewById(R.id.etHeadline);
//        etDescription = findViewById(R.id.etDescription);
//        button = findViewById(R.id.button);
//    }
//
//    private void handleButton() {
//        imageView1.setOnClickListener(view -> {
//            try {
//                file = createImageFile();
//                // 8. 使用FileProvider建立Uri物件
//                Uri uri = FileProvider.getUriForFile(
//                        this,
//                        getPackageName() + ".fileProvider",
//                        file);
//                // 9. 執行
//                takePicLauncher.launch(uri);
//            } catch (ActivityNotFoundException | IOException e) {
//                Log.e(TAG, e.toString());
//            }
//        });
////        imageView2.setOnClickListener(view -> {
////            launcher2.launch(null);
////        });
////        imageView3.setOnClickListener(view -> {
////            launcher3.launch(null);
////        });
////        imageView4.setOnClickListener(view -> {
////            launcher4.launch(null);
////        });
//        button.setOnClickListener(view -> {
//            save();
//            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private void save() {
//        try (
//                FileOutputStream fos = openFileOutput(FILENAME,MODE_PRIVATE);
//                ObjectOutputStream oos = new ObjectOutputStream(fos)
//        ){
//            final String headline = String.valueOf(etHeadline.getText());
//            final String description = String.valueOf(etDescription.getText());
//            final UserScripts userscripts = new UserScripts(headline, description);
//            oos.writeObject(userscripts);
//        }catch (Exception e){
//            Log.e(TAG, e.toString());
//        }
//    }
//
//    private void load() {
//        try (
//                FileInputStream fis = openFileInput(FILENAME);
//                ObjectInputStream ois = new ObjectInputStream(fis)
//        ){
//            final UserScripts userscripts = (UserScripts) ois.readObject();
//            etHeadline.setText(String.valueOf(userscripts.getHeadline()));
//            etDescription.setText(String.valueOf(userscripts.getDescription()));
//        }catch (Exception e){
//            Log.e(TAG, e.toString());
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        // 6. 取得File物件，並設定路徑
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        return File.createTempFile(imageFileName, ".jpg", storageDir);
//    }
}