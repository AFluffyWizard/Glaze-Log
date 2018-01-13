package nh.glazelog;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nh.glazelog.activity.EditRecipe;
import nh.glazelog.database.DbHelper;
import nh.glazelog.database.SimpleSpinnerSaver;
import nh.glazelog.database.SimpleTextSaver;
import nh.glazelog.database.Storable;
import nh.glazelog.glaze.Cone;
import nh.glazelog.glaze.FiringCycle;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.IngredientQuantity;
import nh.glazelog.glaze.RampHold;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static nh.glazelog.Util.PERMISSION_USE_INTERNAL_STORAGE;

/**
 * Created by Nick Hansen on 10/18/2017.
 */

public class VersionFragment extends Fragment {

    Glaze gVer;
    ArrayList<FiringCycle> firingCycles;
    FiringCycle selectedFiringCycle;
    int verNum;
    LayoutInflater inflater;
    private View page;
    private static File preCompressImageFile;
    private static Uri preCompressImageUri;

    private static final double newImageScale = 0.1;

    private TextView versionField;
    private EditText versionNotes;
    private ImageView deleteVersion;
    private ImageView testTileImage;
    private File imageFile;
    private Uri imageUri;
    private TextView spgrField;
    private Button recipeEditButton;
    private TableLayout recipeMaterialsTable;
    private TableLayout recipeAdditionsTable;
    private Spinner firingcycleSpinner;
    private Button firingcycleEditButton;
    private Spinner bisqueSpinner;
    private TableLayout firingCycleTable;
    private EditText notes;

    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Bundle args = getArguments();
        gVer = args.getParcelable(KeyValues.KEY_GLAZE_VERSION);
        verNum = args.getInt(KeyValues.KEY_GLAZE_VERSION_NUMBER) + 1;
        this.inflater = inflater;
        page = inflater.inflate(R.layout.versionfragment_single, container, false);

        versionField = (TextView) page.findViewById(R.id.versionField);
        versionField.setText(new Integer(verNum).toString());

        versionNotes = (EditText) page.findViewById(R.id.versionNotes);
        versionNotes.setText(gVer.getVersionNotes());

        deleteVersion = (ImageView) page.findViewById(R.id.deleteVersionImageView);

        testTileImage = (ImageView) page.findViewById(R.id.previewImageView);
        if (!gVer.getImageUri().equals(Uri.EMPTY)) testTileImage.setImageURI(gVer.getImageUri());

        spgrField = (TextView) page.findViewById(R.id.spgrField);
        Double spgr = new Double(gVer.getSpgr());
        if (spgr != 0) spgrField.setText(spgr.toString());
        else {
            TextView spgrLabel = (TextView) page.findViewById(R.id.spgrLabel);
            TextView spgrLabel2 = (TextView) page.findViewById(R.id.spgrLabel2);
            spgrField.setVisibility(GONE);
            spgrLabel.setVisibility(GONE);
            spgrLabel2.setVisibility(GONE);
        }

        recipeEditButton = (Button) page.findViewById(R.id.recipeEditButton);

        recipeMaterialsTable = (TableLayout) page.findViewById(R.id.recipeMaterialsTable);
        recipeAdditionsTable = (TableLayout) page.findViewById(R.id.recipeAdditionsTable);
        populateRecipeTables();

        firingcycleSpinner = (Spinner) page.findViewById(R.id.firingcycleSpinner);
        firingCycles = Util.typeUntypedList(DbHelper.getSingletonInstance(getContext()).readAll(Storable.Type.FIRING_CYCLE));
        firingCycles.add(0,new FiringCycle(getString(R.string.glaze_firingcycle_makenew)));
        firingcycleSpinner.setAdapter(new FiringCycleArrayAdapter<>(getContext(),R.layout.spinner_item_small,firingCycles));

        firingcycleEditButton = (Button) page.findViewById(R.id.firingcycleEditButton);
        if (firingcycleSpinner.getSelectedItemPosition() == 0) firingcycleEditButton.setVisibility(GONE);

            bisqueSpinner = (Spinner) page.findViewById(R.id.bisqueSpinner);
        bisqueSpinner.setAdapter(new ArrayAdapter<>(getContext(),R.layout.spinner_item_small,Cone.values()));
        Util.setSpinnerSelection(bisqueSpinner,gVer.getBisquedTo());

        firingCycleTable = (TableLayout) page.findViewById(R.id.firingcycleTable);
        selectedFiringCycle = gVer.getFiringCycle();
        populateFiringcycleTable();

        notes = (EditText) page.findViewById(R.id.notes);
        notes.setText(gVer.getNotes());



        page.postDelayed(new Runnable() {
            @Override
            public void run() {
                addChangeListeners();
            }
        },Util.CONST_DELAY_ADD_LISTENER);

        return page;
    }

    /*--------------------CHANGE LISTENERS--------------------*/

    private void addChangeListeners() {
        versionNotes.addTextChangedListener(new SimpleTextSaver(getContext(),gVer, DbHelper.SingleCN.VERSION_NOTES,false));

        if (verNum == 1) deleteVersion.setVisibility(GONE);
        else {
            deleteVersion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDialog deleteVersionDialog = new DeleteDialog(getActivity(), "version", new DeleteDialog.Action() {
                        @Override
                        public void action() {
                            deleteVersion();
                        }
                    });
                    deleteVersionDialog.show();
                }
            });

        }

        recipeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editRecipeIntent = new Intent(getContext(),EditRecipe.class);
                editRecipeIntent.putExtra(KeyValues.KEY_GLAZE_EDITRECIPE,gVer);
                startActivity(editRecipeIntent);
            }
        });

        testTileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkStoragePermissions(getActivity())) dispatchCameraIntent();
                else Util.requestStoragePermissions(getActivity());
            }
        });

        firingcycleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("FIRING CYCLE SPINNER SELECTED");
                selectedFiringCycle = firingCycles.get(position);
                if (position == 0) firingcycleEditButton.callOnClick();
                else populateFiringcycleTable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("NOTHING SELECTED");
            }
        });

        firingcycleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editFcIntent = new Intent(getContext(),EditRecipe.class);
                editFcIntent.putExtra(KeyValues.KEY_FIRING_CYCLE, selectedFiringCycle);
                startActivity(editFcIntent);
            }
        });

        bisqueSpinner.setOnItemSelectedListener(new SimpleSpinnerSaver(getContext(),gVer, DbHelper.SingleCN.BISQUED_TO,false));

        notes.addTextChangedListener(new SimpleTextSaver(getContext(),gVer, DbHelper.CCN_NOTES,false));
    }


    /*--------------------ADD TABLE ROWS--------------------*/

    private void populateFiringcycleTable () {
        ArrayList<RampHold> ramps = selectedFiringCycle.getRampHolds();
        if (ramps.size() == 0) firingCycleTable.setVisibility(GONE);
        else for (RampHold rh : ramps) addFiringCycleRow(rh);
    }

    private void populateRecipeTables() {
        TextView recipeMaterialsLabel = (TextView)page.findViewById(R.id.materialsLabel);
        TextView recipeAdditionsLabel = (TextView)page.findViewById(R.id.additionsLabel);
        ArrayList<IngredientQuantity> materialsList = gVer.getMaterials();
        ArrayList<IngredientQuantity> additionsList = gVer.getAdditions();

        if (materialsList.size() == 0)  {
            recipeMaterialsLabel.setVisibility(GONE);
            recipeAdditionsLabel.setVisibility(GONE);
            recipeMaterialsTable.setVisibility(GONE);
            recipeAdditionsTable.setVisibility(GONE);
            recipeEditButton.setText(R.string.glaze_button_label_norecipe);
        }
        else {
            for (IngredientQuantity iq : materialsList) addRecipeRow(iq,recipeMaterialsTable);
            for (IngredientQuantity iq : additionsList) addRecipeRow(iq,recipeAdditionsTable);
        }
    }

    private void addRecipeRow (@Nullable IngredientQuantity iq, final TableLayout recipeTable) {
        final TableRow recipeRow = (TableRow) inflater.inflate(R.layout.tablerow_recipe,null);

        final TextView ingredient = (TextView) recipeRow.findViewById(R.id.ingredientLabel);
        final TextView amount = (TextView) recipeRow.findViewById(R.id.amountLabel);

        if (iq == null) {
            ingredient.setText("");
            amount.setText("0");
        }
        else {
            ingredient.setText(iq.getIngredientEnum().toString());
            amount.setText(Double.toString(iq.getAmount()));
        }
        recipeTable.addView(recipeRow);
    }

    private void addFiringCycleRow(@Nullable RampHold rh) {
        final TableRow firingCycleRow = (TableRow) inflater.inflate(R.layout.tablerow_firingcycle,null);

        final TextView temperature = (TextView) firingCycleRow.findViewById(R.id.temperatureLabel);
        final TextView rate = (TextView) firingCycleRow.findViewById(R.id.rateLabel);
        final TextView hold = (TextView) firingCycleRow.findViewById(R.id.holdLabel);

        if (rh == null) {
            temperature.setText("");
            rate.setText("");
            hold.setText("");
        }
        else {
            temperature.setText(new Double(rh.getTemperature()).toString());
            rate.setText(new Double(rh.getRate()).toString());
            hold.setText(new Double(rh.getHold()).toString());
        }
        firingCycleTable.addView(firingCycleRow);
    }


    /*--------------------ADDING AND REMOVING VERSIONS--------------------*/

    VersionPager versionPager;
    VersionPagerAdapter versionPagerAdapter;
    // these two MUST be instantiated inside each function to avoid a NullPointerException

    private void addNewVersion() {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPagerAdapter.addPage();
        versionPager.setCurrentItem(versionPagerAdapter.getCount()-1);
    }

    private void deleteVersion () {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPager.setCurrentItem(verNum-2,true);
        versionPagerAdapter.deletePage(gVer);
    }




    /*--------------------TAKE AND SAVE PICTURE OF TEST TILE--------------------*/

    private void initPreCompressFile() {
        if (preCompressImageFile == null) preCompressImageFile = new File(Environment.getExternalStorageDirectory() + getString(R.string.__imagespath), "pre_compress_camera_img.jpg");

        if (preCompressImageUri == null) {
            preCompressImageUri = Util.getVersionSpecificUri(getContext(),preCompressImageFile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("REQUEST CODE MATCHES: " + (requestCode == PERMISSION_USE_INTERNAL_STORAGE));
        System.out.println("PERMISSION GRANTED: " + (grantResults[0] == PackageManager.PERMISSION_GRANTED));
        if (requestCode == PERMISSION_USE_INTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                dispatchCameraIntent();
    }


    public void dispatchCameraIntent() {
        initPreCompressFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,preCompressImageUri);
        //if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, KeyValues.KEY_REQUEST_IMAGE_CAPTURE);
        //}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resultCodeType = "UNKNOWN RESULT CODE";
        if (resultCode == RESULT_OK) resultCodeType = "RESULT_OK";
        else if (resultCode == RESULT_CANCELED) resultCodeType = "RESULT_CANCELED";
        else if (resultCode == RESULT_FIRST_USER) resultCodeType = "RESULT_FIRST_USER";
        System.out.println("Request code matches take image key: " + (requestCode == KeyValues.KEY_REQUEST_IMAGE_CAPTURE));
        System.out.println("Img Intent result code: " + resultCodeType);

        if (requestCode == KeyValues.KEY_REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageFile = new File(Environment.getExternalStorageDirectory() + getString(R.string.__imagespath),gVer.getDateCreatedRaw() + ".png");
            FileOutputStream outputStream = null;
            if (imageFile.exists()) imageFile.delete();
            else imageFile.mkdirs();

            // get image, compress it, and rotate it
            Bitmap imageBitmap = BitmapFactory.decodeFile(preCompressImageFile.toString());
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int)(imageBitmap.getWidth()*newImageScale), (int)(imageBitmap.getHeight()*newImageScale), true);
            Matrix rotate = new Matrix();
            rotate.postRotate(90);
            imageBitmap = Bitmap.createBitmap(imageBitmap , 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), rotate, true);

            try{
                imageFile.createNewFile();
                outputStream = new FileOutputStream(imageFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                onActivityResult(requestCode,resultCode,data);
            } finally {
                preCompressImageFile.delete();
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            imageUri = Util.getVersionSpecificUri(getContext(), imageFile);

            ContentValues imageUriCV= new ContentValues();
            imageUriCV.put(DbHelper.SingleCN.IMAGE_URI_STRING,imageUri.toString());
            DbHelper.getSingletonInstance(getContext()).append(gVer,imageUriCV);
            System.out.println("Image saved with Uri " + imageUri.toString());
            testTileImage.setImageBitmap(imageBitmap);
        }
    }

}
