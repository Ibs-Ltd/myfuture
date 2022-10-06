package com.myfutureapp.dashboard.homeProfile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.homeProfile.model.UserProfilePicModel;
import com.myfutureapp.dashboard.homeProfile.presenter.UserProfilePresenter;
import com.myfutureapp.dashboard.homeProfile.ui.AcademicUserFragment;
import com.myfutureapp.dashboard.homeProfile.ui.BasicUserFragment;
import com.myfutureapp.dashboard.homeProfile.ui.PersonalUserFragment;
import com.myfutureapp.dashboard.homeProfile.ui.UserProfileView;
import com.myfutureapp.dashboard.homeProfile.ui.WorkExperinceUserFragment;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.login.DataHolder;
import com.myfutureapp.login.LoginActivity;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.GetFilePath;
import com.myfutureapp.util.Helper;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment implements UserProfileView {
    Uri outputFileUri;
    boolean userProfileEditable = true;
    private TabLayout tabLayout;
    private LinearLayout mainDataLL, loginLL;
    private CircleImageView iv_profilePic;
    private Fragment basicUserFragment, academicUserFragment, personalUserFragment, workExperinceUserFragment, activeFragment;
    private UserProfilePresenter userProfilePresenter;
    private ProgressDialog progressDialog;
    private String perviousUserName = "";
    private ImageView profileNameEditing;
    private TextView tv_userName;
    private boolean isLoaded = false;
    private TextView tv_mbile;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("isVisibleToUser", "true");
            if (!AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
                loginLL.setVisibility(View.VISIBLE);
                mainDataLL.setVisibility(View.GONE);
            } else {
                if (!isLoaded) {
                    if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.name != null) {
                        tv_userName.setText(DataHolder.getInstance().getUserProfileDataresponse.data.name);
                        perviousUserName = DataHolder.getInstance().getUserProfileDataresponse.data.name;
                    } else {
                        tv_userName.setHint("Add User Name");
                        perviousUserName = "";
                    }
                    if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.profile_picture != null) {
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.ic_vector_default_profile)
                                .error(R.drawable.ic_vector_default_profile);
                        Glide.with(getContext()).load(DataHolder.getInstance().getUserProfileDataresponse.data.profile_picture).apply(options).into(iv_profilePic);

                    }
                    if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.mobile != null) {
                        tv_mbile.setText(DataHolder.getInstance().getUserProfileDataresponse.data.mobile);
                    }
                    tabLayout.addTab(tabLayout.newTab().setText("Basic"));
                    tabLayout.addTab(tabLayout.newTab().setText("Academic"));
                    tabLayout.addTab(tabLayout.newTab().setText("Personal"));
                    tabLayout.addTab(tabLayout.newTab().setText("Work Exp"));

                    FragmentManager fragmentManager = getChildFragmentManager();
                    basicUserFragment = new BasicUserFragment();
                    academicUserFragment = new AcademicUserFragment();
                    personalUserFragment = new PersonalUserFragment();
                    workExperinceUserFragment = new WorkExperinceUserFragment();
                    fragmentManager.beginTransaction().add(R.id.frame_container, basicUserFragment, "basicUserFragment").show(basicUserFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.frame_container, academicUserFragment, "academicUserFragment").hide(academicUserFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.frame_container, personalUserFragment, "personalUserFragment").hide(personalUserFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.frame_container, workExperinceUserFragment, "workExperinceUserFragment").hide(workExperinceUserFragment).commitAllowingStateLoss();
                    activeFragment = basicUserFragment;
                    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            Helper.hideKeyboard(getActivity());
                            if (tab.getPosition() == 0) {
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                transaction.hide(activeFragment).show(basicUserFragment).commit();
                                activeFragment = basicUserFragment;
                                basicUserFragment.setUserVisibleHint(true);
//                                replaceFragment(basicUserFragment);
                            } else if (tab.getPosition() == 1) {
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                transaction.hide(activeFragment).show(academicUserFragment).commit();
                                activeFragment = academicUserFragment;
                                academicUserFragment.setUserVisibleHint(true);
//                                replaceFragment(academicUserFragment);
                            } else if (tab.getPosition() == 2) {
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                transaction.hide(activeFragment).show(personalUserFragment).commit();
                                activeFragment = personalUserFragment;
                                personalUserFragment.setUserVisibleHint(true);
//                                replaceFragment(personalUserFragment);
                            } else if (tab.getPosition() == 3) {
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                transaction.hide(activeFragment).show(workExperinceUserFragment).commit();
                                activeFragment = workExperinceUserFragment;
                                workExperinceUserFragment.setUserVisibleHint(true);
//                                replaceFragment(workExperinceUserFragment);
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                    tabLayout.getTabAt(0);
                    mainDataLL.setVisibility(View.VISIBLE);
                    loginLL.setVisibility(View.GONE);
                    isLoaded = true;
                }
            }
        } else {
            Log.e("isVisibleToUser", "false");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        inUi(root);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("uploading picture");
        progressDialog.setCancelable(false);
        userProfilePresenter = new UserProfilePresenter(getContext(), UserProfileFragment.this);

        return root;
    }


    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case AppConstants.PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkSelfPermission(Manifest.permission.CAMERA, AppConstants.PERMISSION_REQ_ID_CAMERA) &&
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, AppConstants.PERMISSION_REQ_ID_WRITE) &&
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE, AppConstants.PERMISSION_REQ_ID_READ)) {
                        showDialog();
                    }
//                    showDialog();
                } else {
                    showAlertDialogForDenyPermission("Without this permission the app is unable to add pic, so please allow the permission");
                }
                break;
            }

        }
    }

    private void showAlertDialogForDenyPermission(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Permission Denied");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orange));
    }


    private void inUi(View view) {
        setUserVisibleHint(false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mainDataLL = (LinearLayout) view.findViewById(R.id.mainDataLL);
        loginLL = (LinearLayout) view.findViewById(R.id.loginLL);
        iv_profilePic = (CircleImageView) view.findViewById(R.id.iv_profilePic);
        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        tv_mbile = (TextView) view.findViewById(R.id.tv_mbile);
        CircleImageView profilePicEditing = (CircleImageView) view.findViewById(R.id.profilePicEditing);
        profilePicEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA, AppConstants.PERMISSION_REQ_ID_CAMERA) &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, AppConstants.PERMISSION_REQ_ID_WRITE) &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE, AppConstants.PERMISSION_REQ_ID_READ)) {
                    showDialog();
                }

            }
        });
        profileNameEditing = (ImageView) view.findViewById(R.id.profileNameEditing);
        tv_userName.setEnabled(false);
        profileNameEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfileEditable) {
                    tv_userName.setEnabled(true);
                    tv_userName.setBackground(getContext().getResources().getDrawable(R.drawable.simple_white_background));
                    userProfileEditable = false;
                    profileNameEditing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_tick_profile));
                    tv_userName.requestFocus();
                } else {
                    if (tv_userName.getText().toString().trim().equalsIgnoreCase("") && !perviousUserName.equalsIgnoreCase("")) {
                        showMessage("Please enter your name.");
                        return;
                    }

                    if (tv_userName.getText().toString().trim().equalsIgnoreCase(perviousUserName)) {
                        showMessage("nothing to update");
                        tv_userName.setEnabled(false);
                        tv_userName.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
                        profileNameEditing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
                        userProfileEditable = true;

                    } else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("name", tv_userName.getText().toString().trim());
                        userProfilePresenter.callUserProfileUploadApi(jsonObject);
                    }
                }
            }
        });

        loginLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.
                        FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });


    }

    private void showDialog() {
        final CharSequence[] options = new CharSequence[]{"Take Photo", "Choose from Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Add Media");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    captureCameraImage(99);
                    dialog.cancel();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 13);
                    dialog.cancel();
                }
            }
        });
        builder.show();
    }

    void captureCameraImage(int code) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        outputFileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, code);
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 99: {
                try {
                    String selectedContentPath = getRealPathFromURI(outputFileUri);
                    if (new File(selectedContentPath).exists()) {
                        Log.e("filepath!*", "" + selectedContentPath);
                        final File file = new File(selectedContentPath);
                        userProfilePresenter.callUserProfilePicUploadApi(file);

                        progressDialog.show();
                       /* Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        iv_profilePic.setImageBitmap(myBitmap);
                        OutputStream os;
                        try {
                            Bitmap bitmap = null;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                            os = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                        }*/
                      /*  new Compressor.Builder(getContext()).setMaxHeight(2000f).setMaxWidth(2000f)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setQuality(80).build()
                                .compressToFileAsObservable(file)
//                                        .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<File>() {
                                    @Override
                                    public void call(final File file1) {
                                        long fileSizeInBytes = file1.length();
                                        long fileSizeInKB = fileSizeInBytes / 1024;
                                        long fileSizeInMB = fileSizeInKB / 1024;
                                        Log.e("fileSizeInMB", "" + fileSizeInMB);
                                        if (fileSizeInMB <= 4) {
                                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                            iv_profilePic.setImageBitmap(myBitmap);
//                                            uploadPictures(file);

                                        } else {
                                            Toast.makeText(getContext(), "File size should be less then 4MB", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        //	showError(throwable.getMessage());
                                        Log.e("Erroe", "err" + throwable.getMessage());
                                    }
                                });*/

                    }
                } catch (Exception e) {
                    Log.e("exception", e.toString());

                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case 13: {
                if (data == null) {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Uri selectedContentUri = data.getData();
                        String selectedContentPath = GetFilePath.getPath(getContext(), selectedContentUri); //super.getPath(selectedContentUri);
                        File file = new File(selectedContentPath);
                        userProfilePresenter.callUserProfilePicUploadApi(file);
                        progressDialog.show();
                        /*  Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        iv_profilePic.setImageBitmap(myBitmap);*/
                        /*     new Compressor.Builder(getContext()).setMaxHeight(2000f).setMaxWidth(2000f)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setQuality(80).build()
                                .compressToFileAsObservable(file)
                                //   .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<File>() {
                                    @Override
                                    public void call(final File file1) {
                                        long fileSizeInBytes = file1.length();
                                        long fileSizeInKB = fileSizeInBytes / 1024;
                                        long fileSizeInMB = fileSizeInKB / 1024;
                                        Log.e("fileSizeInMB", "" + fileSizeInMB);

                                        if (fileSizeInMB <= 4) {
                                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                            iv_profilePic.setImageBitmap(myBitmap);
//uploadPictures(file1);
                                        } else {
                                            Toast.makeText(getContext(), "File size should be less then 4MB", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        //	showError(throwable.getMessage());
                                        Log.e("Erroe", "err" + throwable.getMessage());
                                    }
                                });*/
                       /* fileExtension = FilenameUtils.getExtension(selectedContentPath);
                        Log.e("File Extensin", fileExtension);
                        if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("bmp") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png")) {
                            Log.e("filepath!*", "" + selectedContentPath);
                            Log.e("filepath!*", "" + selectedContentPath);



                        } else {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
        }
    }

   /* private void uploadPictures(File result) {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("text/plain"), result);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", result.getName(), reqFile);
        Log.e("uri", String.valueOf(body));
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        Apiinterface apiInterface = Retrofitutils.getService();
        Call<ImageResponseModel> call = apiInterface.uploadImage(body);
        call.enqueue(new Callback<ImageResponseModel>() {
            @Override
            public void onResponse(Call<ImageResponseModel> call,
                                   Response<ImageResponseModel> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ImageResponseModel> call, Throwable t) {
                Helper.Loge("UploadFailure", t.getMessage());
                pDialog.dismiss();
            }
        });
    }*/


    private void replaceFragment(Fragment fragment) {

    }

    @Override
    public void setUserProfilePicResponse(UserProfilePicModel userProfilePicModel) {
        if (userProfilePicModel != null && userProfilePicModel.success) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);
            Glide.with(getContext()).load(userProfilePicModel.data).apply(options).into(iv_profilePic);

        }
        progressDialog.dismiss();

    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfileDataResponse(UserProfileUpDataResponse userProfileUpDataResponse) {
        if (userProfileUpDataResponse != null && userProfileUpDataResponse.success) {
            userProfilePresenter.callUserProfileApi(AppPreferences.getInstance(getContext()).getPreferencesString(AppConstants.STUDENTID));
        }

    }

    @Override
    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null && userProfileResponse.success) {
            DataHolder.getInstance().getUserProfileDataresponse = userProfileResponse;
            tv_userName.setEnabled(false);
            tv_userName.setBackground(getContext().getResources().getDrawable(R.drawable.simple_cool_grey_background));
            if (DataHolder.getInstance().getUserProfileDataresponse != null && DataHolder.getInstance().getUserProfileDataresponse.data != null && DataHolder.getInstance().getUserProfileDataresponse.data.name != null) {
                tv_userName.setText(DataHolder.getInstance().getUserProfileDataresponse.data.name);
                perviousUserName = DataHolder.getInstance().getUserProfileDataresponse.data.name;
            } else {
                tv_userName.setHint("Add User Name");
                perviousUserName = "";
            }
            profileNameEditing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit));
            userProfileEditable = true;
//            progressDialog.dismiss();
        }
    }
}