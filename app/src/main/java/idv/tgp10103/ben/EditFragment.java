package idv.tgp10103.ben;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.app.Activity.RESULT_OK;

public class EditFragment extends Fragment {
    private Activity activity;
    private static final String TAG = "TAG_MainActivity";
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private EditText etHeadline, etDescription;
    private ImageButton button;
    private static final String FILENAME = "userscripts";
    private Uri contentUri;
    private FirebaseStorage storage;

    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::takePictureResult);

    ActivityResultLauncher<Intent> cropPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::cropPictureResult);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleButton();
        load();
        
    }

    private void findViews(View view) {
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        etHeadline = view.findViewById(R.id.etHeadline);
        etDescription = view.findViewById(R.id.etDescription);
        button = view.findViewById(R.id.button);
    }

    private void handleButton() {
        imageView1.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (dir != null && !dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e(TAG, getString(R.string.textDirNotCreated));
                    return;
                }
            }
            File file = new File(dir, "picture.jpg");
            contentUri = FileProvider.getUriForFile(
                    requireContext(), requireContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            try {
                takePictureLauncher.launch(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(requireContext(), R.string.textNoCameraAppFound,
                        Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(view -> {
            save();
            Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show();
        });
    }

    private void takePictureResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            crop(contentUri);
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        Intent cropIntent = UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .getIntent(requireContext());
        cropPictureLauncher.launch(cropIntent);
    }

    private void cropPictureResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri imageUri = UCrop.getOutput(result.getData());
            if (imageUri != null) {
                uploadImage(imageUri);
            }
        }
    }

    private void save() {
        try (
                FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                FileOutputStream fos = openFileOutput(FILENAME,MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            final String headline = String.valueOf(etHeadline.getText());
            final String description = String.valueOf(etDescription.getText());
            final UserScripts userscripts = new UserScripts(headline, description);
            oos.writeObject(userscripts);
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private void load() {
        try (
                FileInputStream fis = activity.openFileInput(FILENAME);
//                FileInputStream fis = openFileInput(FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ){
            final UserScripts userscripts = (UserScripts) ois.readObject();
            etHeadline.setText(String.valueOf(userscripts.getHeadline()));
            etDescription.setText(String.valueOf(userscripts.getDescription()));
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private void uploadImage(Uri imageUri) {
        // 取得storage根目錄位置
        StorageReference rootRef = storage.getReference();
        final String imagePath = getString(R.string.app_name) + "/images/" + System.currentTimeMillis();
        // 建立當下目錄的子路徑
        final StorageReference imageRef = rootRef.child(imagePath);
        // 將儲存在uri的照片上傳
        imageRef.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message = getString(R.string.textUploadSuccess);
                        Log.d(TAG, message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        // 下載剛上傳好的照片
                        downloadImage(imagePath);
                    } else {
                        String message = task.getException() == null ?
                                getString(R.string.textUploadFail) :
                                task.getException().getMessage();
                        Log.e(TAG, "message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 下載Firebase storage的照片
    private void downloadImage(String path) {
        final int ONE_MEGABYTE = 1024 * 1024;
        StorageReference imageRef = storage.getReference().child(path);
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        byte[] bytes = task.getResult();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView1.setImageBitmap(bitmap);
                    } else {
                        imageView1.setImageResource(R.drawable.save);
                        String message = task.getException() == null ?
                                getString(R.string.textDownloadFail) :
                                task.getException().getMessage();
                        Log.e(TAG, "message: " + message);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}