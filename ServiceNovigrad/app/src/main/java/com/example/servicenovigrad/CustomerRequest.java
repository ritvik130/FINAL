package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.data.Class.Address;
import com.example.servicenovigrad.data.Class.Customer;
import com.example.servicenovigrad.data.Class.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CustomerRequest extends AppCompatActivity {
    TextView dobView,appointmentDateView, imageCaptionView;
    String serviceName, serviceId;
    LinearLayout imageContainer;
    RadioGroup licenseTypeGroup,timeslotsGroup;
    Button submitBtn;
    EditText editTextFirstName, editTextLastName, editTextStreet, editTextNumber, editTextTown, editTextZipcode;
    ImageButton uploadBtn;
    static final int PICK_IMAGE_CAMERA = 0000, PICK_IMAGE_GALLERY = 1111, MY_CAMERA_PERMISSION_CODE = 2222;
    String imageData, mProfileImagedownloadURL;
    byte[] BYTE;
    StorageReference storageReference;
    Bitmap mCompressedBitmap;
    List<byte[]> bytes;
    List<String> imgURLs;
    DatabaseReference databaseRequests;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request);

        dobView = findViewById(R.id.dob);
        appointmentDateView = findViewById(R.id.appointmentDate);
        imageCaptionView = findViewById(R.id.imageCaption);
        licenseTypeGroup = findViewById(R.id.licenseTypeGroup);
        timeslotsGroup = findViewById(R.id.timeslotsGroup);
        submitBtn = findViewById(R.id.submitBtn);
        editTextFirstName = findViewById(R.id.firstname);
        editTextLastName = findViewById(R.id.lastname);
        editTextStreet = findViewById(R.id.street);
        editTextNumber = findViewById(R.id.number);
        editTextTown = findViewById(R.id.town);
        editTextZipcode = findViewById(R.id.zipcode);
        uploadBtn = findViewById(R.id.uploadBtn);
        imageContainer = findViewById(R.id.imageContainer);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseRequests = FirebaseDatabase.getInstance().getReference("customer_requests");
        serviceName = getIntent().getExtras().getString("serviceName");
        serviceId = getIntent().getExtras().getString("serviceId");

        bytes = new ArrayList<>();
        imgURLs = new ArrayList<>();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        Log.d("TOKEN", token);
                    }
                });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        if (serviceName.equals("Driver Licence")) {
            licenseTypeGroup.setVisibility(View.VISIBLE);
            imageCaptionView.setText(getResources().getString(R.string.residence_proof));
        } else if (serviceName.equals("Health Card")) {
            imageCaptionView.setText(getResources().getString(R.string.residence_proof) + "\n" + getResources().getString(R.string.status_proof));
        } else if (serviceName.equals("Photo ID")) {
            imageCaptionView.setText(getResources().getString(R.string.residence_proof) + "\n" + getResources().getString(R.string.photo));
        } else {
            licenseTypeGroup.setVisibility(View.VISIBLE);
        }
        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerRequest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day, monthh;
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }
                        if (month + 1 < 10) {
                            monthh = "0" + (month + 1);
                        } else {
                            monthh = "" + (month + 1);
                        }
                        switch (monthh) {
                            case "01":
                                monthh = "Jan";
                                break;
                            case "02":
                                monthh = "Feb";
                                break;
                            case "03":
                                monthh = "Mar";
                                break;
                            case "04":
                                monthh = "Apr";
                                break;
                            case "05":
                                monthh = "May";
                                break;
                            case "06":
                                monthh = "Jun";
                                break;
                            case "07":
                                monthh = "Jul";
                                break;
                            case "08":
                                monthh = "Aug";
                                break;
                            case "09":
                                monthh = "Sep";
                                break;
                            case "10":
                                monthh = "Oct";
                                break;
                            case "11":
                                monthh = "Nov";
                                break;
                            case "12":
                                monthh = "Dec";
                                break;
                            default:
                                monthh = "";
                        }
                        dobView.setText(day + "-" + monthh + "-" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });appointmentDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerRequest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day, monthh;
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }
                        if (month + 1 < 10) {
                            monthh = "0" + (month + 1);
                        } else {
                            monthh = "" + (month + 1);
                        }
                        switch (monthh) {
                            case "01":
                                monthh = "Jan";
                                break;
                            case "02":
                                monthh = "Feb";
                                break;
                            case "03":
                                monthh = "Mar";
                                break;
                            case "04":
                                monthh = "Apr";
                                break;
                            case "05":
                                monthh = "May";
                                break;
                            case "06":
                                monthh = "Jun";
                                break;
                            case "07":
                                monthh = "Jul";
                                break;
                            case "08":
                                monthh = "Aug";
                                break;
                            case "09":
                                monthh = "Sep";
                                break;
                            case "10":
                                monthh = "Oct";
                                break;
                            case "11":
                                monthh = "Nov";
                                break;
                            case "12":
                                monthh = "Dec";
                                break;
                            default:
                                monthh = "";
                        }
                        appointmentDateView.setText(day + "-" + monthh + "-" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton1 = findViewById(licenseTypeGroup.getCheckedRadioButtonId());
                RadioButton radioButton2 = findViewById(timeslotsGroup.getCheckedRadioButtonId());
                if (editTextFirstName.getText().toString().isEmpty()) {
                    editTextFirstName.setError("First Name is required");
                } else if (editTextLastName.getText().toString().isEmpty()) {
                    editTextLastName.setError("Last Name is required");
                } else if (editTextStreet.getText().toString().isEmpty()) {
                    editTextStreet.setError("Street is required");
                } else if (editTextNumber.getText().toString().isEmpty()) {
                    editTextNumber.setError("Number is required");
                } else if (editTextTown.getText().toString().isEmpty()) {
                    editTextTown.setError("Town is required");
                } else if (editTextZipcode.getText().toString().isEmpty()) {
                    editTextZipcode.setError("Zipcode is required");
                }else if(appointmentDateView.getText().toString().isEmpty()){
                    appointmentDateView.setError("Appointment Date is required");
                }else if(radioButton2.getText().toString().isEmpty()){
                    ((RadioButton)licenseTypeGroup.getChildAt(0)).setError("Time Slot is required!");
                } else {
                    if (serviceName.equals("Driver Licence")) {
                        if (bytes.size() <= 0) {
                            imageCaptionView.setError("Image Required");
                        }else if(radioButton1.getText().toString().isEmpty()){
                            ((RadioButton)licenseTypeGroup.getChildAt(0)).setError("License Type is required!");
                        }else{
                            uploadImage();
                        }
                    }else if (serviceName.equals("Health Card") || serviceName.equals("Photo ID")) {
                        if (bytes.size() < 2) {
                            imageCaptionView.setError("Both Images Required");
                        }else{
                            uploadImage();
                        }
                    }else{
                        uploadImage();
                    }
                }
            }
        });
    }

    public void chooseImage() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pic_picker_dialog);
        dialog.findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.galleryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                dialog.dismiss();
            }
        });
        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.image_dialog_background);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            imageData = filePath.toString();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                BYTE = compressInJPEG(bitmap);
                Log.d("bittt", BYTE.length + "");
                mCompressedBitmap = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), mCompressedBitmap);
                roundedBitmapDrawable.setCircular(true);
                roundedBitmapDrawable.setCornerRadius(6);
                ImageView imageView = new ImageView(CustomerRequest.this);
                imageView.setImageDrawable(roundedBitmapDrawable);
                if (imageContainer.getChildCount() >= 2) {
                    imageContainer.removeAllViews();
                    bytes.clear();
                }
                imageContainer.addView(imageView);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK
                && data != null) {
            imageData = "camera";
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            BYTE = compressInJPEG(bitmap);
            mCompressedBitmap = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), mCompressedBitmap);
            roundedBitmapDrawable.setCircular(true);
            roundedBitmapDrawable.setCornerRadius(6);
            ImageView imageView = new ImageView(CustomerRequest.this);
            imageView.setImageDrawable(roundedBitmapDrawable);
            if (imageContainer.getChildCount() >= 2) {
                imageContainer.removeAllViews();
                bytes.clear();
            }
            imageContainer.addView(imageView);
        }
        bytes.add(BYTE);

    }

    private byte[] compressInJPEG(Bitmap bitmap) {
        mCompressedBitmap = getScaledDownBitmap(bitmap, 300, false);
        Log.d("bittt", "Compressed: " + mCompressedBitmap.getWidth() + "X" + mCompressedBitmap.getHeight());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mCompressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] mImageBytes = outputStream.toByteArray();
//        byte encodedImage[] = Base64.encode(mImageBytes, Base64.DEFAULT);
//        String encodedImage = Base64.encodeToString(mImageBytes, Base64.DEFAULT);
        return mImageBytes;
    }

    public Bitmap getScaledDownBitmap(Bitmap bitmap, int threshold, boolean isNecessaryToKeepOrig) {
        Log.d("bittt", "Original: " + bitmap.getWidth() + "X" + bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;

        if (width > height && width > threshold) {
            newWidth = threshold;
            newHeight = (int) (height * (float) newWidth / width);
        }

        if (width > height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        if (width < height && height > threshold) {
            newHeight = threshold;
            newWidth = (int) (width * (float) newHeight / height);
        }

        if (width < height && height <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        if (width == height && width > threshold) {
            newWidth = threshold;
            newHeight = newWidth;
        }
        if (width > height && width > threshold) {
            newWidth = threshold;
            newHeight = newWidth;
        }
        if (height > width && height > threshold) {
            newWidth = threshold;
            newHeight = newWidth;
        }

        if (width == height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        return getResizedBitmap(bitmap, newWidth, newHeight, isNecessaryToKeepOrig);
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, boolean isNecessaryToKeepOrig) {
        Log.d("bittt", "NewWidthHeight: " + newWidth + "X" + newHeight);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (!isNecessaryToKeepOrig) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    private void uploadImage() {
        if (imgURLs.size() > 0)
            imgURLs.clear();
        for (byte[] bytes1 : bytes) {
            StorageReference reference = storageReference.child(UUID.randomUUID().toString());
            reference.putBytes(bytes1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mProfileImagedownloadURL = uri.toString();
                            imgURLs.add(mProfileImagedownloadURL);
                            if (imgURLs.size() == 2) {
                                save();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CustomerRequest.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                }
            });
        }

    }

    public void save() {
        Customer customer = new Customer();
        customer.setUsername(Common.userName);
        customer.setFirstName(editTextFirstName.getText().toString());
        customer.setLastName(editTextLastName.getText().toString());
        customer.setDob(dobView.getText().toString());
        Address address = new Address();
        address.setStreet(editTextStreet.getText().toString());
        address.setNumber(editTextNumber.getText().toString());
        address.setTown(editTextTown.getText().toString());
        address.setZipCode(editTextZipcode.getText().toString());
        customer.setAddress(address);
        Request request = new Request();
        request.setCustomer(customer);
        request.setImageList(imgURLs);
        request.setStatus("Pending");
        request.setServiceId(serviceId);
        request.setDate(appointmentDateView.getText().toString());
        request.setTime(((RadioButton)findViewById(timeslotsGroup.getCheckedRadioButtonId())).getText().toString());
        request.setToken(token);
        String id = databaseRequests.child(Common.userName).push().getKey();
        request.setId(id);
        databaseRequests.child(Common.userName).child(id).setValue(request);

        Intent intent = new Intent(getApplicationContext(), WelcomePageUsers.class); //Directs to Employee Screen
        startActivityForResult(intent, 0);

    }
}