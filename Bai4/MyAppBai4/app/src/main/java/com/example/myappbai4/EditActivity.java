package com.example.myappbai4;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private EditText etJob, etName, etPhone, etEmail, etAddress, etHomePage;
    private static final int CAMERA_REQUEST_CODE = 100;

    // Đường dẫn tuyệt đối của file ảnh hiện tại
    private String currentPhotoPath;

    // 1. Khai báo Launcher cho Camera
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Khi chụp ảnh thành công, ảnh đã được lưu vào đường dẫn currentPhotoPath
                    if (currentPhotoPath != null) {
                        setPic(imgAvatar);
                        Toast.makeText(this, "Đã lưu ảnh full-size.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu người dùng hủy, xóa file rỗng đã tạo trước đó
                    if (currentPhotoPath != null) {
                        new File(currentPhotoPath).delete();
                        currentPhotoPath = null;
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        // Tham chiếu tới các View
        imgAvatar = findViewById(R.id.imgAvatar);
        etJob = findViewById(R.id.etJob);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etHomePage = findViewById(R.id.etHomePage);
        Button btnSave = findViewById(R.id.btnSave);
        ImageView ivCam = findViewById(R.id.ivCam);

        Intent intent = getIntent();
        if (intent != null) {
            etJob.setText(intent.getStringExtra("job"));
            etName.setText(intent.getStringExtra("name"));
            etPhone.setText(intent.getStringExtra("phone"));
            etEmail.setText(intent.getStringExtra("email"));
            etAddress.setText(intent.getStringExtra("address"));
            etHomePage.setText(intent.getStringExtra("homePage"));

            // Nếu có đường dẫn ảnh cũ được truyền vào, load ảnh đó
            String initialPath = intent.getStringExtra("avatarPath");
            if (initialPath != null) {
                currentPhotoPath = initialPath; // Cập nhật đường dẫn hiện tại
                setPic(imgAvatar);
            }
        }

        ivCam.setOnClickListener(v -> {
            // Kiểm tra và xin quyền truy cập camera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Xin quyền nếu chưa được cấp
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                // Đã có quyền, mở camera
                openCamera();
            }
        });

        btnSave.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditActivity.this, MainActivity.class);

            // 1. Đưa các trường dữ liệu text về MainActivity
            intent1.putExtra("job", etJob.getText().toString());
            intent1.putExtra("name", etName.getText().toString());
            intent1.putExtra("phone", etPhone.getText().toString());
            intent1.putExtra("email", etEmail.getText().toString());
            intent1.putExtra("address", etAddress.getText().toString());
            intent1.putExtra("homePage", etHomePage.getText().toString());

            // 2. TRUYỀN ĐƯỜNG DẪN ẢNH VỀ MainActivity
            if (currentPhotoPath != null) {
                intent1.putExtra("avatarPath", currentPhotoPath);
            }

            startActivity(intent1);
            finish(); // Kết thúc EditActivity
        });
    }

    // Phương thức tạo file ảnh tạm thời để lưu ảnh full-size
    private File createImageFile() throws IOException {
        // Tạo tên file ảnh dựa trên dấu thời gian
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Lấy thư mục Pictures (thư mục này được định nghĩa trong file_paths.xml)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Tạo file ảnh
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Lưu đường dẫn file: đây là đường dẫn tuyệt đối để load ảnh sau này
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Phương thức mở camera và cung cấp URI cho camera
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Xử lý lỗi khi tạo file
                Toast.makeText(this, "Lỗi khi tạo file ảnh", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                // Lấy URI từ FileProvider (quan trọng cho Android 7+)
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);

                // Gửi URI này cho ứng dụng camera, nó sẽ lưu ảnh vào đây
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Khởi chạy camera bằng launcher mới
                takePictureLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng camera", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức load ảnh full-size và đặt vào ImageView
    private void setPic(ImageView imageView) {
        if (currentPhotoPath == null) return;

        // Lấy kích thước của ImageView
        int targetW = imageView.getWidth() > 0 ? imageView.getWidth() : 100;
        int targetH = imageView.getHeight() > 0 ? imageView.getHeight() : 100;

        // Lấy kích thước ảnh thực tế
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Xác định tỷ lệ co dãn
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        if (scaleFactor <= 0) scaleFactor = 1; // Đảm bảo tỷ lệ không âm hoặc bằng 0

        // Decode bitmap với tỷ lệ đã xác định để tiết kiệm bộ nhớ
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    // Xử lý kết quả xin quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Quyền truy cập camera bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
