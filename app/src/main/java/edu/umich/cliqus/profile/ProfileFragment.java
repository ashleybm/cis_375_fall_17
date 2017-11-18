package edu.umich.cliqus.profile;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import edu.umich.cliqus.NavBar.NavDrawerActivity;
import edu.umich.cliqus.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String PROFILE = "profile";
    public static final int PICK_PROFILE_IMAGE = 1;
    public static final int PICK_BACKGROUND_IMAGE = 2;

    private Profile profile = null;
    private TextView name;
    private ImageView userCoverPhoto;
    private ImageView userPhoto;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private StorageReference mStorageRef;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profile Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String PROFILE) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("profiledata", PROFILE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            Bundle bundle = this.getArguments();
            profile = (Profile) bundle.getSerializable("profiledata");
            if (profile == null) {
                Log.w("Cliqus", "null for days");
            } else
                Log.w("Cliqus", "no null for days");
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (TextView) view.findViewById(R.id.user_profile_name);
        userCoverPhoto = (ImageView) view.findViewById(R.id.header_cover_image);
        userPhoto = (ImageView) view.findViewById(R.id.user_profile_photo);

        if(profile != null) {
            name.setText(profile.getFirstName() + " " + profile.getLastName());
        }
        setImages();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.w("Cliqus", "ActivityResult " + requestCode );
        if(data != null) {
            if (requestCode == PICK_PROFILE_IMAGE) {
                userPhoto.setImageBitmap(null);

                Uri selectedImageUri = data.getData();

                Log.w("Cliqus", "image filepath = " + selectedImageUri.getPath());
                userPhoto.setImageURI(selectedImageUri);

                StorageReference riversRef = mStorageRef.child("profile_image")
                        .child(FirebaseAuth.getInstance().getUid());

                uploadImage(selectedImageUri, riversRef);
            } else if (requestCode == PICK_BACKGROUND_IMAGE) {
                userCoverPhoto.setImageBitmap(null);
                Uri selectedImageUri = data.getData();

                StorageReference riversRef = mStorageRef.child("cover_image")
                        .child(FirebaseAuth.getInstance().getUid());

                Log.w("Cliqus", "image filepath = " + selectedImageUri.getPath());
                userCoverPhoto.setImageURI(selectedImageUri);
                uploadImage(selectedImageUri, riversRef);

            }
        } else {
            Log.w("cliqus", "no image was selected!!!");
        }
    }

    public void uploadImage(final Uri image, final StorageReference riversRef) {

        riversRef.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.w("cliqus", image.getPath() + " upload passed: ");
                        if(riversRef.getPath().contains("cover_image")) {
                            profile.setCoverPhotoSet(true);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(profile);

                        } else if(riversRef.getPath().contains("profile_image")) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(profile);
                            profile.setCoverPhotoSet(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.w("cliqus", "upload failed");
                    }
                });
    }

    public void setImages() {
        if(userCoverPhoto != null && profile != null) {
            if(profile.isCoverPhotoSet()) {
                Log.w("cliqus", "fetching cover data");

                StorageReference riversRef = mStorageRef.child("profile_image")
                        .child(FirebaseAuth.getInstance().getUid());

                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap image = BitmapFactory.decodeFile(
                                            localFile.getAbsolutePath());

                                    userPhoto.setImageBitmap(image);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.w("cliqus", "avatar download failed");
                        }
                    });} catch (IOException e) {
                    e.printStackTrace();
                }

            }

            userPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PROFILE_IMAGE);
                }
            });
        }

        if(userCoverPhoto != null && profile != null) {
            if(profile.isCoverPhotoSet()) {
                Log.w("cliqus", "fetching cover data");

                StorageReference riversRef = mStorageRef.child("cover_image")
                        .child(FirebaseAuth.getInstance().getUid());

                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap image = BitmapFactory.decodeFile(
                                            localFile.getAbsolutePath());

                                    userCoverPhoto.setImageBitmap(image);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.w("cliqus", "cover download failed");
                        }
                    });} catch (IOException e) {
                    e.printStackTrace();
                }

            }

            userCoverPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_BACKGROUND_IMAGE);
                }
            });
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
